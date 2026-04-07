package com.mycompany.bancoadn;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;

public class Sign{
    
    static class Login extends JFrame {
        
        // Nota: Agregar un menú que indique las opciones de "Login" y "Registrar Usuario"
        // SOLO SE PUEDEN REGISTRAR CLIENTES, LOS ADMINISTRADORES SERÁN PURA Y EXCLUSIVAMENTE CREADOS EN LA BASE DE DATOS
        
        private JTextField txtUsuario;
        private JPasswordField txtPassword;
        private JButton btnLogin;
        private JButton btnBack;
    
        public Login() {
        
            setTitle("Banco de ADN - Iniciar Sesión");
            setSize(700, 480);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setResizable(false);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panel.setBackground(Color.GRAY);

            JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
            lblTitulo.setIcon(new ImageIcon("C:\\Users\\facun\\OneDrive\\Documents\\GitHub\\BancoADN\\BancoADN\\DNA.png"));
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
            btnLogin.setForeground(Color.BLACK);
            btnLogin.setFocusPainted(false);

            btnLogin.addActionListener((ActionEvent e) -> autenticarLogin());
            
            btnBack = new JButton("Regresar");
            btnBack.setBackground(Color.RED);
            btnBack.setForeground(Color.BLACK);
            btnBack.setFocusPainted(false);
            
            btnBack.addActionListener((ActionEvent e) -> this.setVisible(false));
            
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
            
                new BancoADNUI().setVisible(true);
                dispose();
            }
        }
    }
    
    
        
    
    
}
