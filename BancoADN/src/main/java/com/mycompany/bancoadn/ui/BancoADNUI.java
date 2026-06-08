package com.mycompany.bancoadn.ui;

import com.mycompany.bancoadn.red.ClienteSocket;
import com.mycompany.bancoadn.modelos.TipoSangre;
import com.mycompany.bancoadn.Sign;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class BancoADNUI extends JFrame {

    private JTextArea areaSalida;
    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollContenedor;

    private final String rolUsuario;
    private final int idUsuarioActual;

    // VARIABLE GLOBAL PARA GUARDAR LA RUTA ABSOLUTA DE LA IMAGEN
    private String rutaFotoSeleccionada = "sin_foto.jpg";

    public BancoADNUI(String rol, int idUsuario) {
        this.rolUsuario = rol;
        this.idUsuarioActual = idUsuario;

        setTitle("Banco de ADN - Panel de Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================
        // TÍTULO DE LA INTERFAZ
        // =========================
        JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
        lblTitulo.setIcon(new ImageIcon("dna_146c.gif"));
        lblTitulo.setBorder(new EmptyBorder(15, 10, 15, 10));
        getContentPane().setBackground(Color.BLACK);

        // =========================
        // PANEL CENTRAL: BOTONES REDUCIDOS
        // =========================
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.BLACK);
        panelCentral.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel panelBotonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotonera.setBackground(Color.BLACK);

        if (rolUsuario.equals("CLIENTE")) {
            JButton btnConsultar = boton("Consultar Mi Perfil");
            JButton btnEditar = boton("Editar Mi Perfil");

            panelBotonera.add(btnConsultar);
            panelBotonera.add(btnEditar);

            btnConsultar.addActionListener(e -> mostrarTexto(ClienteSocket.enviar("CONSULTAR," + idUsuarioActual)));
            btnEditar.addActionListener(e -> abrirVentanaCuestionarioCliente(true));

            SwingUtilities.invokeLater(() -> {
                String check = ClienteSocket.enviar("CONSULTAR," + idUsuarioActual);
                if (check == null || check.equals("No tiene perfil") || check.startsWith("ERROR")) {
                    mostrarTexto("No se detectó un perfil activo. Cargando cuestionario obligatorio...");
                    abrirVentanaCuestionarioCliente(false);
                } else {
                    mostrarTexto(check);
                }
            });

        } else if (rolUsuario.equals("ADMIN")) {
            JButton btnListar = boton("Listar Perfiles");
            JButton btnConsultar = boton("Consultar Perfil");
            JButton btnEliminar = boton("Eliminar Perfil");

            panelBotonera.add(btnListar);
            panelBotonera.add(btnConsultar);
            panelBotonera.add(btnEliminar);

            btnListar.addActionListener(e -> cargarDatosEnTabla(ClienteSocket.enviar("LISTAR")));
            btnConsultar.addActionListener(e -> abrirPantallaBusquedaAdmin("CONSULTAR"));
            btnEliminar.addActionListener(e -> abrirPantallaBusquedaAdmin("ELIMINAR"));
        }

        panelCentral.add(panelBotonera);

        // =========================
        // PANEL INFERIOR (CONSOLA MIXTA)
        // =========================
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.BLACK);
        panelInferior.setBorder(new EmptyBorder(5, 25, 15, 25));

        String[] columnas = {"Información de los Perfiles Registrados en el Sistema"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setBackground(new Color(25, 25, 25));
        tablaDatos.setForeground(Color.WHITE);
        tablaDatos.setGridColor(Color.DARK_GRAY);
        tablaDatos.setRowHeight(24);

        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setBackground(new Color(15, 15, 15));
        areaSalida.setForeground(new Color(0, 230, 110));
        areaSalida.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaSalida.setBorder(new EmptyBorder(10, 10, 10, 10));

        scrollContenedor = new JScrollPane(areaSalida);
        scrollContenedor.setPreferredSize(new Dimension(150, 220));
        panelInferior.add(scrollContenedor, BorderLayout.CENTER);

        JPanel panelEsquina = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelEsquina.setBackground(Color.BLACK);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(180, 40, 40));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setPreferredSize(new Dimension(140, 30));

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> Sign.main(new String[]{}));
        });

        panelEsquina.add(btnCerrarSesion);
        panelInferior.add(panelEsquina, BorderLayout.SOUTH);

        add(lblTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void abrirPantallaBusquedaAdmin(String accion) {
        JDialog ventanaBusqueda = new JDialog(this, "Buscador de Perfiles por ID", true);
        ventanaBusqueda.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        ventanaBusqueda.setSize(340, 130);
        ventanaBusqueda.setLocationRelativeTo(this);
        ventanaBusqueda.getContentPane().setBackground(Color.DARK_GRAY);

        JLabel lblId = new JLabel("ID de Perfil:");
        lblId.setForeground(Color.WHITE);
        JTextField txtId = new JTextField(10);
        JButton btnEjecutar = new JButton(accion.equals("CONSULTAR") ? "Buscar" : "Eliminar");

        btnEjecutar.setBackground(new Color(30, 95, 180));
        btnEjecutar.setForeground(Color.WHITE);

        btnEjecutar.addActionListener(e -> {
            String entrada = txtId.getText().trim();
            ventanaBusqueda.dispose();

            if (entrada.isEmpty()) {
                limpiarTablaAAsfaltoVacio();
                return;
            }

            try {
                int idNumerico = Integer.parseInt(entrada);
                if (idNumerico <= 0) {
                    limpiarTablaAAsfaltoVacio();
                    return;
                }

                if (accion.equals("CONSULTAR")) {
                    String respuesta = ClienteSocket.enviar("CONSULTAR_ID," + idNumerico);
                    if (respuesta == null || respuesta.trim().isEmpty() || respuesta.startsWith("ERROR")
                            || respuesta.contains("Perfil no existe")) {
                        limpiarTablaAAsfaltoVacio();
                        mostrarTexto(respuesta != null ? respuesta : "Perfil no encontrado.");
                    } else {
                        cargarDatosEnTabla(respuesta);
                    }
                } else {
                    int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar perfil ID: " + idNumerico + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        String delRes = ClienteSocket.enviar("ELIMINAR," + idNumerico);
                        mostrarTexto(delRes);
                    }
                }
            } catch (NumberFormatException ex) {
                limpiarTablaAAsfaltoVacio();
            }
        });

        ventanaBusqueda.add(lblId);
        ventanaBusqueda.add(txtId);
        ventanaBusqueda.add(btnEjecutar);
        ventanaBusqueda.setVisible(true);
    }

    private void cargarDatosEnTabla(String datosRaw) {
        modeloTabla.setRowCount(0);
        if (datosRaw == null || datosRaw.trim().isEmpty() || datosRaw.startsWith("ERROR")) {
            limpiarTablaAAsfaltoVacio();
            return;
        }

        scrollContenedor.setViewportView(tablaDatos);
        String[] lineas = datosRaw.split("\n");
        for (String linea : lineas) {
            if (!linea.trim().isEmpty()) {
                modeloTabla.addRow(new Object[]{linea.trim()});
            }
        }
        scrollContenedor.revalidate();
        scrollContenedor.repaint();
    }

    private void limpiarTablaAAsfaltoVacio() {
        modeloTabla.setRowCount(0);
        scrollContenedor.setViewportView(tablaDatos);
        scrollContenedor.revalidate();
        scrollContenedor.repaint();
    }

    private void mostrarTexto(String txt) {
        scrollContenedor.setViewportView(areaSalida);
        areaSalida.setText(txt);
        scrollContenedor.revalidate();
        scrollContenedor.repaint();
    }

    // ========================================================
    // CUESTIONARIO COMPLETO (CON FILTRADO DE IMÁGENES POR BOTÓN)
    // ========================================================
    private void abrirVentanaCuestionarioCliente(boolean esEdicion) {
        JDialog dialogoForm = new JDialog(this, esEdicion ? "Modificar Mi Perfil Genético" : "Formulario de Registro Obligatorio", true);
        dialogoForm.setSize(420, 380); // Ajustado levemente el ancho para el layout del botón
        dialogoForm.setLayout(new GridBagLayout());
        dialogoForm.setLocationRelativeTo(this);
        dialogoForm.getContentPane().setBackground(new Color(25, 25, 25));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Reseteamos el path por defecto al abrir el formulario
        rutaFotoSeleccionada = "sin_foto.jpg";

        // Componentes para la selección interactiva de archivos
        JButton btnBuscarFoto = new JButton("Seleccionar Imagen...");
        btnBuscarFoto.setBackground(new Color(60, 60, 60));
        btnBuscarFoto.setForeground(Color.WHITE);

        JLabel lblNombreFoto = new JLabel("Ninguna foto elegida");
        lblNombreFoto.setForeground(Color.LIGHT_GRAY);
        lblNombreFoto.setFont(new Font("SansSerif", Font.ITALIC, 11));

        // Lógica del explorador de archivos nativo
        btnBuscarFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Buscar Foto de Perfil");

            // Filtro estricto para extensiones de imagen
            javax.swing.filechooser.FileNameExtensionFilter filtro
                    = new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
            selector.setFileFilter(filtro);

            int resultado = selector.showOpenDialog(dialogoForm);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                rutaFotoSeleccionada = archivo.getAbsolutePath(); // Guardamos el path completo para el socket
                lblNombreFoto.setText(archivo.getName()); // Mostramos solo el nombre en el modal
            }
        });

        JComboBox<TipoSangre> cmbSangre = new JComboBox<>(TipoSangre.values());
        JTextField txtOjos = new JTextField(12);
        JTextField txtPelo = new JTextField(12);
        JTextField txtConducta = new JTextField(12);
        JTextField txtAltura = new JTextField(12);
        JTextField txtPeso = new JTextField(12);

        // Posicionamiento de los componentes en el GridBagLayout
        c.gridx = 0;
        c.gridy = 0;
        dialogoForm.add(modalLabel("Foto Perfil:"), c);
        c.gridx = 1;
        // Metemos el botón buscador en lugar del viejo JTextField
        dialogoForm.add(btnBuscarFoto, c);

        c.gridx = 1;
        c.gridy = 1;
        // Renglón extra para que el cliente verifique visualmente qué archivo cargó
        dialogoForm.add(lblNombreFoto, c);

        c.gridx = 0;
        c.gridy = 2;
        dialogoForm.add(modalLabel("Tipo Sangre:"), c);
        c.gridx = 1;
        dialogoForm.add(cmbSangre, c);

        c.gridx = 0;
        c.gridy = 3;
        dialogoForm.add(modalLabel("Color Ojos:"), c);
        c.gridx = 1;
        dialogoForm.add(txtOjos, c);

        c.gridx = 0;
        c.gridy = 4;
        dialogoForm.add(modalLabel("Color Pelo:"), c);
        c.gridx = 1;
        dialogoForm.add(txtPelo, c);

        c.gridx = 0;
        c.gridy = 5;
        dialogoForm.add(modalLabel("Conducta:"), c);
        c.gridx = 1;
        dialogoForm.add(txtConducta, c);

        c.gridx = 0;
        c.gridy = 6;
        dialogoForm.add(modalLabel("Altura (m):"), c);
        c.gridx = 1;
        dialogoForm.add(txtAltura, c);

        c.gridx = 0;
        c.gridy = 7;
        dialogoForm.add(modalLabel("Peso (kg):"), c);
        c.gridx = 1;
        dialogoForm.add(txtPeso, c);

        JButton btnEnviar = new JButton(esEdicion ? "Guardar Cambios" : "Registrar Mi Perfil");
        btnEnviar.setBackground(new Color(30, 120, 60));
        btnEnviar.setForeground(Color.WHITE);

        btnEnviar.addActionListener(e -> {
            try {
                double alt = Double.parseDouble(txtAltura.getText());
                double pso = Double.parseDouble(txtPeso.getText());
                String res;

                // CAMBIO: Ahora pasamos "rutaFotoSeleccionada" en vez de txtFoto.getText()
                if (!esEdicion) {
                    res = ClienteSocket.enviar(
                            "REGISTRAR," + idUsuarioActual + "," + rutaFotoSeleccionada + "," + cmbSangre.getSelectedItem().toString() + ","
                            + txtOjos.getText() + "," + txtPelo.getText() + "," + txtConducta.getText() + "," + alt + "," + pso
                    );
                } else {
                    res = ClienteSocket.enviar(
                            "EDITAR," + idUsuarioActual + "," + rutaFotoSeleccionada + "," + cmbSangre.getSelectedItem().toString() + ","
                            + txtOjos.getText() + "," + txtPelo.getText() + "," + txtConducta.getText() + "," + alt + "," + pso + ",activo"
                    );
                }

                mostrarTexto(res);

                if (!res.startsWith("ERROR")) {
                    mostrarTexto(ClienteSocket.enviar("CONSULTAR," + idUsuarioActual));
                    dialogoForm.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogoForm, "Por favor, valide los campos numéricos.");
            }
        });

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        dialogoForm.add(btnEnviar, c);

        dialogoForm.setVisible(true);
    }

    private JLabel modalLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private JButton boton(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(new Color(35, 90, 175));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(150, 30));
        return b;
    }
}
