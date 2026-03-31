package com.mycompany.bancoadn;

import java.util.HashMap;
import java.util.Map;

public class BancoADN {

    private Map<String, PerfilADN> perfiles = new HashMap<>();

    // Registrar perfil
    public boolean registrarPerfil(String id, String nombre, String descripcion) {
        if (perfiles.containsKey(id)) {
            System.out.println("Error: ID ya existe.");
            return false;
        }

        PerfilADN nuevo = new PerfilADN(id, nombre, descripcion);
        perfiles.put(id, nuevo);
        return true;
    }

    // Consultar perfil por ID
    public PerfilADN consultarPerfil(String id) {
        return perfiles.get(id);
    }

    // Listar perfiles
    public void listarPerfiles() {
        for (PerfilADN perfil : perfiles.values()) {
            System.out.println(perfil);
        }
    }

    // Eliminar perfil (baja lógica)
    public boolean eliminarPerfil(String id) {
        PerfilADN perfil = perfiles.get(id);
        if (perfil != null && perfil.isActivo()) {
            perfil.eliminar();
            return true;
        }
        return false;
    }

    // MAIN de prueba
    public static void main(String[] args) {
        BancoADN banco = new BancoADN();

        banco.registrarPerfil("001", "Juan", "Perfil genético A");
        banco.registrarPerfil("002", "Ana", "Perfil genético B");

        System.out.println("\n--- LISTA ---");
        banco.listarPerfiles();

        System.out.println("\n--- CONSULTA ---");
        System.out.println(banco.consultarPerfil("001"));

        System.out.println("\n--- ELIMINAR ---");
        banco.eliminarPerfil("001");

        System.out.println("\n--- LISTA FINAL ---");
        banco.listarPerfiles();
    }
}
