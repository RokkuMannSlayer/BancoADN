package com.mycompany.bancoadn;

import java.io.*;
import java.net.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Set;
import java.util.HashSet;

public class ServidorBancoADN {

    public static Set<String> clientesConectados = new HashSet<>();

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

            String comando;

            // 🔥 LOOP → permite múltiples comandos por conexión
            while ((comando = entrada.readLine()) != null) {

                String respuesta = procesar(comando);
                salida.println(respuesta);
            }

        } catch (Exception e) {
            System.out.println("Error cliente: " + e.getMessage());
        } finally {
            ServidorBancoADN.clientesConectados.remove(usuario);
            mostrarClientes();

            try { socket.close(); } catch (Exception e) {}
        }
    }

    // =========================
    // PROCESAR COMANDOS
    // =========================
    private String procesar(String comando) {

        try {

            String[] datos = comando.split(",");

            switch (datos[0]) {

                case "LOGIN":
                    usuario = datos[1];
                    String pass = datos[2];

                    ServidorBancoADN.clientesConectados.add(usuario);
                    mostrarClientes();

                    return LoginRemoto.login(usuario, pass);

                case "REGISTRO":
                    return banco.registrarCliente(
                            datos[1], // nombre
                            datos[3], // email
                            datos[4], // pass
                            datos[2]  // dni
                    );

                case "REGISTRAR":
                    return banco.registrarPerfil(
                            Integer.parseInt(datos[1]),
                            datos[2]
                    );

                case "CONSULTAR":
                    return banco.consultarPerfilCliente(
                            Integer.parseInt(datos[1])
                    );

                case "EDITAR":
                    return banco.editarPerfilGenetico(
                            Integer.parseInt(datos[1]),
                            datos[2],
                            datos[3],
                            1
                    );

                case "LISTAR":
                    return listarPerfilesTexto();

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
    // LISTAR EN TEXTO (simple)
    // =========================
    private String listarPerfilesTexto() {

    StringBuilder sb = new StringBuilder();

    try (Connection con = ConexionBD.conectar();
         CallableStatement cs = con.prepareCall("{CALL ListarPerfiles()}");
         ResultSet rs = cs.executeQuery()) {

        while (rs.next()) {
            sb.append("ID: ").append(rs.getInt("IDperfil"))
              .append(" | Cliente: ").append(rs.getString("Nombre_cliente"))
              .append(" | DNI: ").append(rs.getString("DNI_cliente"))
              .append(" | Estado: ").append(rs.getString("Estado"))
              .append("\n");
        }

        if (sb.length() == 0) {
            return "Sin datos";
        }

        return sb.toString();

    } catch (Exception e) {
        return "ERROR," + e.getMessage();
    }
}

    private void mostrarClientes() {
        System.out.println("Usuarios conectados: " + ServidorBancoADN.clientesConectados);
    }
}