package com.mycompany.bancoadn;

import java.sql.*;

public class LoginRemoto {

    public static String login(String usuario, String password) {

        // 🔹 Validación básica
        if (usuario == null || password == null ||
            usuario.trim().isEmpty() || password.trim().isEmpty()) {
            return "ERROR,Complete usuario y contraseña";
        }

        String sql = "{CALL LoginUsuario(?, ?)}";

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, usuario);
            cs.setString(2, password);

            try (ResultSet rs = cs.executeQuery()) {

                if (rs.next()) {

                    String tipo = rs.getString("tipo");

                    if ("CLIENTE".equals(tipo) || "ADMIN".equals(tipo)) {

                        int id = rs.getInt("id");
                        return "OK," + tipo + "," + id;

                    } else {
                        return "ERROR,Credenciales incorrectas";
                    }
                }
            }

        } catch (SQLException e) {
            return "ERROR,SQL: " + e.getMessage();
        }

        return "ERROR,Credenciales incorrectas";
    }
}