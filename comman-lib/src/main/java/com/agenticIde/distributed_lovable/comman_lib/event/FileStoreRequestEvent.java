package com.agenticIde.distributed_lovable.comman_lib.event;

public record FileStoreRequestEvent(
        Long projectId,
        String sagaId,
        String filepath,
        String content,
        Long userId
) {

}
