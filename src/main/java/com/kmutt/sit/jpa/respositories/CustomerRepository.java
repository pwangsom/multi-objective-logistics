package com.kmutt.sit.jpa.respositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.kmutt.sit.jpa.entities.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}
