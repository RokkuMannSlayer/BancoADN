package com.mycompany.bancoadn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BancoADN {

    // 🔹 REGISTRAR CLIENTE
    public String registrarCliente(String nombre, String email, String password, String dni) {
        String sql = "{CALL RegistrarCliente(?, ?, ?, ?)}";

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, nombre);
            cs.setString(2, email);
            cs.setString(3, password);
            cs.setString(4, dni);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mensaje");
                }
            }

        } catch (SQLException e) {
            return "Error SQL: " + e.getMessage();
        }

        return "Error inesperado";
    }

    // 🔹 EDITAR PERFIL GENÉTICO
    public String editarPerfilGenetico(int idPerfil, String descripcion, String estado, int idAdmin) {
        String sql = "{CALL EditarPerfilGenetico(?, ?, ?, ?)}";

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPerfil);
            cs.setString(2, descripcion);
            cs.setString(3, estado);
            cs.setInt(4, idAdmin);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mensaje");
                }
            }

        } catch (SQLException e) {
            return "Error SQL: " + e.getMessage();
        }

        return "Error inesperado";
    }

    // 🔹 REGISTRAR PERFIL
    public String registrarPerfil(int idCliente, String descripcion) {
        String sql = "{CALL RegistrarPerfil(?, ?, ?)}";

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, descripcion);
            cs.setInt(2, idCliente);
            cs.setInt(3, 1); // admin por defecto

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mensaje");
                }
            }

        } catch (SQLException e) {
            return "Error SQL: " + e.getMessage();
        }

        return "Error inesperado";
    }

    public String consultarPerfilCliente(int idCliente) {

        StringBuilder resultado = new StringBuilder();

        try (Connection con = ConexionBD.conectar();
            CallableStatement cs = con.prepareCall("{CALL ConsultarPerfilCliente(?)}")) {

            cs.setInt(1, idCliente);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                resultado.append("ID Perfil: ").append(rs.getInt(1))
                    .append(" | Descripción: ").append(rs.getString(2))
                    .append(" | Estado: ").append(rs.getString(3))
                    .append(" | Cliente: ").append(rs.getString(4))
                    .append(" | DNI: ").append(rs.getString(5))
                    .append(" | Admin: ").append(rs.getString(6));
            } else {
                return "No tiene perfil";
            }

        } catch (Exception e) {
            return "ERROR," + e.getMessage();
        }

        return resultado.toString();
    }

    // 🔹 LISTAR PERFILES (CORREGIDO)
    public List<String> listarPerfiles() {
        String sql = "{CALL ListarPerfiles()}";
        List<String> lista = new ArrayList<>();

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                String perfil = "ID: " + rs.getInt("IDperfil") +
                        " | Cliente: " + rs.getString("Nombre_cliente") +
                        " | DNI: " + rs.getString("DNI_cliente") +
                        " | Estado: " + rs.getString("Estado");

                lista.add(perfil);
            }

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 CONSULTAR TODOS LOS PERFILES (CORREGIDO)
    public List<String> consultarTodosPerfiles() {
        String sql = "{CALL ConsultarTodosPerfiles()}";
        List<String> lista = new ArrayList<>();

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                String perfil = "ID: " + rs.getInt("IDperfil") +
                        " | Cliente: " + rs.getString("Nombre_cliente") +
                        " | DNI: " + rs.getString("DNI_cliente") +
                        " | Estado: " + rs.getString("Estado");

                lista.add(perfil);
            }

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 ELIMINAR PERFIL
    public String eliminarPerfil(int idPerfil) {
        String sql = "{CALL EliminarPerfil(?)}";

        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPerfil);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("mensaje");
                }
            }

        } catch (SQLException e) {
            return "Error SQL: " + e.getMessage();
        }

        return "Error inesperado";
    }
}