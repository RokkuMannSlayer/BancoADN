package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

public class Sign{
    
    static JTextField txtUsuario;
    static JPasswordField txtPassword;
    static JButton btnBack;
    
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
            
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);
            
            JLabel iconTitle = new JLabel("", SwingConstants.CENTER);
            iconTitle.setIcon(new ImageIcon("C:\\Users\\facun\\OneDrive\\Documents\\GitHub\\BancoADN\\BancoADN\\dna_146c.gif"));
            
            login = new JButton("Iniciar Sesión");
            login.setBackground(Color.BLUE);
            login.setForeground(Color.WHITE);
            login.setFocusPainted(false);
            
            login.addActionListener((ActionEvent e) -> {
                this.dispose();
                new Login().setVisible(true);
            });
            
            register = new JButton("Registrar Perfíl");
            register.setBackground(Color.BLUE);
            register.setForeground(Color.WHITE);
            register.setFocusPainted(false);
            
            register.addActionListener((ActionEvent e) -> {
                this.dispose();
                new Register().setVisible(true);
            });
            
            exit = new JButton("Salir");
            exit.setBackground(Color.RED);
            exit.setForeground(Color.WHITE);
            exit.setFocusPainted(false);
            
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
    
    static class Login extends JFrame {
        
        // Nota: Agregar un menú que indique las opciones de "Login" y "Registrar Usuario"
        // SOLO SE PUEDEN REGISTRAR CLIENTES, LOS ADMINISTRADORES SERÁN PURA Y EXCLUSIVAMENTE CREADOS EN LA BASE DE DATOS
        
        private JButton btnLogin;
    
        public Login() {
        
            setTitle("Banco de ADN - Iniciar Sesión");
            setSize(700, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.BLACK);

            JLabel lblTitulo = new JLabel("Iniciar Sesión\n", SwingConstants.CENTER);
            lblTitulo.setIcon(new ImageIcon("C:\\Users\\facun\\OneDrive\\Documents\\GitHub\\BancoADN\\BancoADN\\dna_146c.gif"));
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
            btnLogin.setFocusPainted(false);

            btnLogin.addActionListener((ActionEvent e) -> autenticarLogin());
            
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
        
        private void autenticarLogin() {
        
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());
        
            if (usuario.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "Bienvenido "+ usuario);
            
                
                dispose();
            }
        }
    }
    
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
            titulo.setIcon(new ImageIcon("C:\\Users\\facun\\OneDrive\\Documents\\GitHub\\BancoADN\\BancoADN\\dna_146c.gif"));
            titulo.setForeground(Color.WHITE);
            titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
            
            JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));

            form.add(new JLabel("ID Cliente:"));
            txtId = new JTextField();
            form.add(txtId);

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
        
        
        private void registrarUsuario() {
            
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String dni = txtDni.getText();
            String mail = txtMail.getText();
            String pass = new String(txtPass.getPassword());
            
            if (id.isEmpty() || nombre.isEmpty() || dni.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
            }
            
            // HARD-CODE (simulación)
            JOptionPane.showMessageDialog(this,
                "Usuario registrado:\n\nID: " + id +
                "\nNombre: " + nombre +
                "\nDNI: " + dni +
                "\nMail: " + mail);
            
            // Volver al menú
            new signMenu().setVisible(true);
            dispose();
        }
        
        
    }
        
    
    
}
