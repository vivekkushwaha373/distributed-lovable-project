package com.agenticIde.distributed_lovable.account_service.dto.subscrption;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProjects,
        Integer unlimitedAi
) {
}
