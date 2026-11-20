package com.airlinespoo.app;

import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.model.DocumentType;
import com.airlinespoo.customers.repository.CustomerRepository;

import java.util.List;

/**
 * Punto de entrada de Airlines-poo.
 *
 * <p>Esta es una clase <b>provisional</b> que entrega el docente para que el proyecto se
 * pueda ejecutar desde el primer día. El estudiante del Módulo 3 la reemplaza al final
 * por la versión que ensambla todos los módulos (lector de consola, generadores de ids,
 * repositorios, servicios, datos de ejemplo y menús) y arranca el menú principal.</p>
 */
public final class Main {

    private Main() {
        // Clase de arranque: no se instancia.
    }

    /**
     * Arranca la aplicación.
     *
     * @param args argumentos de la línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        System.out.println("=== Airlines-poo ===");
        System.out.println("Proyecto base listo. El kit base (paquete core) está disponible.");
        System.out.println("Cada módulo se integra en develop mediante Pull Request.");
        // TODO (Módulo 3): ensamblar aquí lector, generadores, repositorios, servicios y menús.
        CustomerRepository repository = new CustomerRepository();

        DocumentType documentType =
            new DocumentType("CC", "Cedula de ciudadania");

// CREAR
        Customer customer = new Customer(
            "123456",
            "Juan",
            "Perez",
            documentType,
            "+573001234567",
            "Colombia"
        );

        customer.setEmail("juan@gmail.com");
        customer.setAddress("Calle 10");

        repository.crear(customer);

// BUSCAR
        Customer encontrado = repository.buscarPorId("123456");

        if (encontrado != null) {
            System.out.println(encontrado);
            System.out.println(encontrado.getEmail());
            System.out.println(encontrado.getCountry());
            System.out.println(encontrado.getDocumentType());
        }

// LISTAR
        List<Customer> customers = repository.ListarTodos();

        for (Customer c : customers) {
            System.out.println(c);
        }

// ACTUALIZAR
        Customer actualizado = new Customer(
            "123456",
            "Juan Carlos",
            "Perez",
            documentType,
            "+573001234567",
            "Colombia"
        );

        actualizado.setEmail("juancarlos@gmail.com");
        actualizado.setAddress("Carrera 20");

        repository.actualizar(actualizado);

// ELIMINAR
        repository.eliminar("123456");
    }
}
