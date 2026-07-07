package com.mycompany.bancoadn.servicios;

import com.mycompany.bancoadn.modelos.TipoSangre;
import com.mycompany.bancoadn.modelos.Usuario;
import com.mycompany.bancoadn.modelos.PerfilGenetico;
import com.mycompany.bancoadn.modelos.LogSistema;
import com.mycompany.bancoadn.persistencia.GestorArchivos;

import java.util.List;

public class BancoADN {

    // =========================
    // LOGIN
    // =========================
    public String login(String email, String password) {

        try {

            List<Usuario> usuarios = GestorArchivos.leerUsuarios();

            for (Usuario u : usuarios) {

                if (u.getEmail().equals(email) && u.getPassword().equals(password)) {

                    GestorArchivos.guardarLog(new LogSistema("LOGIN", email, u.getRol()));

                    return "OK," + u.getRol() + "," + u.getIdUsuario();
                }
            }

            return "ERROR,Credenciales incorrectas";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // REGISTRAR USUARIO
    // =========================
    public String registrarCliente(String nombre, String email, String password, String dni) {

        try {

            List<Usuario> usuarios = GestorArchivos.leerUsuarios();

            for (Usuario u : usuarios) {

                if (u.getEmail().equals(email)) {
                    return "ERROR,Email existente";
                }

                if (u.getDni().equals(dni)) {
                    return "ERROR,DNI existente";
                }
            }

            int nuevoId = usuarios.size() + 1;

            Usuario usuario = new Usuario(nuevoId, nombre, email, password, dni, "CLIENTE");

            GestorArchivos.guardarUsuario(usuario);

            return "OK";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // REGISTRAR PERFIL
    // =========================
    public String registrarPerfil(int idCliente, String fotoPerfil, String tipoSangre, String colorOjos, String colorPelo, String tendencia, double altura, double peso) {

        try {

            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();

            for (PerfilGenetico p : perfiles) {

                if (p.getIdCliente() == idCliente && p.getEstado().equals("activo")) {

                    return "ERROR,Ya tiene perfil";
                }
            }

            int nuevoId = 1;

            for (PerfilGenetico p : perfiles) {
                if (p.getIdPerfil() >= nuevoId) {
                    nuevoId = p.getIdPerfil() + 1;
                }
            }

            PerfilGenetico perfil = new PerfilGenetico(nuevoId, fotoPerfil, TipoSangre.valueOf(tipoSangre), colorOjos, colorPelo, tendencia, altura, peso, "activo", idCliente, 1);

            GestorArchivos.guardarPerfil(perfil);

            return "Perfil registrado correctamente";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // CONSULTAR PERFIL
    // =========================
    public String consultarPerfilCliente(int idCliente) {

        try {

            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();

            List<Usuario> usuarios = GestorArchivos.leerUsuarios();

            String nombre = "";
            String dni = "";

            for (Usuario u : usuarios) {

                if (u.getIdUsuario() == idCliente) {

                    nombre = u.getNombre();
                    dni = u.getDni();

                    break;
                }
            }

            for (PerfilGenetico p : perfiles) {

                if (p.getIdCliente() == idCliente && p.getEstado().equals("activo")) {

                    return "ID Perfil: " + p.getIdPerfil() + " | Cliente: " + nombre + " | DNI: " + dni + " | Sangre: " + p.getTipoSangre() + " | Ojos: " + p.getColorOjos() + " | Pelo: " + p.getColorPelo() + " | Conducta: " + p.getTendenciaConductual() + " | Altura: " + p.getAltura() + " | Peso: " + p.getPeso() + " | IMC: " + String.format("%.2f", p.getImc()) + " | Estado: " + p.getEstado();
                }
            }

            return "No tiene perfil";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }
    public String consultarPerfilPorIdPerfil(int idPerfil) {
        try {
            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();
            List<Usuario> usuarios = GestorArchivos.leerUsuarios();

            for (PerfilGenetico p : perfiles) {
                if (p.getIdPerfil() == idPerfil && p.getEstado().equals("activo")) {
                    String nombre = "";
                    String dni = "";

                    for (Usuario u : usuarios) {
                        if (u.getIdUsuario() == p.getIdCliente()) {
                            nombre = u.getNombre();
                            dni = u.getDni();
                            break;
                        }
                    }

                    return "ID Perfil: " + p.getIdPerfil() + " | Cliente: " + nombre + " | DNI: " + dni + " | Sangre: " + p.getTipoSangre() + " | Ojos: " + p.getColorOjos() + " | Pelo: " + p.getColorPelo() + " | Conducta: " + p.getTendenciaConductual() + " | Altura: " + p.getAltura() + " | Peso: " + p.getPeso() + " | IMC: " + String.format("%.2f", p.getImc()) + " | Estado: " + p.getEstado();
                }
            }
            return "Perfil no existe";
        } catch (Exception e) {
            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // EDITAR PERFIL
    // =========================
    public String editarPerfilGenetico(int idPerfil, String fotoPerfil, String tipoSangre, String colorOjos, String colorPelo, String tendencia, double altura, double peso, String estado, int idAdmin) {

        try {

            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();

            boolean encontrado = false;

            for (PerfilGenetico p : perfiles) {

                if (p.getIdPerfil() == idPerfil) {

                    p.setFotoPerfil(fotoPerfil);

                    p.setTipoSangre(TipoSangre.valueOf(tipoSangre));

                    p.setColorOjos(colorOjos);

                    p.setColorPelo(colorPelo);

                    p.setTendenciaConductual(tendencia);

                    p.setAltura(altura);

                    p.setPeso(peso);

                    p.setEstado(estado);

                    p.setIdAdmin(idAdmin);

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return "ERROR,Perfil no existe";
            }

            GestorArchivos.actualizarPerfiles(perfiles);

            return "Perfil actualizado correctamente";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // ELIMINAR PERFIL
    // =========================
    public String eliminarPerfil(int idPerfil) {

        try {

            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();

            boolean encontrado = false;

            for (PerfilGenetico p : perfiles) {

                if (p.getIdPerfil() == idPerfil) {

                    p.setEstado("eliminado");

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return "ERROR,Perfil no encontrado";
            }

            GestorArchivos.actualizarPerfiles(perfiles);

            return "Perfil eliminado correctamente";

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }

    // =========================
    // LISTAR
    // =========================
    public String listarPerfilesTexto() {

        try {

            List<PerfilGenetico> perfiles = GestorArchivos.leerPerfiles();

            List<Usuario> usuarios = GestorArchivos.leerUsuarios();

            StringBuilder sb = new StringBuilder();

            for (PerfilGenetico p : perfiles) {

                String nombre = "";

                for (Usuario u : usuarios) {

                    if (u.getIdUsuario() == p.getIdCliente()) {

                        nombre = u.getNombre();
                        break;
                    }
                }

                sb.append("ID: ").append(p.getIdPerfil()).append(" | Cliente: ").append(nombre).append(" | Sangre: ").append(p.getTipoSangre()).append(" | Ojos: ").append(p.getColorOjos()).append(" | Pelo: ").append(p.getColorPelo()).append(" | IMC: ").append(String.format("%.2f", p.getImc())).append(" | Estado: ").append(p.getEstado()).append("\n");
            }

            return sb.toString();

        } catch (Exception e) {

            return "ERROR," + e.getMessage();
        }
    }
}