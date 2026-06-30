package com.mycompany.bancoadn.concurrencia;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class SemaforoSistema {

    private static final ConcurrentHashMap<Integer, Semaphore> semaforos = new ConcurrentHashMap<>();

    static {

        semaforos.put(IdSemaforos.USUARIOS, new Semaphore(1, true));

        semaforos.put(IdSemaforos.PERFILES,  new Semaphore(1, true));

        semaforos.put(IdSemaforos.LOGS, new Semaphore(1, true));

        semaforos.put(IdSemaforos.SESIONES, new Semaphore(1, true));

        semaforos.put(IdSemaforos.SOCKET_CLIENTE, new Semaphore(1, true));

        semaforos.put(IdSemaforos.USUARIOS_CONECTADOS, new Semaphore(1, true));
    }

    public static void adquirir(int id) {

        Semaphore s = semaforos.get(id);

        if (s == null) {
            throw new IllegalArgumentException("No existe el semáforo ID = " + id);
        }

        try {

            s.acquire();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    public static void liberar(int id) {

        Semaphore s = semaforos.get(id);

        if (s != null) {
            s.release();
        }
    }
}