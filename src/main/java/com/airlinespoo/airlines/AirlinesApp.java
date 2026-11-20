package com.airlinespoo.airlines;

import com.airlinespoo.airlines.repository.JdbcAirlineRepository;
import com.airlinespoo.airlines.service.AirlineService;
import com.airlinespoo.airlines.ui.AirlineMenu;
import com.airlinespoo.core.ui.ConsoleInput;
import com.airlinespoo.core.util.IdGenerator;
import com.airlinespoo.customers.repository.CountryRepository;
import com.airlinespoo.customers.repository.DatabaseConnection;
import com.airlinespoo.customers.service.CountryService;
import com.airlinespoo.customers.service.CustomerService;
import com.airlinespoo.customers.ui.CountryMenu;
import com.airlinespoo.flights.repository.JdbcFlightRepository;
import com.airlinespoo.flights.service.FlightService;
import com.airlinespoo.flights.ui.FlightMenu;
import com.airlinespoo.locations.repository.*;
import com.airlinespoo.locations.service.*;
import com.airlinespoo.locations.ui.LocationsMenu;
import com.airlinespoo.reservations.repository.JdbcReservationRepository;
import com.airlinespoo.reservations.service.ReservationService;
import com.airlinespoo.reservations.ui.ReservationMenu;
import java.sql.*;

/** Ejecutar este main desde IntelliJ. El Main del profesor no se modifica. */
public class AirlinesApp {
    public static void main(String[] args) {
        // Para ejecutar con el Main del profesor habría que llamar aquí a AirlinesApp.main(args).
        // Como no podemos modificar ese archivo, arrancamos directamente desde esta clase.
        try (Connection conn=DatabaseConnection.getConnection()) {
            // No seguimos si PostgreSQL no está disponible o las tablas no existen.
            try(Statement st=conn.createStatement()){
                for(String table:new String[]{"tblcountries","tblcities","tblairports","tblairlines","tblcustomers","tblflights","tblreservations","tbldetailsreservation"})
                    st.executeQuery("SELECT 1 FROM "+table+" LIMIT 1").close();
            }
            System.out.println("Conectado a PostgreSQL: "+conn.getCatalog());
        } catch(SQLException e) {
            System.err.println("No se pudo iniciar con PostgreSQL: "+e.getMessage());
            System.err.println("Revisa que PostgreSQL este iniciado y que la base airlinespoo tenga las tablas creadas.");
            return;
        }
        ConsoleInput input=new ConsoleInput();
        JdbcAirlineRepository airlinesRepo=new JdbcAirlineRepository();
        AirlineService airlines=new AirlineService(airlinesRepo,ids("tblairlines","airlineid",100,10));
        PostgresConnection db=new PostgresConnection();
        CityRepository citiesRepo=new CityRepository(db);
        AirportRepository airportsRepo=new AirportRepository(db);
        CityService cities=new CityService(citiesRepo,airportsRepo);
        AirportService airports=new AirportService(airportsRepo,citiesRepo);
        LocationService locations=new LocationService(airports);
        FlightService flights=new FlightService(new JdbcFlightRepository(airlinesRepo,airportsRepo),locations,airlines,ids("tblflights","flightid",1,1));
        CustomerService customers=new CustomerService();
        ReservationService reservations=new ReservationService(
            new JdbcReservationRepository(DatabaseConnection::getConnection,customers::getCustomer,flights::getFlight),
            customers,flights,ids("tblreservations","reservationid",1,1),ids("tbldetailsreservation","detailid",1,1));
        CountryService countries=new CountryService(new CountryRepository());
        while(true){
            System.out.println("\n=== AIRLINES POO - POSTGRESQL ===");
            System.out.println("1. Aerolíneas");System.out.println("2. Países");
            System.out.println("3. Ciudades y aeropuertos");System.out.println("4. Vuelos");
            System.out.println("5. Clientes");System.out.println("6. Reservas");System.out.println("0. Salir");
            int option=input.readInt("Selecciona una opción",0,6);
            try{
                switch(option){
                    case 1 -> new AirlineMenu(airlines,input).run();
                    case 2 -> new CountryMenu(countries,input).run();
                    case 3 -> new LocationsMenu(input,cities,airports).run();
                    case 4 -> new FlightMenu(flights,locations,airlines,input).run();
                    case 5 -> customerMenu(input,customers);
                    case 6 -> new ReservationMenu(input,reservations,customers,flights).run();
                    case 0 -> {System.out.println("Hasta luego.");return;}
                }
            }catch(RuntimeException e){System.out.println("No se pudo completar: "+e.getMessage());}
        }
    }
    private static IdGenerator ids(String table,String column,int first,int step){
        IdGenerator gen=new IdGenerator(first,step);
        // El último id almacenado debe tenerse en cuenta después de reiniciar.
        try(Connection c=DatabaseConnection.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT COALESCE(MAX("+column+"),0) FROM "+table)){
            if(rs.next())gen.syncWith(rs.getInt(1));return gen;
        }catch(SQLException e){throw new IllegalStateException("No se pudo revisar los ids de "+table,e);}
    }
    private static void customerMenu(ConsoleInput input,CustomerService customers){
        while(true){
            System.out.println("\n--- Clientes ---");System.out.println("1. Registrar  2. Consultar  3. Listar  0. Volver");
            int op=input.readInt("Elige una opción",0,3);
            try{
                switch(op){
                    case 1 -> System.out.println(customers.createCustomer(input.readRequiredText("Documento"),input.readRequiredText("Nombre"),input.readRequiredText("Apellido")));
                    case 2 -> System.out.println(customers.getCustomer(input.readRequiredText("Documento")));
                    case 3 -> customers.listCustomers().forEach(System.out::println);
                    case 0 -> {return;}
                }
            }catch(RuntimeException e){System.out.println("Error: "+e.getMessage());}
        }
    }
}
