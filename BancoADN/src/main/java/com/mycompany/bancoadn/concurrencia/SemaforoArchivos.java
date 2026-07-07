package com.mycompany.bancoadn.concurrencia;

import java.util.concurrent.Semaphore;

public class SemaforoArchivos {

    private static final Semaphore semaforo = new Semaphore(1, true);

    public static void entrar() throws InterruptedException {

        semaforo.acquire();
    }

    public static void salir() {

        semaforo.release();
    }
}