package com.airlinespoo.airlines.ui;

import com.airlinespoo.airlines.model.Airline;
import com.airlinespoo.airlines.repository.AirlineRepository;
import com.airlinespoo.airlines.service.AirlineService;
import com.airlinespoo.core.exception.AirlinesException;
import com.airlinespoo.core.util.IdGenerator;

import java.util.List;
import java.util.Scanner;

public class AirlineMenu {

    private final AirlineService airlineService;
    private final Scanner scanner;

    public AirlineMenu() {
        // Inicializamos los componentes de manera interna y autónoma para este módulo
        AirlineRepository repository = new AirlineRepository();
        IdGenerator idGenerator = new IdGenerator(100, 10); // Arranca en 100 y suma de 10 en 10
        this.airlineService = new AirlineService(repository, idGenerator);
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int option = 0;
        while (option != 6) {
            System.out.println("\n========================================");
            System.out.println("       MENÚ DE GESTIÓN DE AEROLÍNEAS     ");
            System.out.println("========================================");
            System.out.println("1. Registrar nueva aerolínea");
            System.out.println("2. Buscar aerolínea por ID");
            System.out.println("3. Listar todas las aerolíneas");
            System.out.println("4. Renombrar aerolínea");
            System.out.println("5. Eliminar aerolínea");
            System.out.println("6. Salir al menú principal");
            System.out.print("Seleccione una opción: ");

            try {
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer de entrada

                    switch (option) {
                        case 1:
                            registerAirline();
                            break;
                        case 2:
                            findAirlineById();
                            break;
                        case 3:
                            listAllAirlines();
                            break;
                        case 4:
                            renameAirline();
                            break;
                        case 5:
                            deleteAirline();
                            break;
                        case 6:
                            System.out.println("Saliendo del módulo de aerolíneas...");
                            break;
                        default:
                            System.out.println("⚠️ Opción no válida. Intente de nuevo.");
                    }
                } else {
                    System.out.println("⚠️ Por favor, ingrese un número válido.");
                    scanner.nextLine(); // Limpiar entrada errónea
                }
            } catch (AirlinesException e) {
                System.out.println("❌ Error de negocio: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Ocurrió un error inesperado: " + e.getMessage());
            }
        }
    }

    private void registerAirline() {
        System.out.print("\nIngrese el nombre de la nueva aerolínea: ");
        String name = scanner.nextLine();
        Airline created = airlineService.createAirline(name);
        System.out.println("✅ ¡Aerolínea registrada con éxito! Detalles: " + created);
    }

    private void findAirlineById() {
        System.out.print("\nIngrese el ID de la aerolínea a buscar: ");
        if (scanner.hasNextInt()) {
            Integer id = scanner.nextInt();
            scanner.nextLine();
            Airline airline = airlineService.getAirline(id);
            System.out.println("🔍 Aerolínea encontrada: " + airline);
        } else {
            System.out.println("⚠️ ID inválido.");
            scanner.nextLine();
        }
    }

    private void listAllAirlines() {
        System.out.println("\n--- LISTADO DE AEROLÍNEAS (Orden alfabético) ---");
        List<Airline> airlines = airlineService.listAirlines();
        if (airlines.isEmpty()) {
            System.out.println("No hay aerolíneas registradas actualmente.");
        } else {
            for (Airline a : airlines) {
                System.out.println(" - ID: " + a.getId() + " | Nombre: " + a.getName());
            }
            System.out.println("Total registradas: " + airlineService.countAirlines());
        }
    }

    private void renameAirline() {
        System.out.print("\nIngrese el ID de la aerolínea que desea renombrar: ");
        if (scanner.hasNextInt()) {
            Integer id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese el nuevo nombre: ");
            String newName = scanner.nextLine();
            Airline updated = airlineService.renameAirline(id, newName);
            System.out.println("✅ ¡Aerolínea actualizada con éxito! " + updated);
        } else {
            System.out.println("⚠️ ID inválido.");
            scanner.nextLine();
        }
    }

    private void deleteAirline() {
        System.out.print("\nIngrese el ID de la aerolínea que desea eliminar: ");
        if (scanner.hasNextInt()) {
            Integer id = scanner.nextInt();
            scanner.nextLine();
            airlineService.deleteAirline(id);
            System.out.println("🗑️ Aerolínea eliminada correctamente.");
        } else {
            System.out.println("⚠️ ID inválido.");
            scanner.nextLine();
        }
    }
}
