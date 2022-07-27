package com.gbossoufolly.springbootecommerce.dto;

import com.gbossoufolly.springbootecommerce.entities.Address;
import com.gbossoufolly.springbootecommerce.entities.Customer;
import com.gbossoufolly.springbootecommerce.entities.Order;
import com.gbossoufolly.springbootecommerce.entities.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
