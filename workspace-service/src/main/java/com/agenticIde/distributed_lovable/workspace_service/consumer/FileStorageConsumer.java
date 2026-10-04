package com.agenticIde.distributed_lovable.workspace_service.consumer;


import com.agenticIde.distributed_lovable.comman_lib.event.FileStoreRequestEvent;
import com.agenticIde.distributed_lovable.comman_lib.event.FileStoreResponseEvent;
import com.agenticIde.distributed_lovable.workspace_service.entity.ProcessedEvent;
import com.agenticIde.distributed_lovable.workspace_service.repository.ProcessedEventRepository;
import com.agenticIde.distributed_lovable.workspace_service.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageConsumer {

    private final ProjectFileService projectFileService;
    private final ProcessedEventRepository processedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "file-storage-request-event", groupId = "workspace-group")
    public void consumeFileEvent(FileStoreRequestEvent requestEvent) {

        if (processedEventRepository.existsById(requestEvent.sagaId())) {
            log.info("Duplicat saga Detected: {}, Resending Prev ACK .", requestEvent.sagaId());
            sendResponse(requestEvent, true, null);
            return;
        }
        try {

            log.info("Saving file: {}", requestEvent.filepath());

            projectFileService.saveFile(requestEvent.projectId(), requestEvent.filepath(), requestEvent.content());
            processedEventRepository.save(new ProcessedEvent(
                    requestEvent.sagaId(), LocalDateTime.now()
            ));

            sendResponse(requestEvent,true,null);
        } catch (Exception e) {
            log.error("Error saving file: {} ",e.getMessage());
            sendResponse(requestEvent,false,e.getMessage());
        }
    }

    private void sendResponse(FileStoreRequestEvent requestEvent, boolean success, String error) {

        FileStoreResponseEvent response = FileStoreResponseEvent.builder()
                .sagaId(requestEvent.sagaId())
                .projectId(requestEvent.projectId())
                .success(success)
                .errorMessage(error)
                .build();

        kafkaTemplate.send("file-store-responses", response);

    }


}
