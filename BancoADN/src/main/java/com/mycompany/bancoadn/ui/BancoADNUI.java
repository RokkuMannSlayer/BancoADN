package com.mycompany.bancoadn.ui;

import com.mycompany.bancoadn.red.ClienteSocket;
import com.mycompany.bancoadn.modelos.TipoSangre;
import java.io.File;
import javax.swing.*;
import java.awt.*;

public class BancoADNUI extends JFrame {

    private JTextArea areaSalida;

    public BancoADNUI(String rol, int idUsuario) {


        setTitle("Banco de ADN");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================
        // DESCONECTAR SOCKET
        // =========================
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {

                ClienteSocket.desconectar();
            }
        });

        // =========================
        // TÍTULO
        // =========================
        JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);

        lblTitulo.setIcon(new ImageIcon("dna_146c.gif") );

        getContentPane().setBackground(Color.BLACK);

        JPanel panelBotones = new JPanel(new GridLayout(0, 2));

        panelBotones.setBackground(Color.BLACK);

        // =========================
        // CLIENTE
        // =========================
        if (rol.equals("CLIENTE")) {

            JTextField txtFoto = new JTextField(15);

            JComboBox<TipoSangre> cmbSangre = new JComboBox<>(TipoSangre.values());

            JTextField txtOjos = new JTextField(10);

            JTextField txtPelo = new JTextField(10);

            JTextField txtConducta = new JTextField(10);

            JTextField txtAltura = new JTextField(5);

            JTextField txtPeso = new JTextField(5);

            JLabel lblIMC = new JLabel("IMC: 0.0");

            lblIMC.setForeground(Color.WHITE);

            JTextField txtIdPerfil = new JTextField(5);

            JButton btnRegistrar = boton("Registrar Perfil");

            JButton btnConsultar = boton("Consultar Mi Perfil");

            JButton btnEditar = boton("Editar Perfil");
            
            JButton btnFoto = boton("Seleccionar Foto");

            btnFoto.addActionListener(e -> {

                JFileChooser chooser = new JFileChooser();

                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                    File archivo = chooser.getSelectedFile();

                    txtFoto.setText(archivo.getAbsolutePath());
                }
            });

            JLabel lblId = new JLabel("ID Perfil:");

            lblId.setForeground(Color.WHITE);

            JLabel lblDesc = new JLabel("Descripción:");

            lblDesc.setForeground(Color.WHITE);

            panelBotones.add(lblId);
            panelBotones.add(txtIdPerfil);

            panelBotones.add(new JLabel("Foto"));
            panelBotones.add(txtFoto);

            panelBotones.add(btnFoto);

            panelBotones.add(new JLabel("Sangre"));
            panelBotones.add(cmbSangre);

            panelBotones.add(new JLabel("Ojos"));
            panelBotones.add(txtOjos);

            panelBotones.add(new JLabel("Pelo"));
            panelBotones.add(txtPelo);

            panelBotones.add(new JLabel("Conducta"));
            panelBotones.add(txtConducta);

            panelBotones.add(new JLabel("Altura"));
            panelBotones.add(txtAltura);

            panelBotones.add(new JLabel("Peso"));
            panelBotones.add(txtPeso);

            panelBotones.add(lblIMC);

            // =========================
            // REGISTRAR
            // =========================
            btnRegistrar.addActionListener(e -> {

                String foto = txtFoto.getText();

                String sangre = cmbSangre.getSelectedItem().toString();

                String ojos = txtOjos.getText();

                String pelo = txtPelo.getText();

                String conducta = txtConducta.getText();

                double altura = Double.parseDouble(txtAltura.getText());

                double peso = Double.parseDouble(txtPeso.getText());

                double imc = peso / (altura * altura);

                lblIMC.setText("IMC: " + String.format("%.2f", imc));

                String res = ClienteSocket.enviar(
                    "REGISTRAR,"
                    + idUsuario + ","
                    + foto + ","
                    + sangre + ","
                    + ojos + ","
                    + pelo + ","
                    + conducta + ","
                    + altura + ","
                    + peso
                );
                
                mostrar(res);
            });

            // =========================
            // CONSULTAR
            // =========================
            btnConsultar.addActionListener(e -> {

                String res = ClienteSocket.enviar("CONSULTAR," + idUsuario);

                mostrar(res);
            });

            // =========================
            // EDITAR
            // =========================
            btnEditar.addActionListener(e -> {

                String idPerfilTxt = txtIdPerfil.getText().trim();

                String foto = txtFoto.getText();

                String sangre = cmbSangre.getSelectedItem().toString();

                String ojos = txtOjos.getText();

                String pelo = txtPelo.getText();

                String conducta = txtConducta.getText();

                double altura = Double.parseDouble(txtAltura.getText());

                double peso = Double.parseDouble(txtPeso.getText());

                String res = ClienteSocket.enviar(

                    "EDITAR,"
                    + idPerfilTxt + ","
                    + foto + ","
                    + sangre + ","
                    + ojos + ","
                    + pelo + ","
                    + conducta + ","
                    + altura + ","
                    + peso + ",activo"
                );

                mostrar(res);
            });

            panelBotones.add(btnRegistrar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEditar);
        }

        // =========================
        // ADMIN
        // =========================
        else if (rol.equals("ADMIN")) {

            JButton btnListar = boton("Listar Perfiles");

            JButton btnConsultar = boton("Consultar Perfiles");

            JButton btnEliminar = boton("Eliminar Perfil");

            // =========================
            // LISTAR
            // =========================
            btnListar.addActionListener(e -> {

                mostrar(ClienteSocket.enviar("LISTAR"));
            });

            // =========================
            // CONSULTAR
            // =========================
            btnConsultar.addActionListener(e -> {

                mostrar(ClienteSocket.enviar("LISTAR"));
            });

            // =========================
            // ELIMINAR
            // =========================
            btnEliminar.addActionListener(e -> {

                String id = JOptionPane.showInputDialog(this, "ID del perfil:" );

                if (id != null && !id.isEmpty()) {

                    mostrar(ClienteSocket.enviar("ELIMINAR," + id));
                }
            });

            panelBotones.add(btnListar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEliminar);
        }

        // =========================
        // ÁREA SALIDA
        // =========================
        areaSalida = new JTextArea();

        areaSalida.setEditable(false);

        areaSalida.setPreferredSize(new Dimension(115, 115));

        areaSalida.setBackground(Color.BLACK);

        areaSalida.setForeground(Color.WHITE);

        JScrollPane scroll =  new JScrollPane(areaSalida);

        // =========================
        // ADD
        // =========================
        add(lblTitulo, BorderLayout.NORTH);

        add(panelBotones, BorderLayout.CENTER);

        add(scroll, BorderLayout.SOUTH);
    }

    // =========================
    // MOSTRAR RESPUESTA
    // =========================
    private void mostrar(String res) {

        if (res == null || res.startsWith("ERROR")) {

            JOptionPane.showMessageDialog(this, res);

        } else {

            areaSalida.setText(res);
        }
    }

    // =========================
    // BOTÓN ESTILO
    // =========================
    private JButton boton(String txt) {

        JButton b = new JButton(txt);

        b.setBackground(Color.BLUE);

        b.setForeground(Color.WHITE);

        return b;
    }
}