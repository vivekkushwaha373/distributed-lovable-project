package com.agenticIde.distributed_lovable.account_service.dto.subscrption;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokenLimit,
        Integer previewsRunning,
        Integer previewLimit
) {
}
