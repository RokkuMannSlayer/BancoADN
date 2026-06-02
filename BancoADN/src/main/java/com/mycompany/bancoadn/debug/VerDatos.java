package com.mycompany.bancoadn.debug;

import com.mycompany.bancoadn.modelos.Usuario;
import com.mycompany.bancoadn.modelos.PerfilGenetico;
import com.mycompany.bancoadn.modelos.LogSistema;
import com.mycompany.bancoadn.persistencia.GestorArchivos;

import java.util.List;

public class VerDatos {

    public static void main(String[] args) {

        mostrarUsuarios();
        mostrarPerfiles();
        mostrarLogs();
    }

    // =========================
    // USUARIOS
    // =========================
    public static void mostrarUsuarios() {

        System.out.println("\n===== USUARIOS =====");

        List<Usuario> usuarios =
                GestorArchivos.leerUsuarios();

        if (usuarios.isEmpty()) {

            System.out.println("Sin usuarios");
            return;
        }

        for (Usuario u : usuarios) {

            System.out.println(
                    "ID: " + u.getIdUsuario()
                    + " | Nombre: " + u.getNombre()
                    + " | Email: " + u.getEmail()
                    + " | DNI: " + u.getDni()
                    + " | Rol: " + u.getRol()
            );
        }
    }

    // =========================
    // PERFILES
    // =========================
    public static void mostrarPerfiles() {

        System.out.println("\n===== PERFILES =====");

        List<PerfilGenetico> perfiles =
                GestorArchivos.leerPerfiles();

        if (perfiles.isEmpty()) {

            System.out.println("Sin perfiles");
            return;
        }

        for (PerfilGenetico p : perfiles) {

            System.out.println(
                    "ID Perfil: " + p.getIdPerfil()
                    + " | Foto: " + p.getFotoPerfil()
                    + " | Tipo Sangre: " + p.getTipoSangre()
                    + " | Ojos: " + p.getColorOjos()
                    + " | Pelo: " + p.getColorPelo()
                    + " | Conducta: " + p.getTendenciaConductual()
                    + " | Altura: " + p.getAltura()
                    + " | Peso: " + p.getPeso()
                    + " | IMC: " + String.format("%.2f", p.getImc())
                    + " | Estado: " + p.getEstado()
                    + " | ID Cliente: " + p.getIdCliente()
                    + " | ID Admin: " + p.getIdAdmin()
            );
        }
    }

    // =========================
    // LOGS
    // =========================
    public static void mostrarLogs() {

        System.out.println("\n===== LOGS =====");

        List<LogSistema> logs =
                GestorArchivos.leerLogs();

        if (logs.isEmpty()) {

            System.out.println("Sin logs");
            return;
        }

        for (LogSistema l : logs) {

            System.out.println(
                    "Fecha: " + l.getFecha()
                    + " | Acción: " + l.getAccion()
                    + " | Usuario: " + l.getUsuario()
                    + " | Descripción: " + l.getDescripcion()
            );
        }
    }
}