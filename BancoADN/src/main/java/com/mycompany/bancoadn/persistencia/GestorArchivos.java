package com.mycompany.bancoadn.persistencia;

import com.mycompany.bancoadn.modelos.Usuario;
import com.mycompany.bancoadn.modelos.PerfilGenetico;
import com.mycompany.bancoadn.modelos.LogSistema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private static final String ARCHIVO_PERFILES = "perfiles.dat";
    private static final String ARCHIVO_LOGS = "logs.dat";

    // =========================
    // ADMIN PRINCIPAL
    // =========================
    static {
        inicializarAdmin();
    }

    private static void inicializarAdmin() {

        List<Usuario> usuarios = leerUsuarios();

        boolean existeAdmin = false;

        for (Usuario u : usuarios) {

            if (u.getRol().equals("ADMIN")) {
                existeAdmin = true;
                break;
            }
        }

        if (!existeAdmin) {

            Usuario admin = new Usuario(1, "Admin Principal", "admin@banco.com", "1234", "00000000", "ADMIN");

            usuarios.add(admin);

            escribirArchivo(ARCHIVO_USUARIOS, usuarios);

            guardarLog(new LogSistema( "ADMIN_DEFAULT", admin.getEmail(), "Admin principal creado"));
        }
    }

    // =========================
    // USUARIOS
    // =========================
    public static synchronized void guardarUsuario(Usuario usuario) {

        List<Usuario> usuarios = leerUsuarios();

        usuarios.add(usuario);

        escribirArchivo(ARCHIVO_USUARIOS, usuarios);
    }

    public static synchronized List<Usuario> leerUsuarios() {

        Object obj = leerArchivo(ARCHIVO_USUARIOS);

        if (obj == null) {
            return new ArrayList<>();
        }

        return (List<Usuario>) obj;
    }

    // =========================
    // PERFILES
    // =========================
    public static synchronized void guardarPerfil(PerfilGenetico perfil) {

        List<PerfilGenetico> perfiles = leerPerfiles();

        perfiles.add(perfil);

        escribirArchivo(ARCHIVO_PERFILES, perfiles);

        guardarLog(new LogSistema("REGISTRO_PERFIL", String.valueOf(perfil.getIdCliente()), "Perfil registrado" ));
    }

    public static synchronized List<PerfilGenetico> leerPerfiles() {

        return (List<PerfilGenetico>) leerArchivo(ARCHIVO_PERFILES);
    }

    public static synchronized void actualizarPerfiles(List<PerfilGenetico> perfiles) {

        escribirArchivo(ARCHIVO_PERFILES, perfiles);

        guardarLog(new LogSistema("ACTUALIZAR_PERFILES", "SISTEMA", "Perfiles actualizados"));
    }

    // =========================
    // LOGS
    // =========================
    public static synchronized void guardarLog(LogSistema log) {

        List<LogSistema> logs = leerLogs();

        logs.add(log);

        escribirArchivo(ARCHIVO_LOGS, logs);
    }

    public static synchronized List<LogSistema> leerLogs() {

        return (List<LogSistema>) leerArchivo(ARCHIVO_LOGS);
    }

    // =========================
    // GENERALES
    // =========================
    private static synchronized void escribirArchivo(String archivo, Object objeto) {

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {

                oos.writeObject(objeto);

            } catch (Exception e) {

                System.out.println( "Error escribiendo archivo: " + e.getMessage());
            }
        }

        private static synchronized Object leerArchivo(String archivo) {

        File file = new File(archivo);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream( new FileInputStream(file))) {

            return ois.readObject();

        } catch (EOFException e) {

            return new ArrayList<>();

        } catch (Exception e) {

            System.out.println("Error leyendo archivo: " + e.getMessage());

            return new ArrayList<>();
        }
    }
}