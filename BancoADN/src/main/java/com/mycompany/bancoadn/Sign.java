package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Sign {

    static JTextField txtUsuario;
    static JPasswordField txtPassword;
    static JButton btnBack;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new signMenu().setVisible(true));
    }

    // =========================
    // MENÚ PRINCIPAL
    // =========================
    static class signMenu extends JFrame {

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

            JButton login = botonAzul("Iniciar Sesión");
            JButton register = botonAzul("Registrar Perfil");
            JButton exit = botonRojo("Salir");

            login.addActionListener(e -> {
                dispose();
                new Login().setVisible(true);
            });

            register.addActionListener(e -> {
                dispose();
                new Register().setVisible(true);
            });

            exit.addActionListener(e -> System.exit(0));

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

        public Login() {

            setTitle("Banco de ADN - Iniciar Sesión");
            setSize(700, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);

            JLabel lblTitulo = new JLabel("", SwingConstants.CENTER);
            lblTitulo.setIcon(new ImageIcon("dna_146c.gif"));

            JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
            form.setOpaque(false);

            JLabel lblMail = new JLabel("Mail:");
            lblMail.setForeground(Color.WHITE);

            JLabel lblPass = new JLabel("Contraseña:");
            lblPass.setForeground(Color.WHITE);

            txtUsuario = new JTextField();
            txtPassword = new JPasswordField();

            form.add(lblMail);
            form.add(txtUsuario);
            form.add(lblPass);
            form.add(txtPassword);

            JButton btnLogin = botonAzul("Ingresar");

            btnLogin.addActionListener((ActionEvent e) -> autenticarLogin());

            btnBack = botonRojo("Regresar");
            btnBack.addActionListener(e -> {
                dispose();
                new signMenu().setVisible(true);
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            topPanel.add(btnBack, BorderLayout.WEST);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            centerPanel.add(lblTitulo, BorderLayout.NORTH);
            centerPanel.add(form, BorderLayout.SOUTH);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setOpaque(false);
            bottomPanel.add(btnLogin);

            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            add(panel);
        }

        private void autenticarLogin() {

            String usuario = txtUsuario.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            String respuesta = ClienteSocket.enviar(
                    "LOGIN," + usuario + "," + password
            );

            if (respuesta == null || respuesta.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, respuesta);
                return;
            }

            if (!respuesta.startsWith("OK")) {
                JOptionPane.showMessageDialog(this, respuesta);
                return;
            }

            String[] datos = respuesta.split(",");

            if (datos.length < 3) {
                JOptionPane.showMessageDialog(this, "Respuesta inválida");
                return;
            }

            String tipo = datos[1];
            int id = Integer.parseInt(datos[2]);

            new BancoADNUI(tipo, id).setVisible(true);
            dispose();
        }
    }

    // =========================
    // REGISTER
    // =========================
    static class Register extends JFrame {

        private JTextField txtNombre, txtDni, txtMail;
        private JPasswordField txtPass;

        public Register() {

            setTitle("Banco de ADN - Registrar Perfil");
            setSize(700, 540);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);

            JLabel titulo = new JLabel("", SwingConstants.CENTER);
            titulo.setIcon(new ImageIcon("dna_146c.gif"));

            JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
            form.setOpaque(false);

            txtNombre = new JTextField();
            txtDni = new JTextField();
            txtMail = new JTextField();
            txtPass = new JPasswordField();

            form.add(label("Nombre:"));
            form.add(txtNombre);

            form.add(label("DNI:"));
            form.add(txtDni);

            form.add(label("Mail:"));
            form.add(txtMail);

            form.add(label("Contraseña:"));
            form.add(txtPass);

            JButton btnRegistrar = botonAzul("Registrar");
            btnRegistrar.addActionListener(e -> registrarUsuario());

            JButton btnBack = botonRojo("Regresar");
            btnBack.addActionListener(e -> {
                dispose();
                new signMenu().setVisible(true);
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setOpaque(false);
            topPanel.add(btnBack, BorderLayout.WEST);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setOpaque(false);
            centerPanel.add(titulo, BorderLayout.NORTH);
            centerPanel.add(form, BorderLayout.SOUTH);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setOpaque(false);
            bottomPanel.add(btnRegistrar);

            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(centerPanel, BorderLayout.CENTER);
            panel.add(bottomPanel, BorderLayout.SOUTH);

            add(panel);
        }

        private void registrarUsuario() {

            String nombre = txtNombre.getText();
            String dni = txtDni.getText();
            String email = txtMail.getText();
            String password = new String(txtPass.getPassword());

            if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }

            String respuesta = ClienteSocket.enviar(
                    "REGISTRO," + nombre + "," + dni + "," + email + "," + password
            );

            if (respuesta == null || respuesta.startsWith("ERROR")) {
                JOptionPane.showMessageDialog(this, respuesta);
                return;
            }

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente");

            new signMenu().setVisible(true);
            dispose();
        }
    }

    // =========================
    // HELPERS
    // =========================
    private static JButton botonAzul(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);
        return b;
    }

    private static JButton botonRojo(String txt) {
        JButton b = new JButton(txt);
        b.setBackground(Color.RED);
        b.setForeground(Color.WHITE);
        return b;
    }

    private static JLabel label(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(Color.WHITE);
        return l;
    }
}