package com.agenticIde.distributed_lovable.intelligence_service.dto.chat;


import com.agenticIde.distributed_lovable.comman_lib.enums.ChatEventType;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
