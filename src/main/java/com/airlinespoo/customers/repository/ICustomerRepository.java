
package com.airlinespoo.customers.repository;

import com.airlinespoo.core.repository.Repository;
import com.airlinespoo.customers.model.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepository
    extends Repository<Customer, String> {

    // Buscar cliente por correo
    Optional<Customer> findByEmail(String email);

    // Verificar si un correo existe
    boolean existsByEmail(String email);

    // Listar clientes por tipo de documento
    List<Customer> findByDocumentType(String typeId);

    // Verificar si un tipo de documento está siendo utilizado
    boolean existsByDocumentType(String typeId);

}
