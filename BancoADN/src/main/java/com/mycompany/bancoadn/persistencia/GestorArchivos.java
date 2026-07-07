package com.mycompany.bancoadn.persistencia;

import com.mycompany.bancoadn.concurrencia.IdSemaforos;
import com.mycompany.bancoadn.concurrencia.SemaforoSistema;
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

        SemaforoSistema.adquirir(IdSemaforos.USUARIOS);

        try {

            List<Usuario> usuarios = leerUsuariosInterno();

            boolean existeAdmin = false;

            for (Usuario u : usuarios) {

                if ("ADMIN".equals(u.getRol())) {

                    existeAdmin = true;
                    break;
                }
            }

            if (!existeAdmin) {

                Usuario admin = new Usuario(1, "Admin Principal", "admin@banco.com", "1234", "00000000", "ADMIN");

                usuarios.add(admin);

                escribirArchivo(ARCHIVO_USUARIOS, usuarios);
            }

        } finally {

        SemaforoSistema.liberar(IdSemaforos.USUARIOS);
        }

        // El log se guarda fuera del semáforo de usuarios
        // para no mezclar recursos distintos.
        guardarLog(new LogSistema("ADMIN_DEFAULT", "admin@banco.com", "Admin principal creado"));
    }

    // =========================
    // USUARIOS
    // =========================
    public static void guardarUsuario(Usuario usuario) {

        try {

            SemaforoSistema.adquirir(IdSemaforos.USUARIOS);

            List<Usuario> usuarios = leerUsuariosInterno();

            usuarios.add(usuario);

            escribirArchivo(ARCHIVO_USUARIOS, usuarios);

        } finally {

            SemaforoSistema.liberar(IdSemaforos.USUARIOS);
        }

        guardarLog(new LogSistema("REGISTRO_USUARIO", usuario.getEmail(), "Usuario registrado"));
    }

    public static List<Usuario> leerUsuarios() {

        try {

            SemaforoSistema.adquirir(IdSemaforos.USUARIOS);

            return leerUsuariosInterno();

        } finally {

            SemaforoSistema.liberar(IdSemaforos.USUARIOS);
        }
    }
    
    private static List<Usuario> leerUsuariosInterno() {
        
        Object obj = leerArchivo(ARCHIVO_USUARIOS);

        if (obj == null) {
            return new ArrayList<>();
        }

        return (List<Usuario>) obj;
    }

    // =========================
    // PERFILES
    // =========================
    public static void guardarPerfil(PerfilGenetico perfil) {

        try {

            SemaforoSistema.adquirir(IdSemaforos.PERFILES);

            List<PerfilGenetico> perfiles = leerPerfilesInterno();

            perfiles.add(perfil);

            escribirArchivo(ARCHIVO_PERFILES, perfiles);

        } finally {

            SemaforoSistema.liberar(IdSemaforos.PERFILES);
        }

        guardarLog(new LogSistema("REGISTRO_PERFIL", String.valueOf(perfil.getIdCliente()), "Perfil registrado"));
    }

    public static List<PerfilGenetico> leerPerfiles() {

        try {

            SemaforoSistema.adquirir(IdSemaforos.PERFILES);

            return (List<PerfilGenetico>) leerArchivo(ARCHIVO_PERFILES);

        } finally {

            SemaforoSistema.liberar(IdSemaforos.PERFILES);
        }
    }

    public static void actualizarPerfiles(List<PerfilGenetico> perfiles) {

        try {

            SemaforoSistema.adquirir(IdSemaforos.PERFILES);

            escribirArchivo(ARCHIVO_PERFILES, perfiles);

            guardarLog(new LogSistema("ACTUALIZAR_PERFILES", "SISTEMA", "Perfiles actualizados"));

        } finally {

            SemaforoSistema.liberar(IdSemaforos.PERFILES);
        }
    }
    
    private static List<PerfilGenetico> leerPerfilesInterno() {
        
        return (List<PerfilGenetico>) leerArchivo(ARCHIVO_PERFILES);
    }

    // =========================
    // LOGS
    // =========================
    public static void guardarLog(LogSistema log) {

        try {

            SemaforoSistema.adquirir(IdSemaforos.LOGS);

            List<LogSistema> logs = leerLogsInterno();

            logs.add(log);

            escribirArchivo(ARCHIVO_LOGS, logs);

        } finally {

            SemaforoSistema.liberar(IdSemaforos.LOGS);
        }
    }

    public static List<LogSistema> leerLogs() {

        try {

            SemaforoSistema.adquirir(IdSemaforos.LOGS);

            return (List<LogSistema>) leerArchivo(ARCHIVO_LOGS);

        } finally {

            SemaforoSistema.liberar(IdSemaforos.LOGS);
        }
    }

    // =========================
    // GENERALES
    // =========================
    private static void escribirArchivo(String archivo, Object objeto) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {

            oos.writeObject(objeto);

        } catch (Exception e) {

            System.out.println( "Error escribiendo archivo: " + e.getMessage());
        }
    }

    private static Object leerArchivo(String archivo) {

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
        
    private static List<LogSistema> leerLogsInterno() {
        return (List<LogSistema>) leerArchivo(ARCHIVO_LOGS);
    }
}