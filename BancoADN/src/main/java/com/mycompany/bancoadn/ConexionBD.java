package com.mycompany.bancoadn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/banco_adn";
    private static final String USER = "root"; // cambiar si usás otro usuario
    private static final String PASSWORD = "Root1818"; // contraseña de Trigal: kaminoki

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}