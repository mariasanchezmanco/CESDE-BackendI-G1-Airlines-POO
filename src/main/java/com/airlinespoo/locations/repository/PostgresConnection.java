package com.airlinespoo.locations.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/AirlinesPOO";
        String user = "postgresql";
        String password = "123456";

        return DriverManager.getConnection(url, user, password);
    }
}
