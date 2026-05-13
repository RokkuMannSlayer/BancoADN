package com.mycompany.bancoadn;

import com.mycompany.bancoadn.red.ClienteSocket;
import com.mycompany.bancoadn.ui.BancoADNUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Sign {

    static JTextField txtUsuario;
    static JPasswordField txtPassword;
    static JButton btnBack;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new signMenu().setVisible(true)
        );
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
            panel.setBorder(
                    BorderFactory.createEmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            );

            panel.setBackground(Color.BLACK);

            JLabel iconTitle =
                    new JLabel(
                            "",
                            SwingConstants.CENTER
                    );

            iconTitle.setIcon(
                    new ImageIcon("dna_146c.gif")
            );

            JButton login =
                    botonAzul("Iniciar Sesión");

            JButton register =
                    botonAzul("Registrar Perfil");

            JButton exit =
                    botonRojo("Salir");

            // LOGIN
            login.addActionListener(e -> {

                dispose();

                new Login().setVisible(true);
            });

            // REGISTER
            register.addActionListener(e -> {

                dispose();

                new Register().setVisible(true);
            });

            // EXIT
            exit.addActionListener(
                    e -> System.exit(0)
            );

            JPanel btnPanel =
                    new JPanel(new BorderLayout());

            btnPanel.setOpaque(false);

            btnPanel.add(
                    login,
                    BorderLayout.NORTH
            );

            btnPanel.add(
                    register,
                    BorderLayout.CENTER
            );

            btnPanel.add(
                    exit,
                    BorderLayout.SOUTH
            );

            panel.add(
                    iconTitle,
                    BorderLayout.NORTH
            );

            panel.add(
                    btnPanel,
                    BorderLayout.CENTER
            );

            add(panel);
        }
    }

    // =========================
    // LOGIN
    // =========================
    static class Login extends JFrame {

        public Login() {

            setTitle(
                    "Banco de ADN - Iniciar Sesión"
            );

            setSize(700, 480);

            setLocationRelativeTo(null);

            setDefaultCloseOperation(
                    EXIT_ON_CLOSE
            );

            setResizable(false);

            JPanel panel =
                    new JPanel(new BorderLayout());

            panel.setBorder(
                    BorderFactory.createEmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            );

            panel.setBackground(Color.BLACK);

            JLabel lblTitulo =
                    new JLabel(
                            "",
                            SwingConstants.CENTER
                    );

            lblTitulo.setIcon(
                    new ImageIcon("dna_146c.gif")
            );

            JPanel form =
                    new JPanel(
                            new GridLayout(
                                    2,
                                    2,
                                    10,
                                    10
                            )
                    );

            form.setOpaque(false);

            JLabel lblMail =
                    label("Mail:");

            JLabel lblPass =
                    label("Contraseña:");

            txtUsuario = new JTextField();

            txtPassword = new JPasswordField();

            form.add(lblMail);
            form.add(txtUsuario);

            form.add(lblPass);
            form.add(txtPassword);

            JButton btnLogin =
                    botonAzul("Ingresar");

            btnLogin.addActionListener(
                    (ActionEvent e) ->
                            autenticarLogin()
            );

            btnBack =
                    botonRojo("Regresar");

            btnBack.addActionListener(e -> {

                dispose();

                new signMenu().setVisible(true);
            });

            JPanel topPanel =
                    new JPanel(new BorderLayout());

            topPanel.setOpaque(false);

            topPanel.add(
                    btnBack,
                    BorderLayout.WEST
            );

            JPanel centerPanel =
                    new JPanel(new BorderLayout());

            centerPanel.setOpaque(false);

            centerPanel.add(
                    lblTitulo,
                    BorderLayout.NORTH
            );

            centerPanel.add(
                    form,
                    BorderLayout.SOUTH
            );

            JPanel bottomPanel =
                    new JPanel();

            bottomPanel.setOpaque(false);

            bottomPanel.add(btnLogin);

            panel.add(
                    topPanel,
                    BorderLayout.NORTH
            );

            panel.add(
                    centerPanel,
                    BorderLayout.CENTER
            );

            panel.add(
                    bottomPanel,
                    BorderLayout.SOUTH
            );

            add(panel);
        }

        // =========================
        // LOGIN REMOTO
        // =========================
        private void autenticarLogin() {

            String usuario =
                    txtUsuario
                            .getText()
                            .trim();

            String password =
                    new String(
                            txtPassword.getPassword()
                    ).trim();

            // VALIDAR
            if (usuario.isEmpty()
                    ||
                password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete todos los campos"
                );

                return;
            }

            // SOCKET
            String respuesta =
                    ClienteSocket.enviar(
                            "LOGIN,"
                            + usuario
                            + ","
                            + password
                    );

            // ERROR
            if (respuesta == null
                    ||
                respuesta.startsWith("ERROR")) {

                JOptionPane.showMessageDialog(
                        this,
                        respuesta
                );

                return;
            }

            // VALIDAR RESPUESTA
            if (!respuesta.startsWith("OK")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Credenciales incorrectas"
                );

                return;
            }

            try {

                String[] datos =
                        respuesta.split(",");

                String tipo = datos[1];

                int id =
                        Integer.parseInt(
                                datos[2]
                        );

                // ABRIR UI
                new BancoADNUI(
                        tipo,
                        id
                ).setVisible(true);

                dispose();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Respuesta inválida"
                );
            }
        }
    }

    // =========================
    // REGISTER
    // =========================
    static class Register extends JFrame {

        private JTextField txtNombre;
        private JTextField txtDni;
        private JTextField txtMail;

        private JPasswordField txtPass;

        public Register() {

            setTitle(
                    "Banco de ADN - Registrar Perfil"
            );

            setSize(700, 540);

            setLocationRelativeTo(null);

            setDefaultCloseOperation(
                    EXIT_ON_CLOSE
            );

            setResizable(false);

            JPanel panel =
                    new JPanel(new BorderLayout());

            panel.setBorder(
                    BorderFactory.createEmptyBorder(
                            20,
                            20,
                            20,
                            20
                    )
            );

            panel.setBackground(Color.BLACK);

            JLabel titulo =
                    new JLabel(
                            "",
                            SwingConstants.CENTER
                    );

            titulo.setIcon(
                    new ImageIcon("dna_146c.gif")
            );

            JPanel form =
                    new JPanel(
                            new GridLayout(
                                    4,
                                    2,
                                    10,
                                    10
                            )
                    );

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

            JButton btnRegistrar =
                    botonAzul("Registrar");

            btnRegistrar.addActionListener(
                    e -> registrarUsuario()
            );

            JButton btnBack =
                    botonRojo("Regresar");

            btnBack.addActionListener(e -> {

                dispose();

                new signMenu().setVisible(true);
            });

            JPanel topPanel =
                    new JPanel(new BorderLayout());

            topPanel.setOpaque(false);

            topPanel.add(
                    btnBack,
                    BorderLayout.WEST
            );

            JPanel centerPanel =
                    new JPanel(new BorderLayout());

            centerPanel.setOpaque(false);

            centerPanel.add(
                    titulo,
                    BorderLayout.NORTH
            );

            centerPanel.add(
                    form,
                    BorderLayout.SOUTH
            );

            JPanel bottomPanel =
                    new JPanel();

            bottomPanel.setOpaque(false);

            bottomPanel.add(btnRegistrar);

            panel.add(
                    topPanel,
                    BorderLayout.NORTH
            );

            panel.add(
                    centerPanel,
                    BorderLayout.CENTER
            );

            panel.add(
                    bottomPanel,
                    BorderLayout.SOUTH
            );

            add(panel);
        }

        // =========================
        // REGISTRAR
        // =========================
        private void registrarUsuario() {

            String nombre =
                    txtNombre.getText().trim();

            String dni =
                    txtDni.getText().trim();

            String email =
                    txtMail.getText().trim();

            String password =
                    new String(
                            txtPass.getPassword()
                    ).trim();

            // VALIDAR
            if (nombre.isEmpty()
                    ||
                dni.isEmpty()
                    ||
                email.isEmpty()
                    ||
                password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Complete todos los campos"
                );

                return;
            }

            // SOCKET
            String respuesta =
                    ClienteSocket.enviar(
                            "REGISTRO,"
                            + nombre
                            + ","
                            + dni
                            + ","
                            + email
                            + ","
                            + password
                    );

            // ERROR
            if (respuesta == null
                    ||
                respuesta.startsWith("ERROR")) {

                JOptionPane.showMessageDialog(
                        this,
                        respuesta
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente"
            );

            new signMenu().setVisible(true);

            dispose();
        }
    }

    // =========================
    // HELPERS
    // =========================
    private static JButton botonAzul(String txt) {

        JButton b =
                new JButton(txt);

        b.setBackground(Color.BLUE);
        b.setForeground(Color.WHITE);

        return b;
    }

    private static JButton botonRojo(String txt) {

        JButton b =
                new JButton(txt);

        b.setBackground(Color.RED);
        b.setForeground(Color.WHITE);

        return b;
    }

    private static JLabel label(String txt) {

        JLabel l =
                new JLabel(txt);

        l.setForeground(Color.WHITE);

        return l;
    }
}