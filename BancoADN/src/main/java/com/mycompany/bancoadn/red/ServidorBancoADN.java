package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.concurrencia.SemaforoUsuarios;
import com.mycompany.bancoadn.concurrencia.SemaforoSistema;
import com.mycompany.bancoadn.concurrencia.IdSemaforos;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServidorBancoADN {

    public static ConcurrentHashMap<String, Boolean> usuariosConectados = new ConcurrentHashMap<>();

    private static JTextArea areaUsuarios;

    public static void main(String[] args) {

        crearVentana();

        int puerto = 5000;

        try {

            InetAddress ipLocal = InetAddress.getLocalHost();

            System.out.println("Servidor iniciado");

            System.out.println("IP Local: " + ipLocal.getHostAddress());

            System.out.println("Puerto: " + puerto);

        } catch (Exception e) {

            e.printStackTrace();
        }

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            
            System.out.println("Servidor escuchando en: " + servidor.getInetAddress() + ":" + servidor.getLocalPort());
            
            while (true) {

                Socket cliente = servidor.accept();

                new HiloCliente(cliente).start();
            }

        } catch (IOException e) {

            System.out.println("Error servidor: "  + e.getMessage());
        }
    }

    private static void crearVentana() {

        JFrame frame = new JFrame("Banco ADN - Servidor");

        frame.setSize(500, 600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        areaUsuarios = new JTextArea();

        areaUsuarios.setEditable(false);

        areaUsuarios.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(areaUsuarios);

        frame.add(scroll);

        frame.setVisible(true);

        actualizarUsuarios();
    }

    public static void actualizarUsuarios() {

        SwingUtilities.invokeLater(() -> {

            StringBuilder sb = new StringBuilder();

            sb.append("===== SERVIDOR BANCO ADN =====\n\n");
        
            sb.append("Usuarios conectados: ").append(SemaforoUsuarios.cantidadUsuarios()).append("\n\n");

            SemaforoSistema.adquirir(IdSemaforos.USUARIOS_CONECTADOS);

            try {

                if (usuariosConectados.isEmpty()) {

                    sb.append("Sin usuarios conectados");

                } else {

                    for (String usuario : usuariosConectados.keySet()) {

                        sb.append("• ").append(usuario).append("\n");
                    }
                }

            } finally {

                SemaforoSistema.liberar(IdSemaforos.USUARIOS_CONECTADOS);
            }

            areaUsuarios.setText(sb.toString());
        });
    }
}