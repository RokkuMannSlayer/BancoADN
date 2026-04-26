package com.mycompany.bancoadn;

import java.sql.*;

public class LoginRemoto {

    public static String login(String usuario, String password) {

        try {
            Connection con = ConexionBD.conectar();

            CallableStatement cs = con.prepareCall("{CALL LoginUsuario(?, ?)}");
            cs.setString(1, usuario);
            cs.setString(2, password);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");

                // 🔥 VALIDACIÓN CLAVE
                if (tipo.equals("CLIENTE") || tipo.equals("ADMIN")) {

                    int id = rs.getInt("id");
                    return "OK," + tipo + "," + id;

                } else {
                    return "ERROR,Credenciales incorrectas";
                }
            }

        } catch (Exception e) {
            return "ERROR," + e.getMessage();
        }

        return "ERROR,Credenciales incorrectas";
    }
}