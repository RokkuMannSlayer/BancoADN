package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BancoADNUI extends JFrame {

    private BancoADN banco = new BancoADN();

    private JTextField txtId, txtNombre, txtDescripcion;
    private JTextArea areaSalida;

    public BancoADNUI() {
        setTitle("Banco de ADN");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de entrada
        JPanel panelInput = new JPanel(new GridLayout(3, 2));

        panelInput.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelInput.add(txtId);

        panelInput.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelInput.add(txtNombre);

        panelInput.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField();
        panelInput.add(txtDescripcion);

        add(panelInput, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnConsultar = new JButton("Consultar");
        JButton btnListar = new JButton("Listar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnListar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.CENTER);

        // Área de salida
        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        add(new JScrollPane(areaSalida), BorderLayout.SOUTH);

        // Eventos

        btnRegistrar.addActionListener(e -> {
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String desc = txtDescripcion.getText();

            if (banco.registrarPerfil(id, nombre, desc)) {
                areaSalida.setText("Perfil registrado correctamente");
            } else {
                areaSalida.setText("Error: ID duplicado");
            }
        });

        btnConsultar.addActionListener(e -> {
            String id = txtId.getText();
            PerfilADN perfil = banco.consultarPerfil(id);

            if (perfil != null) {
                areaSalida.setText(perfil.toString());
            } else {
                areaSalida.setText("Perfil no encontrado");
            }
        });

        btnListar.addActionListener(e -> {
            areaSalida.setText("");
            for (PerfilADN p : banco.getPerfiles().values()) {
                areaSalida.append(p.toString() + "\n");
            }
        });

        btnEliminar.addActionListener(e -> {
            String id = txtId.getText();

            if (banco.eliminarPerfil(id)) {
                areaSalida.setText("Perfil eliminado");
            } else {
                areaSalida.setText("No se pudo eliminar");
            }
        });
    }

}
