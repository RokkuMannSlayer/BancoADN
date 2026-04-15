package com.mycompany.bancoadn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Bancoadn";

    private static final String USER = "root";
    private static final String PASS = "Root1818";

    private static Connection conexion = null;
    
    
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {

                // Driver oficial de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                conexion = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        return conexion;
    }

    /**
     * Cierra la conexión si está abierta.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
}
