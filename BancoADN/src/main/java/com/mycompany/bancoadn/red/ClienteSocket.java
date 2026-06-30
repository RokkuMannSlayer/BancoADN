package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.concurrencia.IdSemaforos;
import com.mycompany.bancoadn.concurrencia.SemaforoSistema;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {

    private static String HOST = "localhost";
    private static final int PUERTO = 5000;

    private static Socket socket;
    private static PrintWriter salida;
    private static BufferedReader entrada;

    public static void setHost(String host) {

        HOST = host;
    }

    public static void conectar() throws Exception {

        SemaforoSistema.adquirir(IdSemaforos.SOCKET_CLIENTE);

        try {

            if (socket != null && socket.isConnected() && !socket.isClosed()) {
                return;
            }

            socket = new Socket(HOST, PUERTO);

            salida = new PrintWriter(socket.getOutputStream(), true);

            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } finally {

            SemaforoSistema.liberar(IdSemaforos.SOCKET_CLIENTE);
        }
    }

    public static String enviar(String mensaje) {

        SemaforoSistema.adquirir(IdSemaforos.SOCKET_CLIENTE);

        try {

            if (socket == null || socket.isClosed() || !socket.isConnected()) {

                socket = new Socket(HOST, PUERTO);

                salida = new PrintWriter(socket.getOutputStream(), true);

                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }

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

        } finally {

            SemaforoSistema.liberar(IdSemaforos.SOCKET_CLIENTE);
        }
    }

    public static void desconectar() {

        SemaforoSistema.adquirir(IdSemaforos.SOCKET_CLIENTE);

        try {

            if (socket != null) {

                socket.close();
            }

            socket = null;
            salida = null;
            entrada = null;

        } catch (Exception e) {

            System.out.println("Error cerrando socket: " + e.getMessage());

        } finally {

            SemaforoSistema.liberar(IdSemaforos.SOCKET_CLIENTE);
        }
    }
}