package com.agenticIde.distributed_lovable.account_service.service;


import com.agenticIde.distributed_lovable.account_service.dto.subscrption.CheckoutRequest;
import com.agenticIde.distributed_lovable.account_service.dto.subscrption.CheckoutResponse;
import com.agenticIde.distributed_lovable.account_service.dto.subscrption.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {
    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);

//    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
