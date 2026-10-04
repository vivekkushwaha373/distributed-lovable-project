package com.agenticIde.distributed_lovable.comman_lib.event;

import lombok.Builder;

@Builder
public record FileStoreResponseEvent(
        String sagaId,
        boolean success,
        String errorMessage,
        Long projectId
) {}
