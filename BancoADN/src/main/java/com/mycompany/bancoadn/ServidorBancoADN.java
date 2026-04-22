package com.mycompany.bancoadn;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;

public class ServidorBancoADN {

    public static void main(String[] args) {
        int puerto = 5000;

        try (ServerSocket server = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en puerto " + puerto);

            while (true) {
                Socket cliente = server.accept();
                new HiloCliente(cliente).start();
            }

        } catch (IOException e) {
            System.out.println("Error servidor: " + e.getMessage());
        }
    }
}

// 🔹 HILO PARA CADA CLIENTE
class HiloCliente extends Thread {

    private Socket socket;
    private BancoADN banco = new BancoADN();

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {

            String comando = entrada.readLine();

            // 🔥 LOGIN
            if (comando.startsWith("LOGIN")) {
                String[] datos = comando.split(",");
                String user = datos[1];
                String pass = datos[2];

                String resultado = LoginRemoto.login(user, pass);
                salida.println(resultado);
            }

            // 🔥 REGISTRAR PERFIL
            else if (comando.startsWith("REGISTRAR")) {
                String[] datos = comando.split(",");
                int id = Integer.parseInt(datos[1]);
                String desc = datos[2];

                salida.println(banco.registrarPerfil(id, desc));
            }

            // 🔥 CONSULTAR PERFIL
            else if (comando.startsWith("CONSULTAR")) {
                int id = Integer.parseInt(comando.split(",")[1]);
                salida.println(banco.consultarPerfilCliente(id));
            }

            socket.close();

        } catch (Exception e) {
            System.out.println("Error cliente: " + e.getMessage());
        }
    }
}