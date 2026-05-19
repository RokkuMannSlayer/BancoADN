package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.servicios.BancoADN;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HiloCliente extends Thread {

    private final Socket socket;

    private final BancoADN banco = new BancoADN();

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

            // =========================
            // LOOP DE COMANDOS
            // =========================
            while ((comando = entrada.readLine()) != null) {

                comando = comando.trim();

                // IGNORAR COMANDOS VACÍOS
                if (comando.isEmpty()) {

                    salida.println("ERROR,Comando vacío");

                    continue;
                }

                String respuesta = procesar(comando);

                salida.println(respuesta);
            }

        } catch (Exception e) {

            System.out.println("Error cliente: " + e.getMessage());

        } finally {

            // =========================
            // DESCONECTAR USUARIO
            // =========================
            synchronized (ServidorBancoADN.usuariosConectados) {

                ServidorBancoADN.usuariosConectados.remove(usuario);
            }

            // ACTUALIZAR VENTANA
            ServidorBancoADN .actualizarUsuarios();

            try {

                if (socket != null && !socket.isClosed()) {

                    socket.close();
                }

            } catch (Exception e) {

                System.out.println("Error cerrando socket: " + e.getMessage());
            }
        }
    }

    // =========================
    // PROCESAR COMANDOS
    // =========================
    private String procesar(String comando) {

        try {

            String[] datos = comando.split(",");

            // VALIDAR
            if (datos.length == 0) {

                return "ERROR,Comando inválido";
            }

            switch (datos[0]) {

                // =========================
                // LOGIN
                // =========================
                case "LOGIN":

                    if (datos.length < 3) {

                        return "ERROR,Faltan datos";
                    }

                    usuario = datos[1];

                    String respuestaLogin = banco.login(datos[1], datos[2]);

                    // LOGIN EXITOSO
                    if (respuestaLogin.startsWith("OK")) {

                        synchronized (ServidorBancoADN.usuariosConectados) {

                            ServidorBancoADN.usuariosConectados.add(usuario);
                        }

                        // ACTUALIZAR UI
                        ServidorBancoADN.actualizarUsuarios();
                    }

                    return respuestaLogin;

                // =========================
                // REGISTRO
                // =========================
                case "REGISTRO":

                    if (datos.length < 5) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.registrarCliente(

                            datos[1], // nombre
                            datos[3], // email
                            datos[4], // password
                            datos[2]  // dni
                    );

                // =========================
                // REGISTRAR PERFIL
                // =========================
                case "REGISTRAR":

                    if (datos.length < 3) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.registrarPerfil(

                            Integer.parseInt(datos[1]), datos[2]
                    );

                // =========================
                // CONSULTAR PERFIL
                // =========================
                case "CONSULTAR":

                    if (datos.length < 2) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.consultarPerfilCliente(

                            Integer.parseInt(
                                    datos[1]
                            )
                    );

                // =========================
                // EDITAR PERFIL
                // =========================
                case "EDITAR":

                    if (datos.length < 4) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.editarPerfilGenetico(

                            Integer.parseInt(
                                    datos[1]
                            ),

                            datos[2],
                            datos[3],
                            1
                    );

                // =========================
                // LISTAR
                // =========================
                case "LISTAR":

                    return banco.listarPerfilesTexto();

                // =========================
                // ELIMINAR
                // =========================
                case "ELIMINAR":

                    if (datos.length < 2) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.eliminarPerfil(Integer.parseInt(datos[1]));

                // =========================
                // DESCONOCIDO
                // =========================
                default:

                    return "ERROR,Comando desconocido";
            }

        } catch (NumberFormatException e) {

            return "ERROR,Formato numérico inválido";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }
}