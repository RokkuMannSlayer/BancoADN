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
            lblTitulo.setForeground(Color.RED);

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

            panel.add(lblTitulo, BorderLayout.NORTH);
            panel.add(form, BorderLayout.CENTER);
            panel.add(btnLogin, BorderLayout.SOUTH);

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
