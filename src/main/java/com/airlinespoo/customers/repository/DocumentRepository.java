
package com.airlinespoo.customers.repository;

import com.airlinespoo.core.exception.DuplicateException;
import com.airlinespoo.core.exception.NotFoundException;
import com.airlinespoo.core.model.Identifiable;
import com.airlinespoo.customers.model.Customer;
import com.airlinespoo.customers.model.DocumentType;
import com.airlinespoo.customers.repository.IDocumentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentRepository implements IDocumentRepository {

    private final int MOTOR_BD = 1;

        public Connection getSqlServerConnection() throws SQLException {

        String url =
            "jdbc:sqlserver://localhost:1433;" +
                "databaseName=PooAirlines;" +
                "encrypt=true;" +
                "trustServerCertificate=true";

        String user = "usuarioPooAirlines";
        String password = "PooAirlines123*";

        return DriverManager.getConnection(url, user, password);}

        private Connection getConnection() throws SQLException {
        return getSqlServerConnection();
        }

    @Override
    public DocumentType create(DocumentType entity) {

        String sql =
            "INSERT INTO dbo.tblDocumentsType (TypeId, Description) " +
                "VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getId());
            pstmt.setString(2, entity.getDescription());

            pstmt.executeUpdate();

            return entity;

        } catch (SQLException e) {

            // Error 2627: violación de PRIMARY KEY o UNIQUE
            // Error 2601: índice UNIQUE duplicado
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                throw new DuplicateException("tipo de documento", entity.getId());
            }

            throw new RuntimeException(
                "Error al crear tipo de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public Optional<DocumentType> findById(String id) {

        String sql =
            "SELECT TypeId, Description " +
                "FROM dbo.tblDocumentsType WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    DocumentType documentType = new DocumentType(
                        rs.getString("TypeId"),
                        rs.getString("Description")
                    );

                    return Optional.of(documentType);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al buscar tipo de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<DocumentType> findAll() {

        List<DocumentType> documents = new ArrayList<>();

        String sql =
            "SELECT TypeId, Description " +
                "FROM dbo.tblDocumentsType ORDER BY TypeId";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                documents.add(
                    new DocumentType(
                        rs.getString("TypeId"),
                        rs.getString("Description")
                    )
                );
            }

            return documents;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al listar tipos de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public DocumentType update(DocumentType entity) {

        String sql =
            "UPDATE dbo.tblDocumentsType " +
                "SET Description = ? WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getDescription());
            pstmt.setString(2, entity.getId());

            int filas = pstmt.executeUpdate();

            if (filas == 0) {
                throw new NotFoundException(
                    "tipo de documento", entity.getId()
                );
            }

            return entity;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al actualizar tipo de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public boolean deleteById(String id) {

        String sql =
            "DELETE FROM dbo.tblDocumentsType WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int filas = pstmt.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al eliminar tipo de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public boolean existsById(String id) {

        String sql =
            "SELECT COUNT(*) FROM dbo.tblDocumentsType WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al verificar tipo de documento: " + e.getMessage(), e
            );
        }
    }

    @Override
    public long count() {

        String sql =
            "SELECT COUNT(*) FROM dbo.tblDocumentsType";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al contar tipos de documento: " + e.getMessage(), e
            );
        }
    }
}
