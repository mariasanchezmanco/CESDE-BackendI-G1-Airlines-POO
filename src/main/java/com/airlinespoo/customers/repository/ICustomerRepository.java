package com.airlinespoo.customers.repository;

import com.airlinespoo.customers.model.Customer;

import java.util.List;

public interface ICustomerRepository {
    void crear(Customer customer);
    Customer buscarPorId(String id);
    List<Customer> ListarTodos();
    void actualizar(Customer customer);
    void eliminar(String id);
}
