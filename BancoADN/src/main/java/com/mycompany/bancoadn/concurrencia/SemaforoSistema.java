package com.mycompany.bancoadn.concurrencia;

import java.util.concurrent.ConcurrentHashMap;

public class SemaforoSistema {

    private static final ConcurrentHashMap<Integer, Boolean> sesiones = new ConcurrentHashMap<>();

    public static boolean conectar(int idUsuario) {
        
        return sesiones.putIfAbsent(idUsuario, true) == null;
    }

    public static void desconectar(int idUsuario) {

        sesiones.remove(idUsuario);
    }

    public static boolean estaConectado(int idUsuario) {

        return sesiones.containsKey(idUsuario);
    }

    public static int cantidadSesiones() {

        return sesiones.size();
    }
}