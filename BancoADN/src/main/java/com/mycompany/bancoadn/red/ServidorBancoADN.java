package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.servicios.BancoADN;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class ServidorBancoADN extends JFrame {

    // =========================
    // USUARIOS CONECTADOS
    // =========================
    public static Set<String> usuariosConectados = new HashSet<>();

    // =========================
    // COMPONENTES UI
    // =========================
    private static JTextArea areaUsuarios;

    // =========================
    // CONSTRUCTOR UI
    // =========================
    public ServidorBancoADN() {

        setTitle("Servidor Banco ADN");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "Usuarios conectados",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );

        areaUsuarios = new JTextArea();
        areaUsuarios.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaUsuarios);

        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // =========================
    // ACTUALIZAR UI
    // =========================
    public static synchronized void actualizarUsuarios() {

        SwingUtilities.invokeLater(() -> {

            if (areaUsuarios == null) {
                return;
            }

            StringBuilder sb = new StringBuilder();

            synchronized (usuariosConectados) {

                for (String u : usuariosConectados) {

                    sb.append("• ").append(u).append("\n");
                }
            }

            if (sb.length() == 0) {
                sb.append("No hay usuarios conectados");
            }

            areaUsuarios.setText(sb.toString());
        });
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {

        // UI DEL SERVIDOR
        SwingUtilities.invokeLater(() -> {
            new ServidorBancoADN().setVisible(true);
        });

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

    // =========================
    // HILO CLIENTE
    // =========================
    static class HiloCliente extends Thread {

        private Socket socket;

        private BancoADN banco = new BancoADN();

        private String usuario = "Desconocido";

        public HiloCliente(Socket socket) {

            this.socket = socket;
        }

        @Override
        public void run() {

            try (

                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)

            ) {

                String comando;

                while ((comando = entrada.readLine()) != null) {

                    String respuesta = procesar(comando);

                    salida.println(respuesta);
                }

            } catch (Exception e) {

                System.out.println("Error cliente: " + e.getMessage());

            } finally {

                usuariosConectados.remove(usuario);

                actualizarUsuarios();

                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // =========================
        // PROCESAR
        // =========================
        private String procesar(String comando) {

            try {

                String[] datos = comando.split(",");

                switch (datos[0]) {

                    // ================= LOGIN
                    case "LOGIN":

                        usuario = datos[1];

                        String respuestaLogin = banco.login(datos[1], datos[2]);

                        if (respuestaLogin.startsWith("OK")) {

                            usuariosConectados.add(usuario);

                            actualizarUsuarios();
                        }

                        return respuestaLogin;

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
    }
}