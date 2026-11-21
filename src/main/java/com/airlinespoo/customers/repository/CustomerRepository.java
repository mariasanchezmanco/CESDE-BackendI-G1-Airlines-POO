
package com.airlinespoo.customers.repository;

import com.airlinespoo.core.exception.DuplicateException;
import com.airlinespoo.core.exception.NotFoundException;
import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.model.DocumentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // Busca el ID del país a partir de su nombre.
    private short buscarCountryId(Connection conn, String country)
        throws SQLException {

        String sql =
            "SELECT CountryId FROM dbo.tblCountries WHERE Name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, country);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No existe el país: " + country);
                }

                short countryId = rs.getShort("CountryId");

                if (rs.next()) {
                    throw new SQLException(
                        "Existe más de un país con el nombre: " + country
                    );
                }

                return countryId;
            }
        }
    }

    // Convierte una fila de la consulta en un objeto Customer.
    private Customer mapCustomer(ResultSet rs) throws SQLException {

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

    // Consulta base para obtener los datos completos del cliente.
    private String getCustomerSelect() {
        return "SELECT " +
            "c.customerId, c.FirtName, c.Lastname, " +
            "c.TelephoneNumber, c.Email, c.Address, " +
            "dt.TypeId, dt.Description, co.Name AS CountryName " +
            "FROM dbo.tblCustomers c " +
            "INNER JOIN dbo.tblDocumentsType dt ON c.TypeId = dt.TypeId " +
            "INNER JOIN dbo.tblCountries co ON c.CountryId = co.CountryId ";
    }

    // CREAR
    @Override
    public Customer create(Customer entity) {

        String sql = "INSERT INTO dbo.tblCustomers " +
            "(customerId, FirtName, Lastname, TypeId, TelephoneNumber, Email, CountryId, Address) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection()) {

            short countryId = buscarCountryId(conn, entity.getCountry());

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, entity.getId());
                pstmt.setNString(2, entity.getFirstName());
                pstmt.setNString(3, entity.getLastName());
                pstmt.setString(4, entity.getDocumentType().getId());
                pstmt.setString(5, entity.getTelephoneNumber());

                if (entity.getEmail() == null) {
                    pstmt.setNull(6, Types.NVARCHAR);
                } else {
                    pstmt.setNString(6, entity.getEmail());
                }

                pstmt.setShort(7, countryId);

                if (entity.getAddress() == null) {
                    pstmt.setNull(8, Types.NVARCHAR);
                } else {
                    pstmt.setNString(8, entity.getAddress());
                }

                pstmt.executeUpdate();
                return entity;
            }

        } catch (SQLException e) {

            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new DuplicateException("cliente", entity.getId());
            }

            throw new RuntimeException(
                "Error al crear cliente: " + e.getMessage(), e
            );
        }
    }

    // BUSCAR POR ID
    @Override
    public Optional<Customer> findById(String id) {

        String sql = getCustomerSelect() + "WHERE c.customerId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al buscar cliente: " + e.getMessage(), e
            );
        }
    }

    // LISTAR TODOS
    @Override
    public List<Customer> findAll() {

        List<Customer> customers = new ArrayList<>();

        String sql = getCustomerSelect() +
            "ORDER BY c.customerId";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapCustomer(rs));
            }

            return customers;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al listar clientes: " + e.getMessage(), e
            );
        }
    }

    // ACTUALIZAR
    @Override
    public Customer update(Customer entity) {

        String sql = "UPDATE dbo.tblCustomers SET " +
            "FirtName = ?, Lastname = ?, TypeId = ?, " +
            "TelephoneNumber = ?, Email = ?, CountryId = ?, Address = ? " +
            "WHERE customerId = ?";

        try (Connection conn = getConnection()) {

            short countryId = buscarCountryId(conn, entity.getCountry());

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setNString(1, entity.getFirstName());
                pstmt.setNString(2, entity.getLastName());
                pstmt.setString(3, entity.getDocumentType().getId());
                pstmt.setString(4, entity.getTelephoneNumber());

                if (entity.getEmail() == null) {
                    pstmt.setNull(5, Types.NVARCHAR);
                } else {
                    pstmt.setNString(5, entity.getEmail());
                }

                pstmt.setShort(6, countryId);

                if (entity.getAddress() == null) {
                    pstmt.setNull(7, Types.NVARCHAR);
                } else {
                    pstmt.setNString(7, entity.getAddress());
                }

                pstmt.setString(8, entity.getId());

                int filas = pstmt.executeUpdate();

                if (filas == 0) {
                    throw new NotFoundException("cliente", entity.getId());
                }

                return entity;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al actualizar cliente: " + e.getMessage(), e
            );
        }
    }

    // ELIMINAR POR ID
    @Override
    public boolean deleteById(String id) {

        String sql =
            "DELETE FROM dbo.tblCustomers WHERE customerId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al eliminar cliente: " + e.getMessage(), e
            );
        }
    }

    // VERIFICAR EXISTENCIA POR ID
    @Override
    public boolean existsById(String id) {

        String sql =
            "SELECT COUNT(*) FROM dbo.tblCustomers WHERE customerId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al verificar cliente: " + e.getMessage(), e
            );
        }
    }

    // CONTAR CLIENTES
    @Override
    public long count() {

        String sql = "SELECT COUNT(*) FROM dbo.tblCustomers";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getLong(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al contar clientes: " + e.getMessage(), e
            );
        }
    }

    // BUSCAR POR CORREO
    @Override
    public Optional<Customer> findByEmail(String email) {

        String sql = getCustomerSelect() + "WHERE c.Email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al buscar cliente por correo: " + e.getMessage(), e
            );
        }
    }

    // VERIFICAR SI EXISTE UN CORREO
    @Override
    public boolean existsByEmail(String email) {

        String sql =
            "SELECT COUNT(*) FROM dbo.tblCustomers WHERE Email = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al verificar el correo: " + e.getMessage(), e
            );
        }
    }

    // BUSCAR CLIENTES POR TIPO DE DOCUMENTO
    @Override
    public List<Customer> findByDocumentType(String typeId) {

        List<Customer> customers = new ArrayList<>();

        String sql = getCustomerSelect() +
            "WHERE c.TypeId = ? " +
            "ORDER BY c.Lastname, c.FirtName";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, typeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapCustomer(rs));
                }
            }

            return customers;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al listar clientes por documento: " + e.getMessage(), e
            );
        }
    }

    // VERIFICAR SI UN TIPO DE DOCUMENTO ESTÁ ASIGNADO A ALGÚN CLIENTE
    @Override
    public boolean existsByDocumentType(String typeId) {

        String sql =
            "SELECT COUNT(*) FROM dbo.tblCustomers WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, typeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al verificar el tipo de documento: " + e.getMessage(), e
            );
        }
    }
}
