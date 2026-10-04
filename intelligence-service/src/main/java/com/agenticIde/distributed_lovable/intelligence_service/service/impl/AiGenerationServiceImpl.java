package com.agenticIde.distributed_lovable.intelligence_service.service.impl;


import com.agenticIde.distributed_lovable.comman_lib.enums.ChatEventStatus;
import com.agenticIde.distributed_lovable.comman_lib.enums.ChatEventType;
import com.agenticIde.distributed_lovable.comman_lib.enums.MessageRole;
import com.agenticIde.distributed_lovable.comman_lib.error.ResourceNotFoundException;
import com.agenticIde.distributed_lovable.comman_lib.event.FileStoreRequestEvent;
import com.agenticIde.distributed_lovable.comman_lib.security.AuthUtil;
import com.agenticIde.distributed_lovable.intelligence_service.client.WorkspaceClient;
import com.agenticIde.distributed_lovable.intelligence_service.dto.chat.StreamResponse;
import com.agenticIde.distributed_lovable.intelligence_service.entity.ChatEvent;
import com.agenticIde.distributed_lovable.intelligence_service.entity.ChatMessage;
import com.agenticIde.distributed_lovable.intelligence_service.entity.ChatSession;
import com.agenticIde.distributed_lovable.intelligence_service.entity.ChatSessionId;
import com.agenticIde.distributed_lovable.intelligence_service.llm.CodeGenerationTools;
import com.agenticIde.distributed_lovable.intelligence_service.llm.FileTreeContextAdvisor;
import com.agenticIde.distributed_lovable.intelligence_service.llm.LlmResponseParser;
import com.agenticIde.distributed_lovable.intelligence_service.llm.PromptUtils;
import com.agenticIde.distributed_lovable.intelligence_service.repository.ChatEventRepository;
import com.agenticIde.distributed_lovable.intelligence_service.repository.ChatMessageRepository;
import com.agenticIde.distributed_lovable.intelligence_service.repository.ChatSessionRepository;
import com.agenticIde.distributed_lovable.intelligence_service.service.AiGenerationService;
import com.agenticIde.distributed_lovable.intelligence_service.service.UsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final UsageService usageService;
    private final ChatSessionRepository chatSessionRepository;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final LlmResponseParser llmResponseParser;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository chatEventRepository;
    private final WorkspaceClient workspaceClient;
    private final KafkaTemplate<String,Object> kafkaTemplate;


    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<StreamResponse> streamResponse(String userMessage, Long projectId) {
//        usageService.checkDailyTokensUsage();
        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = createChatSessionIfNotExists(projectId, userId);

        Map<String, Object> advisorParam = Map.of(
                "userId", userId,
                "projectId", projectId
        );
        StringBuilder fullresponseBuffer = new StringBuilder();

        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectId,workspaceClient);

        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);
        AtomicReference<Usage> usageRef = new AtomicReference<>();

        return chatClient.prompt().system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                            advisorSpec.params(advisorParam);
                            advisorSpec.advisors(fileTreeContextAdvisor);
                        }
                )
                .stream()
                .chatResponse()
                .doOnNext((res) -> {
                    if (res.getResults() != null && !res.getResults().isEmpty()) {
                        String content = res.getResult().getOutput().getText();

                        if(content != null && !content.isEmpty() && endTime.get() == 0) { // first non-empty chunk received
                            endTime.set(System.currentTimeMillis());
                        }

                        if(res.getMetadata().getUsage() != null) {
                            usageRef.set(res.getMetadata().getUsage());
                        }

                        fullresponseBuffer.append(content);
                    }

//                    String content = res.getResult().getOutput().getText();
//
//                    if (content != null && !content.isEmpty() && endTime.get() == 0) {
//                        endTime.set(System.currentTimeMillis());
//                    }
//
//                    if (res.getMetadata().getUsage() != null) {
//                        usageRef.set(res.getMetadata().getUsage());
//                    }
//
//                    fullresponseBuffer.append(content);
                }).doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(() -> {
//                        parseAndSaveFiles(fullresponseBuffer.toString(), projectId);
                        long duration = (endTime.get() - startTime.get()) / 1000;
                        finalizeChat(userMessage, chatSession, fullresponseBuffer.toString(), duration, usageRef.get(),userId);
                    });


                })
                .doOnError(error -> {
                    log.error("Error During Streaming the project " + error);
                    log.error("Exception: {}", error.getClass().getName());
                    log.error("Message: {}", error.getMessage());
                    log.error("Cause: " + error.getCause());
                })
                .map(response -> {
                    if (response.getResults() != null && !response.getResults().isEmpty()) {
                        String text = response.getResult().getOutput().getText();
                        return new StreamResponse(text != null ? text : "");
                    }
                    return new StreamResponse("");
                });
    }

    private void finalizeChat(String userMessage, ChatSession chatSession, String fullText, Long duration, Usage usage, Long userId) {

        Long projectId = chatSession.getId().getProjectId();

        if (usage != null) {
            int totalTokens = usage.getTotalTokens();
            usageService.recordTokenUsage(chatSession.getId().getUserId(), totalTokens);
        }

        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .role(MessageRole.USER)
                        .content(userMessage)
                        .tokenUsed(usage.getPromptTokens())
                        .build()
        );

        ChatMessage assistentChatMessage = ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .content("Assistent Message here...")
                .chatSession(chatSession)
                .tokenUsed(usage.getCompletionTokens())
                .build();

        assistentChatMessage = chatMessageRepository.save(assistentChatMessage);


        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText, assistentChatMessage);

        chatEventList.addFirst(ChatEvent.builder()
                .type(ChatEventType.THOUGHT)
                .status(ChatEventStatus.CONFIRMED)
                .chatMessage(assistentChatMessage)
                .content("Thought for " + duration + "s")
                .sequenceOrder(0)
                .build());

        chatEventList.stream()
                .filter(e -> e.getType() == ChatEventType.FILE_EDIT)
                .forEach(e -> {
                    String sagaId = UUID.randomUUID().toString();
                    e.setSagaId(sagaId);
                    FileStoreRequestEvent fileStoreRequestEvent = new FileStoreRequestEvent(
                            projectId,
                            sagaId,
                            e.getFilePath(),
                            e.getContent(),
                            userId
                    );
                    log.info("Storage request event sent: {}",e.getFilePath());
                    // passing topic, key and the data
                    kafkaTemplate.send("file-storage-request-event","project-"+projectId, fileStoreRequestEvent);
//                    projectFileService.saveFile(projectId, e.getFilePath(), e.getContent())
                });

        chatEventRepository.saveAll(chatEventList);

    }

//    private void parseAndSaveFiles(String fullRespone, Long projectId) {
//
//        Matcher matcher = FILE_TAG_PATTERN.matcher(fullRespone);
//        while(matcher.find()){
//            String filePath = matcher.group(1);
//            String fileContent = matcher.group(2).trim();
//
//            projectFileService.saveFile(projectId,filePath,fileContent);
//
//    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId, userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);

        if (chatSession == null) {


            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .build();

            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}
