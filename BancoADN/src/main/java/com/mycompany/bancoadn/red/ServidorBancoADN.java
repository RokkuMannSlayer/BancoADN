package com.mycompany.bancoadn.red;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorBancoADN {

    // =========================
    // USUARIOS CONECTADOS
    // =========================
    public static ConcurrentHashMap<String, Boolean> usuariosConectados = new ConcurrentHashMap<>();

    // =========================
    // UI
    // =========================
    private static JTextArea areaUsuarios;

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        crearVentana();

        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("Servidor iniciado en puerto "+ puerto);

            while (true) {

                Socket cliente = servidor.accept();

                System.out.println("Nuevo cliente conectado");

                new HiloCliente(cliente).start();
            }

        } catch (IOException e) {

            System.out.println("Error servidor: " + e.getMessage());
        }
    }

    // =========================
    // VENTANA
    // =========================
    private static void crearVentana() {

        JFrame frame = new JFrame("Usuarios Conectados");

        frame.setSize(400, 500);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        areaUsuarios = new JTextArea();

        areaUsuarios.setEditable(false);

        areaUsuarios.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaUsuarios);

        frame.add(scroll);

        frame.setVisible(true);
    }

    // =========================
    // ACTUALIZAR USUARIOS
    // =========================
    public static synchronized void actualizarUsuarios() {

        SwingUtilities.invokeLater(() -> {

            StringBuilder sb =  new StringBuilder();

            sb.append("USUARIOS CONECTADOS\n\n");

            if (usuariosConectados.isEmpty()) {

                sb.append("Sin usuarios conectados");

            } else {

                for (String usuario : usuariosConectados.keySet()) {

                    sb.append("• ").append(usuario).append("\n");
                }
            }

            areaUsuarios.setText(sb.toString());
        });
    }
}