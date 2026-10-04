package com.agenticIde.distributed_lovable.intelligence_service.consumer;


import com.agenticIde.distributed_lovable.comman_lib.enums.ChatEventStatus;
import com.agenticIde.distributed_lovable.comman_lib.event.FileStoreResponseEvent;
import com.agenticIde.distributed_lovable.intelligence_service.entity.ChatEvent;
import com.agenticIde.distributed_lovable.intelligence_service.repository.ChatEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntelligenceSagaResponseHandler {

    private final ChatEventRepository chatEventRepository;

    @Transactional
    @KafkaListener(topics = "file-store-responses", groupId = "intelligence-group")
    public void handleSagaResponse(FileStoreResponseEvent response){
        chatEventRepository.findBySagaId(response.sagaId()).ifPresent(event->{
            if(!ChatEventStatus.PENDING.equals(event.getStatus())){
                log.info("Response for Saga {} already handled, Skipping.",response.sagaId());
                return;
            }
            if(response.success()){
                event.setStatus(ChatEventStatus.CONFIRMED);
                log.info("Saga {} CONFIRMED",response.sagaId());
            }else{
                log.warn("Saga {} FAILED DELETING event.",response.sagaId());
                event.setStatus(ChatEventStatus.FAILED);
            }
        });
    }


}
