package com.gbossoufolly.springbootecommerce.services;

import com.gbossoufolly.springbootecommerce.dto.Purchase;
import com.gbossoufolly.springbootecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
