
package com.airlinespoo.customers.service;

import com.airlinespoo.core.exception.NotFoundException;
import com.airlinespoo.core.exception.DuplicateException;

import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.repository.ICustomerRepository;
import com.airlinespoo.customers.repository.IDocumentRepository;

import java.util.List;
import java.util.Comparator;

public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository customerRepository;
    private final IDocumentRepository documentRepository;

    public CustomerServiceImpl(
        ICustomerRepository customerRepository,
        IDocumentRepository documentRepository) {

        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
    }

    // REGISTRAR CLIENTE
    @Override
    public void createCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                "El cliente no puede ser nulo."
            );
        }

        // Validar documento duplicado
        if (customerRepository.existsById(customer.getId())) {
            throw new DuplicateException(
                "cliente", customer.getId()
            );
        }

        // Validar correo duplicado
        if (customer.getEmail() != null &&
            !customer.getEmail().isBlank()) {

            if (customerRepository.existsByEmail(customer.getEmail())) {
                throw new DuplicateException(
                    "Ya existe un cliente con el correo: " +
                        customer.getEmail()
                );
            }
        }

        // Validar tipo de documento
        String typeId = customer.getDocumentType().getId();

        if (documentRepository.findById(typeId).isEmpty()) {
            throw new NotFoundException("tipo de documento", typeId);
        }

        if (documentRepository.findById(typeId).isEmpty()) {
            throw new NotFoundException(
                "tipo de documento", typeId
            );
        }

        customerRepository.create(customer);
    }

    // BUSCAR POR ID
    @Override
    public Customer getCustomerById(String id) {

        return customerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("cliente", id));
    }

    // LISTAR CLIENTES
    @Override
    public List<Customer> listCustomers() {

        List<Customer> customers = customerRepository.findAll();

        customers.sort(
            Comparator
                .comparing(
                    Customer::getLastName,
                    String.CASE_INSENSITIVE_ORDER
                )
                .thenComparing(
                    Customer::getFirstName,
                    String.CASE_INSENSITIVE_ORDER
                )
        );

        return customers;
    }

    // ACTUALIZAR CLIENTE
    @Override
    public void updateCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException(
                "El cliente no puede ser nulo."
            );
        }

        // Verificar que el cliente exista
        customerRepository.findById(customer.getId())
            .orElseThrow(
                () -> new NotFoundException("cliente", customer.getId())
            );

        // Validar correo sin chocar consigo mismo
        if (customer.getEmail() != null &&
            !customer.getEmail().isBlank()) {

            customerRepository.findByEmail(customer.getEmail())
                .ifPresent(propietario -> {
                    if (!propietario.getId().equals(customer.getId())) {
                        throw new DuplicateException(
                            "El correo pertenece a otro cliente: " +
                                customer.getEmail()
                        );
                    }
                });
        }

        // Validar tipo de documento
        if (customer.getDocumentType() == null ||
            customer.getDocumentType().getId() == null ||
            customer.getDocumentType().getId().isBlank()) {

            throw new IllegalArgumentException(
                "Debe seleccionar un tipo de documento."
            );
        }

        String typeId = customer.getDocumentType().getId();

        if (documentRepository.findById(typeId).isEmpty()) {
            throw new NotFoundException(
                "tipo de documento", typeId
            );
        }

        customerRepository.update(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("cliente", id)
            );

        customerRepository.deleteById(id);
    }

    // ELIMINAR CLIENTE

    // LISTAR POR TIPO DE DOCUMENTO
    @Override
    public List<Customer> listByDocument(String typeId) {

        if (typeId == null || typeId.isBlank()) {
            throw new IllegalArgumentException(
                "Debe ingresar un tipo de documento."
            );
        }

        if (documentRepository.findById(typeId).isEmpty()) {
            throw new NotFoundException(
                "tipo de documento", typeId
            );
        }

        return customerRepository.findByDocumentType(typeId);
    }

}
