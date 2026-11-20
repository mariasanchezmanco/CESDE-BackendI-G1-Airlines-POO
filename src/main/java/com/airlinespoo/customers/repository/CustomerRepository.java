
package com.airlinespoo.customers.repository;

import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.model.DocumentType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {


    private final int MOTOR_BD = 1;

    public Connection getSqlServerConnection() throws SQLException {
        String url =
            "jdbc:sqlserver://localhost:1433;" +
                "databaseName=PooAirlines;" +
                "encrypt=true;" +
                "trustServerCertificate=true";
        String user = "usuarioPooAirlines";
        String password = "PooAirlines123*";

        return DriverManager.getConnection(url, user, password);
    }

    private Connection getConnection() throws SQLException {
        if (MOTOR_BD == 1) {
            return getSqlServerConnection();
        }
        return getSqlServerConnection();
    }

    // OBTENER ID DEL PAIS POR SU NOMBRE
    private short buscarCountryId(Connection conn, String country)
        throws SQLException {

        String sql = "SELECT CountryId FROM dbo.tblCountries WHERE Name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, country);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                        "No existe el pais: " + country
                    );
                }

                short countryId = rs.getShort("CountryId");

                if (rs.next()) {
                    throw new SQLException(
                        "Existe mas de un pais con el nombre: " + country
                    );
                }

                return countryId;
            }
        }
    }

    // CREAR
    @Override
    public void crear(Customer customer) {

        String sql = "INSERT INTO dbo.tblCustomers " +
            "(customerId, FirtName, Lastname, TypeId, TelephoneNumber, Email, CountryId, Address) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {

            short countryId = buscarCountryId(
                conn, customer.getCountry()
            );

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, customer.getId());
                pstmt.setNString(2, customer.getFirstName());
                pstmt.setNString(3, customer.getLastName());
                pstmt.setString(4, customer.getDocumentType().getId());
                pstmt.setString(5, customer.getTelephoneNumber());

                if (customer.getEmail() == null) {
                    pstmt.setNull(6, Types.NVARCHAR);
                } else {
                    pstmt.setNString(6, customer.getEmail());
                }

                pstmt.setShort(7, countryId);

                if (customer.getAddress() == null) {
                    pstmt.setNull(8, Types.NVARCHAR);
                } else {
                    pstmt.setNString(8, customer.getAddress());
                }

                pstmt.executeUpdate();

                System.out.println("Cliente guardado con exito.");
            }

        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
        }
    }

    // BUSCAR POR ID
    @Override
    public Customer buscarPorId(String id) {

        String sql = "SELECT " +
            "c.customerId, c.FirtName, c.Lastname, " +
            "c.TelephoneNumber, c.Email, c.Address, " +
            "dt.TypeId, dt.Description, co.Name AS CountryName " +
            "FROM dbo.tblCustomers c " +
            "INNER JOIN dbo.tblDocumentsType dt ON c.TypeId = dt.TypeId " +
            "INNER JOIN dbo.tblCountries co ON c.CountryId = co.CountryId " +
            "WHERE c.customerId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    DocumentType documentType = new DocumentType(
                        rs.getString("TypeId"),
                        rs.getString("Description")
                    );

                    Customer customer = new Customer(
                        rs.getString("customerId"),
                        rs.getNString("FirtName"),
                        rs.getNString("Lastname"),
                        documentType,
                        rs.getString("TelephoneNumber"),
                        rs.getString("CountryName")
                    );

                    customer.setEmail(rs.getNString("Email"));
                    customer.setAddress(rs.getNString("Address"));

                    return customer;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }

        return null;
    }

    // LISTAR TODOS
    @Override
    public List<Customer> ListarTodos() {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT " +
            "c.customerId, c.FirtName, c.Lastname, " +
            "c.TelephoneNumber, c.Email, c.Address, " +
            "dt.TypeId, dt.Description, co.Name AS CountryName " +
            "FROM dbo.tblCustomers c " +
            "INNER JOIN dbo.tblDocumentsType dt ON c.TypeId = dt.TypeId " +
            "INNER JOIN dbo.tblCountries co ON c.CountryId = co.CountryId " +
            "ORDER BY c.customerId";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                DocumentType documentType = new DocumentType(
                    rs.getString("TypeId"),
                    rs.getString("Description")
                );

                Customer customer = new Customer(
                    rs.getString("customerId"),
                    rs.getNString("FirtName"),
                    rs.getNString("Lastname"),
                    documentType,
                    rs.getString("TelephoneNumber"),
                    rs.getString("CountryName")
                );

                customer.setEmail(rs.getNString("Email"));
                customer.setAddress(rs.getNString("Address"));

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }

        return customers;
    }

    // ACTUALIZAR
    @Override
    public void actualizar(Customer customer) {

        String sql = "UPDATE dbo.tblCustomers SET " +
            "FirtName = ?, Lastname = ?, TypeId = ?, " +
            "TelephoneNumber = ?, Email = ?, CountryId = ?, Address = ? " +
            "WHERE customerId = ?";

        try (Connection conn = getConnection()) {

            short countryId = buscarCountryId(
                conn, customer.getCountry()
            );

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setNString(1, customer.getFirstName());
                pstmt.setNString(2, customer.getLastName());
                pstmt.setString(3, customer.getDocumentType().getId());
                pstmt.setString(4, customer.getTelephoneNumber());

                if (customer.getEmail() == null) {
                    pstmt.setNull(5, Types.NVARCHAR);
                } else {
                    pstmt.setNString(5, customer.getEmail());
                }

                pstmt.setShort(6, countryId);

                if (customer.getAddress() == null) {
                    pstmt.setNull(7, Types.NVARCHAR);
                } else {
                    pstmt.setNString(7, customer.getAddress());
                }

                pstmt.setString(8, customer.getId());

                int filas = pstmt.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cliente actualizado con exito.");
                } else {
                    System.out.println("No se encontro el cliente.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    // ELIMINAR
    @Override
    public void eliminar(String id) {

        String sql = "DELETE FROM dbo.tblCustomers WHERE customerId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Cliente eliminado con exito.");
            } else {
                System.out.println("No se encontro el cliente.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
