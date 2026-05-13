package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.servicios.BancoADN;

import java.io.*;
import java.net.Socket;

public class HiloCliente extends Thread {

    private Socket socket;

    private BancoADN banco =
            new BancoADN();

    private String usuario =
            "Desconocido";

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (

            BufferedReader entrada =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()
                            )
                    );

            PrintWriter salida =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true
                    );

        ) {

            String comando =
                    entrada.readLine();

            String respuesta =
                    procesar(comando);

            salida.println(respuesta);

        } catch (Exception e) {

            System.out.println(
                    "Error cliente: "
                    + e.getMessage()
            );

        } finally {

            ServidorBancoADN
                    .clientesConectados
                    .remove(usuario);

            mostrarClientes();

            try {
                socket.close();
            } catch (Exception e) {}
        }
    }

    // =========================
    // PROCESAR
    // =========================
    private String procesar(String comando) {

        try {

            String[] datos =
                    comando.split(",");

            switch (datos[0]) {

                // ================= LOGIN
                case "LOGIN":

                    usuario = datos[1];

                    String pass = datos[2];

                    ServidorBancoADN
                            .clientesConectados
                            .add(usuario);

                    mostrarClientes();

                    return banco.login(
                            usuario,
                            pass
                    );

                // ================= REGISTRO
                case "REGISTRO":

                    return banco.registrarCliente(
                            datos[1],
                            datos[3],
                            datos[4],
                            datos[2]
                    );

                // ================= REGISTRAR PERFIL
                case "REGISTRAR":

                    return banco.registrarPerfil(
                            Integer.parseInt(datos[1]),
                            datos[2]
                    );

                // ================= CONSULTAR
                case "CONSULTAR":

                    return banco.consultarPerfilCliente(
                            Integer.parseInt(datos[1])
                    );

                // ================= EDITAR
                case "EDITAR":

                    return banco.editarPerfilGenetico(
                            Integer.parseInt(datos[1]),
                            datos[2],
                            datos[3],
                            1
                    );

                // ================= LISTAR
                case "LISTAR":

                    return banco.listarPerfilesTexto();

                // ================= ELIMINAR
                case "ELIMINAR":

                    return banco.eliminarPerfil(
                            Integer.parseInt(datos[1])
                    );

                default:
                    return "ERROR,Comando desconocido";
            }

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // CLIENTES CONECTADOS
    // =========================
    private void mostrarClientes() {

        System.out.println(
                "Usuarios conectados: "
                + ServidorBancoADN
                .clientesConectados
        );
    }
}