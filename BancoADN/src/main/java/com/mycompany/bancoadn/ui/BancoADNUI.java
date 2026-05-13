package com.mycompany.bancoadn.ui;

import com.mycompany.bancoadn.red.ClienteSocket;
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

            JLabel lblId = new JLabel("ID Perfil:");
            lblId.setForeground(Color.WHITE);

            JLabel lblDesc = new JLabel("Descripción:");
            lblDesc.setForeground(Color.WHITE);

            panelBotones.add(lblId);
            panelBotones.add(txtIdPerfil);

            panelBotones.add(lblDesc);
            panelBotones.add(txtDescripcion);

            // 🔹 REGISTRAR
            btnRegistrar.addActionListener(e -> {

                String descripcion = txtDescripcion.getText().trim();

                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Ingrese una descripción");
                    return;
                }

                String res = ClienteSocket.enviar(
                        "REGISTRAR," + idUsuario + "," + descripcion
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

                String idPerfilTxt = txtIdPerfil.getText().trim();
                String desc = txtDescripcion.getText().trim();

                if (idPerfilTxt.isEmpty() || desc.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Complete todos los campos");
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

            // 🔹 LISTAR
            btnListar.addActionListener(e -> {

                String res = ClienteSocket.enviar("LISTAR");

                mostrar(res);
            });

            // 🔹 CONSULTAR
            btnConsultar.addActionListener(e -> {

                String res = ClienteSocket.enviar("CONSULTAR_TODOS");

                mostrar(res);
            });

            // 🔹 ELIMINAR
            btnEliminar.addActionListener(e -> {

                String id = JOptionPane.showInputDialog(
                        this,
                        "ID del perfil:"
                );

                if (id == null || id.trim().isEmpty()) {
                    return;
                }

                String res = ClienteSocket.enviar(
                        "ELIMINAR," + id
                );

                mostrar(res);
            });

            panelBotones.add(btnListar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEliminar);
        }

        // =========================
        // ÁREA DE SALIDA
        // =========================
        areaSalida = new JTextArea();

        areaSalida.setEditable(false);
        areaSalida.setPreferredSize(new Dimension(115, 115));
        areaSalida.setBackground(Color.BLACK);
        areaSalida.setForeground(Color.WHITE);
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));

        add(lblTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(new JScrollPane(areaSalida), BorderLayout.SOUTH);
    }

    // =========================
    // MOSTRAR RESPUESTA
    // =========================
    private void mostrar(String res) {

        if (res == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Sin respuesta del servidor"
            );

            return;
        }

        if (res.startsWith("ERROR")) {

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