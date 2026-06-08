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
    public static synchronized void conectar() throws Exception {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return;
        }
        socket = new Socket(HOST, PUERTO);
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    // =========================
    // ENVIAR (Híbrido No Bloqueante)
    // =========================
    public static synchronized String enviar(String mensaje) {
        try {
            conectar();

            // Enviamos el comando al servidor
            salida.println(mensaje);

            // Leemos la primera línea (Obligatoria para cualquier respuesta: Login, Consultas, Listar)
            String primeraLinea = entrada.readLine();
            if (primeraLinea == null) {
                return "ERROR, Servidor cerró la conexión de forma inesperada.";
            }

            // Usamos un StringBuilder por si hay múltiples líneas (como en el Listar)
            StringBuilder respuestaCompleta = new StringBuilder(primeraLinea);

            // REVISIÓN CLAVE: Miramos si quedan más líneas pendientes en el buffer de red 
            // sin quedarnos bloqueados esperando (ready() devuelve true si hay bytes listos)
            while (entrada.ready()) {
                String siguienteLinea = entrada.readLine();
                if (siguienteLinea != null) {
                    respuestaCompleta.append("\n").append(siguienteLinea);
                }
            }

            return respuestaCompleta.toString().trim();

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