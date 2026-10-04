package com.agenticIde.distributed_lovable.workspace_service.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="processed_events")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ProcessedEvent {

    @Id
    private String sagaIdl;
    private LocalDateTime processedAt;
    
}
