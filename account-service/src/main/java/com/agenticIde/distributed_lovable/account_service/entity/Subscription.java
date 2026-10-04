package com.agenticIde.distributed_lovable.account_service.entity;

import com.agenticIde.distributed_lovable.comman_lib.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name= "user_id")
    User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable = false, name = "plan_id")
    Plan plan;

    @Enumerated(EnumType.STRING)
    SubscriptionStatus status;


    String stripeSubscriptionId; // to

    Instant currentPeriodStart; //substruction starts
    Instant currentPeriodEnd; // when subscription starts


    Boolean cancelAtPeriodEnd=false; // when subscription ends

    @CreationTimestamp
    Instant createdAt;

    @UpdateTimestamp
    Instant updatedAt;
}
