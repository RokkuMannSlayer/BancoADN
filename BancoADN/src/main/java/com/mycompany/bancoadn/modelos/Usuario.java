package com.mycompany.bancoadn.modelos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private String dni;
    private String rol;

    // =========================
    // CONSTRUCTOR
    // =========================
    public Usuario(
            int idUsuario,
            String nombre,
            String email,
            String password,
            String dni,
            String rol
    ) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.rol = rol;
    }

    // =========================
    // GETTERS
    // =========================
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDni() {
        return dni;
    }

    public String getRol() {
        return rol;
    }

    // =========================
    // SETTERS
    // =========================
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}