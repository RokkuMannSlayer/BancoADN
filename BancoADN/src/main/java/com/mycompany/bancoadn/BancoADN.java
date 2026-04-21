package com.mycompany.bancoadn;

import java.sql.*;

public class BancoADN {

    // 🔹 REGISTRAR PERFIL (CLIENTE)
    public String registrarPerfil(int idCliente, String descripcion) {
        try {
            Connection con = ConexionBD.conectar();

            CallableStatement cs = con.prepareCall("{CALL RegistrarPerfil(?, ?, ?)}");
            cs.setString(1, descripcion);
            cs.setInt(2, idCliente);
            cs.setInt(3, 1);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getString("mensaje");
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "Error";
    }

    // 🔹 CONSULTAR PERFIL CLIENTE
    public String consultarPerfilCliente(int idCliente) {
        StringBuilder resultado = new StringBuilder();

        try {
            Connection con = ConexionBD.conectar();

            CallableStatement cs = con.prepareCall("{CALL ConsultarPerfilCliente(?)}");
            cs.setInt(1, idCliente);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                resultado.append("ID Perfil: ").append(rs.getInt("IDperfil"))
                        .append("\nDescripción: ").append(rs.getString("Descripcion"))
                        .append("\nEstado: ").append(rs.getString("Estado"))
                        .append("\nCliente: ").append(rs.getString("Nombre_cliente"))
                        .append("\nDNI: ").append(rs.getString("DNI_cliente"));
            } else {
                return "No tiene perfil";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return resultado.toString();
    }

    // 🔹 LISTAR PERFILES (ADMIN)
    public ResultSet listarPerfiles() {
        try {
            Connection con = ConexionBD.conectar();
            CallableStatement cs = con.prepareCall("{CALL ListarPerfiles()}");
            return cs.executeQuery();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // 🔹 CONSULTAR TODOS LOS PERFILES (ADMIN)
    public ResultSet consultarTodosPerfiles() {
        try {
            Connection con = ConexionBD.conectar();
            CallableStatement cs = con.prepareCall("{CALL ConsultarTodosPerfiles()}");
            return cs.executeQuery();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // 🔹 ELIMINAR PERFIL
    public String eliminarPerfil(int idPerfil) {
        try {
            Connection con = ConexionBD.conectar();

            CallableStatement cs = con.prepareCall("{CALL EliminarPerfil(?)}");
            cs.setInt(1, idPerfil);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return rs.getString("mensaje");
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

        return "Error";
    }
}
