package com.mycompany.bancoadn.red;

import com.mycompany.bancoadn.servicios.BancoADN;
import com.mycompany.bancoadn.concurrencia.SemaforoUsuarios;

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

            BufferedReader entrada =new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {

            String comando;

            while ((comando = entrada.readLine()) != null) {

                comando = comando.trim();

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

            liberarSesion();

            try {

                if(!socket.isClosed()) {
                    
                    socket.close();
                }

            } catch (Exception e) {
                
                e.printStackTrace();
            }
        }
    }

    private void liberarSesion() {

        if (usuario == null || usuario.equals("Desconocido")) {

            return;
        }

        SemaforoUsuarios.salir(usuario);

        synchronized (ServidorBancoADN.usuariosConectados) {

            ServidorBancoADN.usuariosConectados.remove(usuario);
        }
        
        usuario = "Desconocido";
        
        System.out.println("[SESIÓN LIBERADA] " + usuario);

        ServidorBancoADN.actualizarUsuarios();
    }

    private String procesar(String comando) {

        try {

            String[] datos = comando.split(",");

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

                    String email = datos[1].trim().toLowerCase();

                    String password = datos[2].trim();

                    String respuestaLogin = banco.login(email, password);

                    if (!respuestaLogin.startsWith("OK")) {

                        return respuestaLogin;
                    }

                    if (!SemaforoUsuarios.entrar(email)) {

                        return "ERROR,El usuario ya tiene una sesión activa";
                    }

                    usuario = email;

                    synchronized (ServidorBancoADN.usuariosConectados) {

                        ServidorBancoADN.usuariosConectados.put(usuario, true);
                    }

                    ServidorBancoADN.actualizarUsuarios();
                    
                    System.out.println("[LOGIN] " + email);

                    return respuestaLogin;

                // =========================
                // REGISTRO
                // =========================
                case "REGISTRO":

                    if (datos.length < 5) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.registrarCliente(datos[1], datos[3], datos[4], datos[2] );

                // =========================
                // REGISTRAR PERFIL
                // =========================
                case "REGISTRAR":

                    if (datos.length < 9) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.registrarPerfil(
                            Integer.parseInt(datos[1]),
                            datos[2],
                            datos[3],
                            datos[4],
                            datos[5],
                            datos[6],
                            Double.parseDouble(datos[7]),
                            Double.parseDouble(datos[8])
                    );

                // =========================
                // CONSULTAR PERFIL POR ID
                // =========================
                case "CONSULTAR_ID":

                    if (datos.length < 2) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.consultarPerfilPorIdPerfil(Integer.parseInt(datos[1]));

                // =========================
                // CONSULTAR PERFIL CLIENTE
                // =========================
                case "CONSULTAR":

                    if (datos.length < 2) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.consultarPerfilCliente(Integer.parseInt(datos[1]));

                // =========================
                // EDITAR PERFIL
                // =========================
                case "EDITAR":

                    if (datos.length < 10) {

                        return "ERROR,Faltan datos";
                    }

                    return banco.editarPerfilGenetico(
                            Integer.parseInt(datos[1]),
                            datos[2],
                            datos[3],
                            datos[4],
                            datos[5],
                            datos[6],
                            Double.parseDouble(datos[7]),
                            Double.parseDouble(datos[8]),
                            datos[9],
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

                    return banco.eliminarPerfil(
                            Integer.parseInt(datos[1])
                    );

                // =========================
                // LOGOUT
                // =========================
                case "LOGOUT":

                    if(usuario != null && !usuario.equals("Desconocido")) {
                        
                        liberarSesion();
                        
                        usuario = "Desconocido";
                    }
                    
                    System.out.println("[LOGOUT] " + usuario);

                    return "OK,Logout";

                // =========================
                // COMANDO DESCONOCIDO
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