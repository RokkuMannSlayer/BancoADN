package com.mycompany.bancoadn.red;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServidorBancoADN {

    public static Set<String> clientesConectados =
            new HashSet<>();

    public static void main(String[] args) {

        int puerto = 5000;

        try (ServerSocket server =
                     new ServerSocket(puerto)) {

            System.out.println(
                    "Servidor iniciado en puerto "
                    + puerto
            );

            while (true) {

                Socket cliente =
                        server.accept();

                new HiloCliente(cliente).start();
            }

        } catch (IOException e) {

            System.out.println(
                    "Error servidor: "
                    + e.getMessage()
            );
        }
    }
}