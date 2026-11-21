package com.airlinespoo.customers.ui;

import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.model.DocumentType;

import com.airlinespoo.customers.repository.CustomerRepository;
import com.airlinespoo.customers.repository.DocumentRepository;

import com.airlinespoo.customers.service.CustomerServiceImpl;
import com.airlinespoo.customers.service.DocumentServiceImpl;

import com.airlinespoo.customers.service.ICustomerService;
import com.airlinespoo.customers.service.IDocumentService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static ICustomerService customerService;
    private static IDocumentService documentService;

    public static void main(String[] args) {

        iniciarServicios();

        int opcion;

        do {
            System.out.println("\n================================");
            System.out.println("       AIRLINES - SISTEMA");
            System.out.println("================================");
            System.out.println("1. Gestionar clientes");
            System.out.println("2. Gestionar tipos de documento");
            System.out.println("0. Salir");
            System.out.println("================================");

            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuDocumentos();
                case 0 -> System.out.println("Cerrando aplicación...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // INICIALIZAR REPOSITORIOS Y SERVICIOS

    private static void iniciarServicios() {

        CustomerRepository customerRepository =
            new CustomerRepository();

        DocumentRepository documentRepository =
            new DocumentRepository();

        customerService = new CustomerServiceImpl(
            customerRepository,
            documentRepository
        );

        documentService = new DocumentServiceImpl(
            documentRepository,
            customerRepository
        );
    }

    // ==========================================
    // MENÚ DE CLIENTES
    // ==========================================

    private static void menuClientes() {

        int opcion;

        do {
            System.out.println("\n========== CLIENTES ==========");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Consultar cliente por ID");
            System.out.println("3. Listar clientes");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Listar por tipo de documento");
            System.out.println("0. Volver");
            System.out.println("==============================");

            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {

                    case 1 -> registrarCliente();

                    case 2 -> consultarCliente();

                    case 3 -> listarClientes();

                    case 4 -> actualizarCliente();

                    case 5 -> eliminarCliente();

                    case 6 -> listarClientesPorDocumento();

                    case 0 -> System.out.println("Volviendo al menú principal...");

                    default -> System.out.println("Opción no válida.");
                }

            } catch (RuntimeException e) {
                System.out.println("\nERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private static void registrarCliente() {

        System.out.println("\n--- REGISTRAR CLIENTE ---");

        String id = leerTexto("Documento del cliente: ");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");

        String typeId = leerTexto("Tipo de documento: ");

        DocumentType documentType =
            documentService.getDocumentById(typeId);

        String telefono = leerTexto("Teléfono: ");
        String pais = leerTexto("País: ");

        Customer customer = new Customer(
            id,
            nombre,
            apellido,
            documentType,
            telefono,
            pais
        );

        String correo = leerOpcional("Correo electrónico (Enter para omitir): ");
        String direccion = leerOpcional("Dirección (Enter para omitir): ");

        customer.setEmail(correo);
        customer.setAddress(direccion);

        customerService.createCustomer(customer);

        System.out.println("Cliente registrado correctamente.");
    }

    private static void consultarCliente() {

        System.out.println("\n--- CONSULTAR CLIENTE ---");

        String id = leerTexto("Ingrese el documento del cliente: ");

        Customer customer = customerService.getCustomerById(id);

        mostrarCliente(customer);
    }

    private static void listarClientes() {

        System.out.println("\n--- LISTADO DE CLIENTES ---");

        List<Customer> customers = customerService.listCustomers();

        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }

        for (Customer customer : customers) {
            mostrarCliente(customer);
            System.out.println("------------------------------");
        }
    }

    private static void actualizarCliente() {

        System.out.println("\n--- ACTUALIZAR CLIENTE ---");

        String id = leerTexto("Documento del cliente: ");

        // Comprobar que el cliente exista.
        Customer actual = customerService.getCustomerById(id);

        System.out.println("Cliente encontrado: " + actual.getFullName());

        String nombre = leerTexto("Nuevo nombre: ");
        String apellido = leerTexto("Nuevo apellido: ");

        String typeId = leerTexto("Nuevo tipo de documento: ");

        DocumentType documentType =
            documentService.getDocumentById(typeId);

        String telefono = leerTexto("Nuevo teléfono: ");
        String pais = leerTexto("Nuevo país: ");

        Customer actualizado = new Customer(
            id,
            nombre,
            apellido,
            documentType,
            telefono,
            pais
        );

        String correo = leerOpcional("Nuevo correo (Enter para dejar vacío): ");
        String direccion = leerOpcional("Nueva dirección (Enter para dejar vacía): ");

        actualizado.setEmail(correo);
        actualizado.setAddress(direccion);

        customerService.updateCustomer(actualizado);

        System.out.println("Cliente actualizado correctamente.");
    }

    private static void eliminarCliente() {

        System.out.println("\n--- ELIMINAR CLIENTE ---");

        String id = leerTexto("Documento del cliente: ");

        customerService.getCustomerById(id);

        String confirmacion = leerTexto(
            "¿Está seguro de eliminarlo? (S/N): "
        );

        if (confirmacion.equalsIgnoreCase("S")) {

            customerService.deleteCustomer(id);

            System.out.println("Cliente eliminado correctamente.");

        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private static void listarClientesPorDocumento() {

        System.out.println("\n--- CLIENTES POR DOCUMENTO ---");

        String typeId = leerTexto("Tipo de documento: ");

        List<Customer> customers =
            customerService.listByDocument(typeId);

        if (customers.isEmpty()) {
            System.out.println("No hay clientes con ese tipo de documento.");
            return;
        }

        for (Customer customer : customers) {
            mostrarCliente(customer);
            System.out.println("------------------------------");
        }
    }

    // ==========================================
    // MENÚ DE TIPOS DE DOCUMENTO
    // ==========================================

    private static void menuDocumentos() {

        int opcion;

        do {
            System.out.println("\n====== TIPOS DE DOCUMENTO ======");
            System.out.println("1. Registrar tipo de documento");
            System.out.println("2. Consultar tipo de documento");
            System.out.println("3. Listar tipos de documento");
            System.out.println("4. Actualizar tipo de documento");
            System.out.println("5. Eliminar tipo de documento");
            System.out.println("0. Volver");
            System.out.println("================================");

            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {

                    case 1 -> registrarDocumento();

                    case 2 -> consultarDocumento();

                    case 3 -> listarDocumentos();

                    case 4 -> actualizarDocumento();

                    case 5 -> eliminarDocumento();

                    case 0 -> System.out.println("Volviendo al menú principal...");

                    default -> System.out.println("Opción no válida.");
                }

            } catch (RuntimeException e) {
                System.out.println("\nERROR: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private static void registrarDocumento() {

        System.out.println("\n--- REGISTRAR TIPO DE DOCUMENTO ---");

        String id = leerTexto("Código (2 caracteres): ");
        String descripcion = leerTexto("Descripción: ");

        DocumentType documentType =
            new DocumentType(id, descripcion);

        documentService.createDocument(documentType);

        System.out.println("Tipo de documento registrado correctamente.");
    }

    private static void consultarDocumento() {

        System.out.println("\n--- CONSULTAR TIPO DE DOCUMENTO ---");

        String id = leerTexto("Código: ");

        DocumentType documentType =
            documentService.getDocumentById(id);

        mostrarDocumento(documentType);
    }

    private static void listarDocumentos() {

        System.out.println("\n--- TIPOS DE DOCUMENTO REGISTRADOS ---");

        List<DocumentType> documents =
            documentService.listDocuments();

        if (documents.isEmpty()) {
            System.out.println("No hay tipos de documento registrados.");
            return;
        }

        for (DocumentType document : documents) {
            mostrarDocumento(document);
        }
    }

    private static void actualizarDocumento() {

        System.out.println("\n--- ACTUALIZAR TIPO DE DOCUMENTO ---");

        String id = leerTexto("Código del documento: ");

        // Comprobar que exista.
        documentService.getDocumentById(id);

        String descripcion = leerTexto("Nueva descripción: ");

        DocumentType documentType =
            new DocumentType(id, descripcion);

        documentService.updateDocument(documentType);

        System.out.println("Tipo de documento actualizado correctamente.");
    }

    private static void eliminarDocumento() {

        System.out.println("\n--- ELIMINAR TIPO DE DOCUMENTO ---");

        String id = leerTexto("Código del documento: ");

        documentService.getDocumentById(id);

        String confirmacion = leerTexto(
            "¿Está seguro de eliminarlo? (S/N): "
        );

        if (confirmacion.equalsIgnoreCase("S")) {

            documentService.deleteDocument(id);

            System.out.println("Tipo de documento eliminado correctamente.");

        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // ==========================================
    // MÉTODOS PARA MOSTRAR INFORMACIÓN
    // ==========================================

    private static void mostrarCliente(Customer customer) {

        System.out.println("\nID: " + customer.getId());
        System.out.println("Nombre: " + customer.getFullName());

        System.out.println(
            "Documento: " + customer.getDocumentType()
        );

        System.out.println("Teléfono: " + customer.getTelephoneNumber());
        System.out.println("Correo: " + customer.getEmail());
        System.out.println("País: " + customer.getCountry());
        System.out.println("Dirección: " + customer.getAddress());
    }

    private static void mostrarDocumento(DocumentType document) {

        System.out.println(
            document.getId() + " - " + document.getDescription()
        );
    }

    // ==========================================
    // MÉTODOS DE ENTRADA
    // ==========================================

    private static String leerTexto(String mensaje) {

        System.out.print(mensaje);

        return scanner.nextLine().trim();
    }

    private static String leerOpcional(String mensaje) {

        System.out.print(mensaje);

        String valor = scanner.nextLine().trim();

        return valor.isEmpty() ? null : valor;
    }

    private static int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            try {
                return Integer.parseInt(scanner.nextLine().trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }
}
