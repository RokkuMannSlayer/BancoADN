package com.mycompany.bancoadn.ui;

import com.mycompany.bancoadn.red.ClienteSocket;
import com.mycompany.bancoadn.modelos.TipoSangre;
import com.mycompany.bancoadn.Sign;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class BancoADNUI extends JFrame {

    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollContenedor;

    private final String rolUsuario;
    private final int idUsuarioActual;

    // Almacena el ID real del perfil genético devuelto por la base de datos
    private int idPerfilActual = -1;

    private String rutaFotoSeleccionada = "sin_foto.jpg";

    public BancoADNUI(String rol, int idUsuario) {
        this.rolUsuario = rol;
        this.idUsuarioActual = idUsuario;

        setTitle("Banco de ADN - Panel de Control");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.BLACK);

        // ===================================
        // PANEL SUPERIOR CONTENEDOR
        // ===================================
        JPanel panelSuperiorEncabezado = new JPanel(new BorderLayout());
        panelSuperiorEncabezado.setBackground(Color.BLACK);
        panelSuperiorEncabezado.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel panelBotonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotonera.setBackground(Color.BLACK);

        if (rolUsuario.equals("CLIENTE")) {
            JButton btnConsultar = boton("Consultar Mi Perfil");
            JButton btnEditar = boton("Editar Mi Perfil");

            panelBotonera.add(btnConsultar);
            panelBotonera.add(btnEditar);

            btnConsultar.addActionListener(e -> realizarConsultaCard("CONSULTAR," + idUsuarioActual));
            btnEditar.addActionListener(e -> abrirVentanaCuestionarioCliente(true));

            SwingUtilities.invokeLater(() -> {
                String check = ClienteSocket.enviar("CONSULTAR," + idUsuarioActual);
                if (check == null || check.equals("No tiene perfil") || check.startsWith("ERROR")) {
                    abrirVentanaCuestionarioCliente(false);
                } else {
                    procesarYMostrarCard(check);
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

        JLabel lblGifCostado = new JLabel();
        lblGifCostado.setIcon(new ImageIcon("dna_146c.gif"));
        lblGifCostado.setHorizontalAlignment(SwingConstants.CENTER);

        panelSuperiorEncabezado.add(panelBotonera, BorderLayout.CENTER);
        panelSuperiorEncabezado.add(lblGifCostado, BorderLayout.EAST);

        // ===================================
        // PANEL CENTRAL DE CONTENIDO
        // ===================================
        JPanel panelContenidoPrincipal = new JPanel(new BorderLayout());
        panelContenidoPrincipal.setBackground(Color.BLACK);
        panelContenidoPrincipal.setBorder(new EmptyBorder(5, 25, 15, 25));

        String[] columnas = {"Información de los Perfiles Registrados en el Sistema"};
        
        modeloTabla = new DefaultTableModel(columnas, 0){
            
            public boolean IsCellEditable(int row, int column){
                return false;
            }   
        };
        
        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setBackground(new Color(25, 25, 25));
        tablaDatos.setForeground(Color.WHITE);
        tablaDatos.setGridColor(Color.DARK_GRAY);
        tablaDatos.setRowHeight(26);

        scrollContenedor = new JScrollPane(tablaDatos);
        scrollContenedor.setBackground(Color.BLACK);
        scrollContenedor.getViewport().setBackground(Color.BLACK);
        scrollContenedor.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        panelContenidoPrincipal.add(scrollContenedor, BorderLayout.CENTER);

        JPanel panelEsquina = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelEsquina.setBackground(Color.BLACK);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(180, 40, 40));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setPreferredSize(new Dimension(140, 30));

        btnCerrarSesion.addActionListener(e -> {
            try {
                ClienteSocket.enviar("LOGOUT");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dispose();
            SwingUtilities.invokeLater(() -> Sign.main(new String[]{}));
        });

        panelEsquina.add(btnCerrarSesion);
        panelContenidoPrincipal.add(panelEsquina, BorderLayout.SOUTH);

        add(panelSuperiorEncabezado, BorderLayout.NORTH);
        add(panelContenidoPrincipal, BorderLayout.CENTER);
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
                    realizarConsultaCard("CONSULTAR_ID," + idNumerico);
                } else {
                    int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar perfil ID: " + idNumerico + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                    if (conf == JOptionPane.YES_OPTION) {
                        String delRes = ClienteSocket.enviar("ELIMINAR," + idNumerico);
                        JOptionPane.showMessageDialog(this, delRes);
                        limpiarTablaAAsfaltoVacio();
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

    private void realizarConsultaCard(String comando) {
        String respuesta = ClienteSocket.enviar(comando);
        if (respuesta == null || respuesta.trim().isEmpty() || respuesta.startsWith("ERROR") || respuesta.contains("no existe")) {
            JPanel panelErr = new JPanel(new FlowLayout());
            panelErr.setBackground(Color.BLACK);
            JLabel lblErr = new JLabel(respuesta != null ? respuesta : "Perfil no encontrado.");
            lblErr.setForeground(Color.RED);
            lblErr.setFont(new Font("SansSerif", Font.BOLD, 14));
            panelErr.add(lblErr);
            scrollContenedor.setViewportView(panelErr);
        } else {
            procesarYMostrarCard(respuesta);
        }
    }

    private void procesarYMostrarCard(String rawData) {
        String[] campos = rawData.split("\\|");
        if (campos.length < 8) {
            return;
        }

        // Mapeo estableciendo el orden e índices reales de la BD
        String id = limpiarValor(campos[0]);
        String fotoPath = campos[1].trim();

        String sangre = limpiarValor(campos[3]);
        String ojos = limpiarValor(campos[4]);
        String pelo = limpiarValor(campos[5]);
        String tendencia = limpiarValor(campos[6]);
        String altura = limpiarValor(campos[7]);
        String peso = (campos.length >= 9) ? limpiarValor(campos[8]) : "0.0";

        // Sincronizamos la variable global con el ID numérico del perfil para usar al editar
        try {
            this.idPerfilActual = Integer.parseInt(id);
        } catch (Exception e) {
            this.idPerfilActual = -1;
        }

        String estado = (campos.length >= 10) ? limpiarValor(campos[9]) : "activo";
        if (!estado.equalsIgnoreCase("activo") && !estado.equalsIgnoreCase("inactivo")) {
            estado = "activo";
        }

        JPanel panelCentrado = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        panelCentrado.setBackground(Color.BLACK);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(20, 20, 20));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(680, 280));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // SECCIÓN FOTO (Columna 0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.NORTH;

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(100, 115));
        lblFoto.setBorder(new LineBorder(Color.GRAY, 1));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        File f = new File(fotoPath);
        if (f.exists() && !fotoPath.equals("sin_foto.jpg")) {
            ImageIcon imgIcon = new ImageIcon(fotoPath);
            Image imgEscalada = imgIcon.getImage().getScaledInstance(100, 115, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblFoto.setText("FOTO");
            lblFoto.setForeground(Color.LIGHT_GRAY);
            lblFoto.setFont(new Font("SansSerif", Font.BOLD, 12));
        }
        card.add(lblFoto, gbc);

        gbc.gridheight = 1;

        // Distribución exacta siguiendo tu bosquejo a mano
        // Fila 0
        gbc.gridx = 1;
        gbc.gridy = 0;
        card.add(crearCuadroDato("ID", id), gbc);
        gbc.gridx = 2;
        card.add(crearCuadroDato("ESTADO", estado.toLowerCase()), gbc);

        // Fila 1
        gbc.gridx = 1;
        gbc.gridy = 1;
        card.add(crearCuadroDato("COLOR DE OJOS", ojos), gbc);
        gbc.gridx = 2;
        card.add(crearCuadroDato("TIPO DE SANGRE", sangre), gbc);

        // Fila 2
        gbc.gridx = 1;
        gbc.gridy = 2;
        card.add(crearCuadroDato("COLOR DE PELO", pelo), gbc);
        gbc.gridx = 2;
        card.add(crearCuadroDato("ALTURA", altura.contains("m") ? altura : altura + " m"), gbc);

        // Fila 3
        gbc.gridx = 1;
        gbc.gridy = 3;
        card.add(crearCuadroDato("TENDENCIA", tendencia), gbc);
        gbc.gridx = 2;
        card.add(crearCuadroDato("PESO", peso.contains("kg") ? peso : peso + " kg"), gbc);

        if (rolUsuario.equals("CLIENTE")) {
            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(15, 10, 5, 10);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;

            JButton btnEditarCard = new JButton("EDITAR");
            btnEditarCard.setBackground(new Color(40, 40, 40));
            btnEditarCard.setForeground(Color.WHITE);
            btnEditarCard.setFont(new Font("SansSerif", Font.BOLD, 11));
            btnEditarCard.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.GRAY, 1), new EmptyBorder(5, 20, 5, 20)
            ));
            btnEditarCard.setFocusPainted(false);
            btnEditarCard.addActionListener(e -> abrirVentanaCuestionarioCliente(true));
            card.add(btnEditarCard, gbc);
        }

        panelCentrado.add(card);
        scrollContenedor.setViewportView(panelCentrado);
        scrollContenedor.revalidate();
        scrollContenedor.repaint();
    }

    private JPanel crearCuadroDato(String etiqueta, String valor) {
        JPanel panelCuadro = new JPanel(new BorderLayout(5, 0));
        panelCuadro.setBackground(new Color(20, 20, 20));

        JLabel lblE = new JLabel(etiqueta + ": ");
        lblE.setForeground(Color.WHITE);
        lblE.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel lblV = new JLabel(" " + valor + " ");
        lblV.setForeground(Color.LIGHT_GRAY);
        lblV.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblV.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.DARK_GRAY, 1),
                new EmptyBorder(3, 8, 3, 8)
        ));
        lblV.setOpaque(true);
        lblV.setBackground(new Color(30, 30, 30));

        panelCuadro.add(lblE, BorderLayout.WEST);
        panelCuadro.add(lblV, BorderLayout.CENTER);
        return panelCuadro;
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

    private void abrirVentanaCuestionarioCliente(boolean esEdicion) {
        JDialog dialogoForm = new JDialog(this, esEdicion ? "Modificar Mi Perfil Genético" : "Formulario de Registro Obligatorio", true);
        dialogoForm.setSize(420, 380);
        dialogoForm.setLayout(new GridBagLayout());
        dialogoForm.setLocationRelativeTo(this);
        dialogoForm.getContentPane().setBackground(new Color(25, 25, 25));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        rutaFotoSeleccionada = "sin_foto.jpg";

        JButton btnBuscarFoto = new JButton("Seleccionar Imagen...");
        btnBuscarFoto.setBackground(new Color(60, 60, 60));
        btnBuscarFoto.setForeground(Color.WHITE);

        JLabel lblNombreFoto = new JLabel("Ninguna foto elegida");
        lblNombreFoto.setForeground(Color.LIGHT_GRAY);
        lblNombreFoto.setFont(new Font("SansSerif", Font.ITALIC, 11));

        btnBuscarFoto.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Buscar Foto de Perfil");
            javax.swing.filechooser.FileNameExtensionFilter filtro = new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "jpeg", "png");
            selector.setFileFilter(filtro);

            int resultado = selector.showOpenDialog(dialogoForm);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = selector.getSelectedFile();
                rutaFotoSeleccionada = archivo.getAbsolutePath();
                lblNombreFoto.setText(archivo.getName());
            }
        });

        JComboBox<TipoSangre> cmbSangre = new JComboBox<>(TipoSangre.values());
        JTextField txtOjos = new JTextField(12);
        JTextField txtPelo = new JTextField(12);
        JTextField txtConducta = new JTextField(12);
        JTextField txtAltura = new JTextField(12);
        JTextField txtPeso = new JTextField(12);

        // Carga y aislamiento de valores limpios en el formulario
        if (esEdicion) {
            String actualRaw = ClienteSocket.enviar("CONSULTAR," + idUsuarioActual);
            if (actualRaw != null && !actualRaw.startsWith("ERROR") && !actualRaw.contains("No tiene perfil")) {
                String[] camposActuales = actualRaw.split("\\|");
                if (camposActuales.length >= 8) {
                    rutaFotoSeleccionada = camposActuales[1].trim();
                    File fileFoto = new File(rutaFotoSeleccionada);
                    if (fileFoto.exists()) {
                        lblNombreFoto.setText(fileFoto.getName());
                    }

                    txtOjos.setText(limpiarValor(camposActuales[4]));
                    txtPelo.setText(limpiarValor(camposActuales[5]));
                    txtConducta.setText(limpiarValor(camposActuales[6]));
                    txtAltura.setText(limpiarValor(camposActuales[7]).replace(" m", ""));

                    if (camposActuales.length >= 9) {
                        txtPeso.setText(limpiarValor(camposActuales[8]).replace(" kg", ""));
                    }

                    String sangreLimpia = limpiarValor(camposActuales[3]);
                    for (int i = 0; i < cmbSangre.getItemCount(); i++) {
                        if (cmbSangre.getItemAt(i).toString().equalsIgnoreCase(sangreLimpia)) {
                            cmbSangre.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }

        c.gridx = 0;
        c.gridy = 0;
        dialogoForm.add(modalLabel("Foto Perfil:"), c);
        c.gridx = 1;
        dialogoForm.add(btnBuscarFoto, c);

        c.gridx = 1;
        c.gridy = 1;
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
                String alt = txtAltura.getText().trim().replace(" m", "");
                String pso = txtPeso.getText().trim().replace(" kg", "");

                Double.parseDouble(alt);
                Double.parseDouble(pso);

                String ojos = txtOjos.getText().trim();
                String pelo = txtPelo.getText().trim();
                String conducta = txtConducta.getText().trim();
                String sangre = cmbSangre.getSelectedItem().toString();

                String res;
                if (!esEdicion) {
                    res = ClienteSocket.enviar(
                            "REGISTRAR," + idUsuarioActual + "," + rutaFotoSeleccionada + "," + sangre + ","
                            + ojos + "," + pelo + "," + conducta + "," + alt + "," + pso
                    );
                } else {
                    // Enviamos idPerfilActual para impactar correctamente en la clave primaria de la BD
                    res = ClienteSocket.enviar(
                            "EDITAR," + idPerfilActual + "," + rutaFotoSeleccionada + "," + sangre + ","
                            + ojos + "," + pelo + "," + conducta + "," + alt + "," + pso + ",activo"
                    );
                }

                if (res != null && !res.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(dialogoForm, "¡Perfil actualizado correctamente!");
                    procesarYMostrarCard(ClienteSocket.enviar("CONSULTAR," + idUsuarioActual));
                    dialogoForm.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogoForm, "Error devuelto por el Servidor:\n" + res);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogoForm, "Campos numéricos inválidos. Use formato '1.75' y '80.0'.");
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

    private String limpiarValor(String texto) {
        if (texto == null) {
            return "";
        }
        String t = texto.trim();
        if (t.contains(":")) {
            return t.substring(t.indexOf(":") + 1).trim();
        }
        return t;
    }
}