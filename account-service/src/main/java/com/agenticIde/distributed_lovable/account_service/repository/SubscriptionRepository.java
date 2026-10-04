package com.agenticIde.distributed_lovable.account_service.repository;

import com.agenticIde.distributed_lovable.account_service.entity.Subscription;
import com.agenticIde.distributed_lovable.comman_lib.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    /*
    * Get the Current active Subscription
    * 
    * */
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> statusSet);

    boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subscription> findByStripeSubscriptionId(String gatewaySubscriptionId);
}
