package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class BancoADNUI extends JFrame {

    private BancoADN banco = new BancoADN();
    private JTextArea areaSalida;

    private String rol;
    private int idUsuario;

    public BancoADNUI(String rol, int idUsuario) {

        this.rol = rol;
        this.idUsuario = idUsuario;

        setTitle("Banco de ADN");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.BLACK);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.BLACK);

        if (rol.equals("CLIENTE")) {

            JTextField txtDescripcion = new JTextField(20);

            JButton btnRegistrar = boton("Registrar Perfil");
            JButton btnConsultar = boton("Consultar Mi Perfil");

            panelBotones.add(txtDescripcion);

            btnRegistrar.addActionListener(e -> {
                areaSalida.setText(
                        banco.registrarPerfil(idUsuario, txtDescripcion.getText())
                );
            });

            btnConsultar.addActionListener(e -> {
                areaSalida.setText(
                        banco.consultarPerfilCliente(idUsuario)
                );
            });

            panelBotones.add(btnRegistrar);
            panelBotones.add(btnConsultar);

        } else {

            JButton btnListar = boton("Listar Perfiles");
            JButton btnConsultar = boton("Consultar Perfiles");
            JButton btnEliminar = boton("Eliminar Perfiles");

            // 🔹 LISTAR (solo tabla)
            btnListar.addActionListener(e
                    -> abrirTablaSimple(banco.listarPerfiles(), "Lista de Perfiles")
            );

            // 🔹 CONSULTAR (tabla completa)
            btnConsultar.addActionListener(e
                    -> abrirTablaSimple(banco.consultarTodosPerfiles(), "Consulta de Perfiles")
            );

            // 🔴 ELIMINAR (ventana separada)
            btnEliminar.addActionListener(e
                    -> abrirVentanaEliminar()
            );

            panelBotones.add(btnListar);
            panelBotones.add(btnConsultar);
            panelBotones.add(btnEliminar);
        }

        add(panelBotones, BorderLayout.CENTER);

        areaSalida = new JTextArea();
        areaSalida.setBackground(Color.BLACK);
        areaSalida.setForeground(Color.WHITE);

        add(new JScrollPane(areaSalida), BorderLayout.SOUTH);
    }

    // =========================
    // TABLA SIMPLE (SIN ACCIONES)
    // =========================
    private void abrirTablaSimple(ResultSet rs, String titulo) {

        try {
            String[] columnas = {"ID", "Cliente", "DNI", "Descripción", "Estado", "Admin"};

            javax.swing.table.DefaultTableModel modelo
                    = new javax.swing.table.DefaultTableModel(columnas, 0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("IDperfil"),
                    rs.getString("Nombre_cliente"),
                    rs.getString("DNI_cliente"),
                    rs.getString("Descripcion"),
                    rs.getString("Estado"),
                    rs.getString("Nombre_admin")
                };
                modelo.addRow(fila);
            }

            JTable tabla = new JTable(modelo);
            tabla.setBackground(Color.BLACK);
            tabla.setForeground(Color.WHITE);

            JScrollPane scroll = new JScrollPane(tabla);

            JFrame ventana = new JFrame(titulo);
            ventana.setSize(700, 400);
            ventana.add(scroll);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

        } catch (Exception e) {
            areaSalida.setText("Error al mostrar datos: " + e.getMessage());
        }
    }

    // =========================
    // VENTANA ELIMINAR
    // =========================
    private void abrirVentanaEliminar() {

        try {
            ResultSet rs = banco.listarPerfiles();

            String[] columnas = {"ID", "Cliente", "DNI", "Descripción", "Estado", "Admin"};

            javax.swing.table.DefaultTableModel modelo
                    = new javax.swing.table.DefaultTableModel(columnas, 0);

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("IDperfil"),
                    rs.getString("Nombre_cliente"),
                    rs.getString("DNI_cliente"),
                    rs.getString("Descripcion"),
                    rs.getString("Estado"),
                    rs.getString("Nombre_admin")
                };
                modelo.addRow(fila);
            }

            JTable tabla = new JTable(modelo);
            tabla.setBackground(Color.BLACK);
            tabla.setForeground(Color.WHITE);

            JScrollPane scroll = new JScrollPane(tabla);

            JButton btnEliminar = new JButton("Eliminar Seleccionado");
            btnEliminar.setBackground(Color.RED);
            btnEliminar.setForeground(Color.WHITE);

            btnEliminar.addActionListener(e -> {

                int fila = tabla.getSelectedRow();

                if (fila == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un perfil");
                    return;
                }

                int idPerfil = (int) tabla.getValueAt(fila, 0);

                String res = banco.eliminarPerfil(idPerfil);

                JOptionPane.showMessageDialog(null, res);

                // eliminar de la tabla visual
                ((javax.swing.table.DefaultTableModel) tabla.getModel()).removeRow(fila);
            });

            JFrame ventana = new JFrame("Eliminar Perfiles");
            ventana.setSize(800, 400);
            ventana.setLayout(new BorderLayout());

            ventana.add(scroll, BorderLayout.CENTER);
            ventana.add(btnEliminar, BorderLayout.SOUTH);

            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

        } catch (Exception e) {
            areaSalida.setText("Error al abrir eliminación: " + e.getMessage());
        }
    }

    private JButton boton(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);
        return b;
    }
}