
package com.airlinespoo.customers.repository;

import com.airlinespoo.customers.model.DocumentType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

        return DriverManager.getConnection(url, user, password);
    }

    private Connection getConnection() throws SQLException {
        if (MOTOR_BD == 1) {
            return getSqlServerConnection();
        }
        return getSqlServerConnection();
    }

    // CREAR
    @Override
    public void crear(DocumentType documentType) {

        String sql = "INSERT INTO dbo.tblDocumentsType (TypeId, Description) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, documentType.getId());
            pstmt.setString(2, documentType.getDescription());

            pstmt.executeUpdate();

            System.out.println("Tipo de documento guardado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al crear tipo de documento: " + e.getMessage());
        }
    }

    // BUSCAR POR ID
    @Override
    public DocumentType buscarPorId(String id) {

        String sql = "SELECT TypeId, Description FROM dbo.tblDocumentsType WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return new DocumentType(
                        rs.getString("TypeId"),
                        rs.getString("Description")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar tipo de documento: " + e.getMessage());
        }

        return null;
    }

    // LISTAR TODOS
    @Override
    public List<DocumentType> ListarTodos() {

        List<DocumentType> documentTypes = new ArrayList<>();

        String sql = "SELECT TypeId, Description FROM dbo.tblDocumentsType ORDER BY TypeId";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                documentTypes.add(
                    new DocumentType(
                        rs.getString("TypeId"),
                        rs.getString("Description")
                    )
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al listar tipos de documento: " + e.getMessage());
        }

        return documentTypes;
    }

    // ACTUALIZAR
    @Override
    public void actualizar(DocumentType documentType) {

        String sql = "UPDATE dbo.tblDocumentsType SET Description = ? WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, documentType.getDescription());
            pstmt.setString(2, documentType.getId());

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Tipo de documento actualizado con éxito.");
            } else {
                System.out.println("No se encontró el tipo de documento.");
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar tipo de documento: " + e.getMessage());
        }
    }

    // ELIMINAR
    @Override
    public void eliminar(String id) {

        String sql = "DELETE FROM dbo.tblDocumentsType WHERE TypeId = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                System.out.println("Tipo de documento eliminado con éxito.");
            } else {
                System.out.println("No se encontró el tipo de documento.");
            }

        } catch (SQLException e) {
            System.err.println("Error al eliminar tipo de documento: " + e.getMessage());
        }
    }
}
