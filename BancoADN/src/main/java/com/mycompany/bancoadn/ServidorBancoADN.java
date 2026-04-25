package com.mycompany.bancoadn;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.HashSet;

public class ServidorBancoADN {
    
    public static Set<String> clientesConectados = new HashSet<>();

    public static void main(String[] args) {
        
        if (!ConexionInternet.hayInternet()) {
            System.out.println("Error: no hay conexión a Internet");
            System.exit(0);
        }
        else {
        
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
}

class HiloCliente extends Thread {

    private Socket socket;
    private BancoADN banco = new BancoADN();
    private String usuario = "Desconocido";

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
                usuario = datos[1];

                // AGREGAR USUARIO
                ServidorBancoADN.clientesConectados.add(usuario);
                mostrarClientes();

                String pass = datos[2];
                String resultado = LoginRemoto.login(usuario, pass);

                salida.println(resultado);
            }

            // 🔥 REGISTRAR
            else if (comando.startsWith("REGISTRAR")) {
                String[] datos = comando.split(",");
                int id = Integer.parseInt(datos[1]);
                String desc = datos[2];

                salida.println(banco.registrarPerfil(id, desc));
            }

            // 🔥 CONSULTAR
            else if (comando.startsWith("CONSULTAR")) {
                int id = Integer.parseInt(comando.split(",")[1]);
                salida.println(banco.consultarPerfilCliente(id));
            }

        } catch (Exception e) {
            System.out.println("Error cliente: " + e.getMessage());
        } finally {
            // 🔴 CUANDO SE DESCONECTA
            ServidorBancoADN.clientesConectados.remove(usuario);
            mostrarClientes();

            try {
                socket.close();
            } catch (Exception e) {}
        }
    }

    // 🔥 MOSTRAR CLIENTES EN CONSOLA
    private void mostrarClientes() {
        System.out.println("Usuarios conectados: " + ServidorBancoADN.clientesConectados);
    }
}