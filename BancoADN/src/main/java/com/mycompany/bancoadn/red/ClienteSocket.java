package com.mycompany.bancoadn.red;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {

    private static final String HOST = "localhost";

    private static final int PUERTO = 5000;

    private static Socket socket;

    private static PrintWriter salida;

    private static BufferedReader entrada;

    // =========================
    // CONECTAR
    // =========================

    /**
     *
     * @throws Exception
     */
    public static synchronized void conectar() throws Exception {

        // YA CONECTADO
        if (socket != null
                &&
            socket.isConnected()
                &&
            !socket.isClosed()) {

            return;
        }

        socket = new Socket(HOST, PUERTO);

        salida = new PrintWriter(
                socket.getOutputStream(),
                true
        );

        entrada = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream()
                )
        );
    }

    // =========================
    // ENVIAR
    // =========================
    public static synchronized String enviar(String mensaje) {

        try {

            conectar();

            salida.println(mensaje);

            return entrada.readLine();

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // DESCONECTAR
    // =========================
    public static synchronized void desconectar() {

        try {

            if (socket != null) {
                socket.close();
            }

        } catch (Exception e) {

            System.out.println("Error cerrando socket: " + e.getMessage());
        }
    }
}