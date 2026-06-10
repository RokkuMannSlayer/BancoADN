package com.mycompany.bancoadn.red;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {

    private static String HOST = "localhost"; //A futuro cambiamos la IP para el acceso a otras computadoras al servidor. -Rokku
    private static final int PUERTO = 5000;

    private static Socket socket;
    private static PrintWriter salida;
    private static BufferedReader entrada;

    public static void setHost(String host) {
        HOST = host;
    }

    public static synchronized void conectar() throws Exception {

        if (socket != null && socket.isConnected() && !socket.isClosed()) {

            return;
        }

        socket = new Socket(HOST, PUERTO);

        salida = new PrintWriter(socket.getOutputStream(), true);

        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static synchronized String enviar(String mensaje) {

        try {

            conectar();

            salida.println(mensaje);

            String primeraLinea = entrada.readLine();

            if (primeraLinea == null) {

                return "ERROR,Servidor desconectado";
            }

            StringBuilder respuesta = new StringBuilder(primeraLinea);

            while (entrada.ready()) {

                String linea = entrada.readLine();

                if (linea != null) {

                    respuesta.append("\n").append(linea);
                }
            }

            return respuesta.toString();

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

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