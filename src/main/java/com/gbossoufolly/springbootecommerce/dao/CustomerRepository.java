package com.gbossoufolly.springbootecommerce.dao;

import com.gbossoufolly.springbootecommerce.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String theEmail);
}
