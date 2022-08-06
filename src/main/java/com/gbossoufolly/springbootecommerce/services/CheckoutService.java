package com.gbossoufolly.springbootecommerce.services;

import com.gbossoufolly.springbootecommerce.dto.PaymentInfo;
import com.gbossoufolly.springbootecommerce.dto.Purchase;
import com.gbossoufolly.springbootecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
}
