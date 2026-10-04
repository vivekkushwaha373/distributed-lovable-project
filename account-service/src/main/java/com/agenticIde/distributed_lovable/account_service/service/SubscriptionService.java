package com.agenticIde.distributed_lovable.account_service.service;

import com.agenticIde.distributed_lovable.account_service.dto.subscrption.SubscriptionResponse;
import com.agenticIde.distributed_lovable.comman_lib.dto.PlanDto;
import com.agenticIde.distributed_lovable.comman_lib.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {

    SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId,Long planId,String subscriptionId,String customerId);

    void updateSubscription(String gatewaysubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubsciption(String subscriptionId);

    void renewSubscriptionPeriod(String subId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subId);

    PlanDto getCurrentSubscribedPlanByUser();

//    boolean canCreateNewProject();
}
