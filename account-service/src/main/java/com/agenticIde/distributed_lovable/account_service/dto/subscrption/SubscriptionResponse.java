package com.agenticIde.distributed_lovable.account_service.dto.subscrption;

import com.agenticIde.distributed_lovable.comman_lib.dto.PlanDto;

import java.time.Instant;

public record SubscriptionResponse(
        PlanDto plan,
        String status,
        Instant currentPeriodPlan,
        Long tokensUsedThisCycle
) {
}
