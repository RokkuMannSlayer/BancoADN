package com.mycompany.bancoadn;

import java.util.HashMap;
import java.util.Map;

public class BancoADN {

    private Map<String, PerfilADN> perfiles;

    public BancoADN() {
        perfiles = new HashMap<>();
    }

    public boolean registrarPerfil(String id, String nombre, String descripcion) {

        if (id == null || id.isEmpty()) {
            System.out.println("Error: ID inválido");
            return false;
        }

        if (perfiles.containsKey(id)) {
            System.out.println("Error: ID ya existe");
            return false;
        }

        PerfilADN nuevo = new PerfilADN(id, nombre, descripcion);
        perfiles.put(id, nuevo);

        return true;
    }

    public PerfilADN consultarPerfil(String id) {
        return perfiles.get(id);
    }

    public void listarPerfiles() {
        if (perfiles.isEmpty()) {
            System.out.println("No hay perfiles registrados.");
            return;
        }

        for (PerfilADN perfil : perfiles.values()) {
            System.out.println(perfil);
        }
    }

    public boolean eliminarPerfil(String id) {
        PerfilADN perfil = perfiles.get(id);

        if (perfil != null && perfil.isActivo()) {
            perfil.eliminar();
            return true;
        }

        return false;
    }
    public Map<String, PerfilADN> getPerfiles() {
        return perfiles;
    }
    public void listarActivos() {
        for (PerfilADN perfil : perfiles.values()) {
            if (perfil.isActivo()) {
                System.out.println(perfil);
            }
        }
    }

    public static void main(String[] args) {
        
        new Sign().setVisible(true);

        BancoADN banco = new BancoADN();

        banco.registrarPerfil("001", "Juan", "Perfil A");
        banco.registrarPerfil("002", "Ana", "Perfil B");

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
