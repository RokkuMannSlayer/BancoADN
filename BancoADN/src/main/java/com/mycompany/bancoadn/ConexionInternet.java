package com.mycompany.bancoadn;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ConexionInternet {

    private static final String HOST = "localhost"; // o IP del servidor
    private static final int PUERTO = 5000;

    public static boolean hayServidor() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PUERTO), 2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}