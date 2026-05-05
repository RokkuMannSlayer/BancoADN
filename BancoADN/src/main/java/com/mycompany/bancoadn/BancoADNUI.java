package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;

public class BancoADNUI extends JFrame {

    private JTextArea areaSalida;
    private String rol;
    private int idUsuario;

    public BancoADNUI(String rol, int idUsuario) {

        this.rol = rol;
        this.idUsuario = idUsuario;

        setTitle("Banco de ADN");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
        lblTitulo.setIcon(new ImageIcon("dna_146c.gif"));

        getContentPane().setBackground(Color.BLACK);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.BLACK);

        // ================= CLIENTE =================
        if (rol.equals("CLIENTE")) {

            JTextField txtDescripcion = new JTextField(15);
            JTextField txtIdPerfil = new JTextField(5);

            JButton btnRegistrar = boton("Registrar Perfil");
            JButton btnConsultar = boton("Consultar Mi Perfil");
            JButton btnEditar = boton("Editar Perfil");

            panelBotones.add(new JLabel("ID Perfil:"));
            panelBotones.add(txtIdPerfil);
            panelBotones.add(new JLabel("Descripción:"));
            panelBotones.add(txtDescripcion);

            // 🔹 REGISTRAR
            btnRegistrar.addActionListener(e -> {
                String res = ClienteSocket.enviar(
                        "REGISTRAR," + idUsuario + "," + txtDescripcion.getText()
                );
                mostrar(res);
            });

            // 🔹 CONSULTAR
            btnConsultar.addActionListener(e -> {
                String res = ClienteSocket.enviar(
                        "CONSULTAR," + idUsuario
                );
                mostrar(res);
            });

            // 🔹 EDITAR
            btnEditar.addActionListener(e -> {

                String idPerfilTxt = txtIdPerfil.getText();
                String desc = txtDescripcion.getText();

                if (idPerfilTxt.isEmpty() || desc.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos");
                    return;
                }

                String res = ClienteSocket.enviar(
                        "EDITAR," + idPerfilTxt + "," + desc + ",activo"
                );

                mostrar(res);
            });

            panelBotones.add(btnRegistrar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEditar);
        }

        // ================= ADMIN =================
        else if (rol.equals("ADMIN")) {

            JButton btnListar = boton("Listar Perfiles");
            JButton btnConsultar = boton("Consultar Perfiles");
            JButton btnEliminar = boton("Eliminar Perfil");

            btnListar.addActionListener(e -> {
                mostrar(ClienteSocket.enviar("LISTAR"));
            });

            btnConsultar.addActionListener(e -> {
                mostrar(ClienteSocket.enviar("LISTAR"));
            });

            btnEliminar.addActionListener(e -> {
                String id = JOptionPane.showInputDialog(this, "ID del perfil:");
                if (id != null && !id.isEmpty()) {
                    mostrar(ClienteSocket.enviar("ELIMINAR," + id));
                }
            });

            panelBotones.add(btnListar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEliminar);
        }

        // 🔥 ÁREA DE SALIDA
        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setPreferredSize(new Dimension(115, 115));
        areaSalida.setBackground(Color.BLACK);
        areaSalida.setForeground(Color.WHITE);

        add(lblTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(new JScrollPane(areaSalida), BorderLayout.SOUTH);
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
    // BOTONES ESTILO
    // =========================
    private JButton boton(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);
        return b;
    }
}