package com.agenticIde.distributed_lovable.account_service.mapper;

import com.agenticIde.distributed_lovable.account_service.dto.subscrption.SubscriptionResponse;

import com.agenticIde.distributed_lovable.account_service.entity.Plan;
import com.agenticIde.distributed_lovable.account_service.entity.Subscription;
import com.agenticIde.distributed_lovable.comman_lib.dto.PlanDto;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanDto toPlanResponse(Plan plan);
}
