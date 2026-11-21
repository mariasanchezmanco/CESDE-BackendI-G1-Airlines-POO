package com.airlinespoo.customers.service;

import com.airlinespoo.core.repository.Repository;
import com.airlinespoo.customers.model.Customer;

import java.util.List;

public interface ICustomerService {
    void createCustomer(Customer customer);
    Customer getCustomerById(String id);
    List<Customer> listCustomers();
    void updateCustomer(Customer customer);
    void deleteCustomer(String id);

    // ELIMINAR CLIENTE

    List<Customer> listByDocument(String typeId);
}
