package com.mycompany.bancoadn;

import java.io.*;
import java.net.*;

public class ClienteSocket {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static String enviar(String mensaje) {
        try (
            Socket socket = new Socket(HOST, PUERTO);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            salida.println(mensaje);
            return entrada.readLine();

        } catch (Exception e) {
            return "ERROR," + e.getMessage();
        }
    }
}