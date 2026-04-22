package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// JDBC
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public class Sign {

    static JTextField txtUsuario;
    static JPasswordField txtPassword;
    static JButton btnBack;

    // 🔥 MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new signMenu().setVisible(true);
        });
    }

    // =========================
    // MENÚ PRINCIPAL
    // =========================
    static class signMenu extends JFrame {

        private JButton login;
        private JButton register;
        private JButton exit;

        public signMenu() {

            setTitle("Banco de ADN - Inicio");
            setSize(700, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);

            JLabel iconTitle = new JLabel("", SwingConstants.CENTER);
            iconTitle.setIcon(new ImageIcon("dna_146c.gif"));

            login = new JButton("Iniciar Sesión");
            login.setBackground(Color.BLUE);
            login.setForeground(Color.WHITE);

            login.addActionListener((ActionEvent e) -> {
                this.dispose();
                new Login().setVisible(true);
            });

            register = new JButton("Registrar Perfíl");
            register.setBackground(Color.BLUE);
            register.setForeground(Color.WHITE);
            
            register.addActionListener((ActionEvent e) -> {
                this.dispose();
                new Register().setVisible(true);
            });

            exit = new JButton("Salir");
            exit.setBackground(Color.RED);
            exit.setForeground(Color.WHITE);

            exit.addActionListener((ActionEvent e) -> System.exit(0));

            JPanel btnPanel = new JPanel(new BorderLayout());
            btnPanel.setOpaque(false);

            btnPanel.add(login, BorderLayout.NORTH);
            btnPanel.add(register, BorderLayout.CENTER);
            btnPanel.add(exit, BorderLayout.SOUTH);

            panel.add(iconTitle, BorderLayout.NORTH);
            panel.add(btnPanel, BorderLayout.CENTER);

            add(panel);
        }
    }

    // =========================
    // LOGIN
    // =========================
    static class Login extends JFrame {

        private JButton btnLogin;

        public Login() {

            setTitle("Banco de ADN - Iniciar Sesión");
            setSize(700, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);

            JLabel lblTitulo = new JLabel("Iniciar Sesión\n", SwingConstants.CENTER);
            lblTitulo.setIcon(new ImageIcon("dna_146c.gif"));
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTitulo.setForeground(Color.WHITE);

            JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));

            form.add(new JLabel("Usuario:"));
            txtUsuario = new JTextField();
            form.add(txtUsuario);

            form.add(new JLabel("Contraseña:"));
            txtPassword = new JPasswordField();
            form.add(txtPassword);

            btnLogin = new JButton("Ingresar");
            btnLogin.setBackground(Color.BLUE);
            btnLogin.setForeground(Color.WHITE);

            btnLogin.addActionListener((ActionEvent e) -> autenticarLogin());

            btnBack = new JButton("Regresar");
            btnBack.setBackground(Color.RED);
            btnBack.setForeground(Color.WHITE);

            btnBack.addActionListener((ActionEvent e) -> {
                this.dispose();
                new signMenu().setVisible(true);
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);

            JPanel ttlPanel = new JPanel(new BorderLayout());
            ttlPanel.setOpaque(false);

            topPanel.add(btnBack, BorderLayout.WEST);
            ttlPanel.add(lblTitulo, BorderLayout.NORTH);

            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(ttlPanel, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            bottomPanel.add(form, BorderLayout.NORTH);
            bottomPanel.add(btnLogin, BorderLayout.SOUTH);

            add(panel);
        }

        // 🔐 LOGIN USANDO STORED PROCEDURE
        private void autenticarLogin() {

            if (!ConexionInternet.hayInternet()) {
                JOptionPane.showMessageDialog(this, "Sin conexión a Internet");
                return;
            }

            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            String respuesta = ClienteSocket.enviar("LOGIN," + usuario + "," + password);

            if (respuesta.startsWith("OK")) {

                String[] datos = respuesta.split(",");
                String tipo = datos[1];
                int id = Integer.parseInt(datos[2]);

                new BancoADNUI(tipo, id).setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Error: " + respuesta);
            }
        }
    }

    // =========================
    // REGISTER (NO IMPLEMENTADO)
    // =========================
    static class Register extends JFrame {
        
        private JTextField txtId, txtNombre, txtDni, txtMail;
        private JPasswordField txtPass;
        private JButton btnRegistrar, btnBack;
        
        public Register() {
            
            setTitle("Banco de ADN - Registrar Perfil");
            setSize(700, 540);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);
            
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);
            
            JLabel titulo = new JLabel("Registrar Perfil", SwingConstants.CENTER);
            titulo.setIcon(new ImageIcon("dna_146c.gif"));
            titulo.setForeground(Color.WHITE);
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            
            JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

            form.add(new JLabel("Nombre:"));
            txtNombre = new JTextField();
            form.add(txtNombre);

            form.add(new JLabel("DNI:"));
            txtDni = new JTextField();
            form.add(txtDni);

            form.add(new JLabel("Mail:"));
            txtMail = new JTextField();
            form.add(txtMail);

            form.add(new JLabel("Contraseña:"));
            txtPass = new JPasswordField();
            form.add(txtPass);

            btnRegistrar = new JButton("Registrar");
            btnRegistrar.setBackground(Color.BLUE);
            btnRegistrar.setForeground(Color.WHITE);
            btnRegistrar.setFocusPainted(false);

            btnRegistrar.addActionListener((ActionEvent e) -> registrarUsuario());

            btnBack = new JButton("Regresar");
            btnBack.setBackground(Color.RED);
            btnBack.setForeground(Color.WHITE);
            btnBack.setFocusPainted(false);
            
            btnBack.addActionListener((ActionEvent e) -> {
            this.dispose();
            new signMenu().setVisible(true);
            });
            
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            
            topPanel.add(btnBack, BorderLayout.WEST);
            
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            centerPanel.add(titulo, BorderLayout.NORTH);
            centerPanel.add(form, BorderLayout.SOUTH);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            
            bottomPanel.add(btnRegistrar, BorderLayout.SOUTH);
            
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            add(panel);
        }
        
        //Modificar este void para Stored Procedure
        private void registrarUsuario() {
            
            
            new signMenu().setVisible(true);
            dispose();
        }
    }
}