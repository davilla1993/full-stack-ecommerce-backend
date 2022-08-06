package com.gbossoufolly.springbootecommerce.services.impl;

import com.gbossoufolly.springbootecommerce.dao.CustomerRepository;
import com.gbossoufolly.springbootecommerce.dto.PaymentInfo;
import com.gbossoufolly.springbootecommerce.dto.Purchase;
import com.gbossoufolly.springbootecommerce.dto.PurchaseResponse;
import com.gbossoufolly.springbootecommerce.entities.Customer;
import com.gbossoufolly.springbootecommerce.entities.Order;
import com.gbossoufolly.springbootecommerce.entities.OrderItem;
import com.gbossoufolly.springbootecommerce.services.CheckoutService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServiceImpl(CustomerRepository customerRepository,
                                @Value("${stripe.key.secret}") String secretKey) {

        this.customerRepository = customerRepository;

        // initialize Stripe API with secret key
        Stripe.apiKey = secretKey;
    }

    @Override
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress((purchase.getShippingAddress()));

        // populate customer with order
        Customer customer = purchase.getCustomer();

        // check if this is an existing customer
        String theEmail = customer.getEmail();
        Customer customerFromDB = customerRepository.findByEmail(theEmail);
        if(customerFromDB != null) {
            // we found them... let's assign them accordingly
            customer = customerFromDB;
        }

        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Luv2Shop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4)
        // For details see: https://en.wikipedia.org/wiki/Universally_unique_identifier
        return UUID.randomUUID().toString();
    }
}
