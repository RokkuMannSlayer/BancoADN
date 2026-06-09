package com.mycompany.bancoadn.concurrencia;

import java.util.concurrent.ConcurrentHashMap;

public class SemaforoUsuarios {

    private static final ConcurrentHashMap<String, Boolean> usuariosActivos = new ConcurrentHashMap<>();

    // =========================
    // ENTRAR
    // =========================
    public static boolean entrar(String email) {

        boolean resultado = usuariosActivos.putIfAbsent(email, true) == null;

        System.out.println("[SEMAFORO] LOGIN -> " + email + " | permitido=" + resultado);

        return resultado;
    }

    // =========================
    // SALIR
    // =========================
    public static void salir(String email) {

        if (email == null) {
            return;
        }

        usuariosActivos.remove(email);

        System.out.println("[SEMAFORO] LOGOUT -> " + email);
    }

    // =========================
    // CONSULTAR
    // =========================
    public static boolean estaConectado(String email) {

        return usuariosActivos.containsKey(email);
    }

    // =========================
    // CANTIDAD
    // =========================
    public static int cantidadUsuarios() {

        return usuariosActivos.size();
    }

    // =========================
    // LISTAR
    // =========================
    public static String listarUsuarios() {

        StringBuilder sb = new StringBuilder();

        for (String usuario : usuariosActivos.keySet()) {

            sb.append(usuario).append("\n");
        }

        return sb.toString();
    }
}