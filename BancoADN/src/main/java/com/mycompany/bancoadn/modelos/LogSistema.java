package com.mycompany.bancoadn.modelos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LogSistema implements Serializable {

    private String accion;
    private String usuario;
    private String descripcion;
    private LocalDateTime fecha;

    public LogSistema(String accion, String usuario, String descripcion) {

        this.accion = accion;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    // =========================
    // GETTERS
    // =========================

    public String getAccion() {
        return accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {

        return "[" + fecha + "] " + accion + " | " + usuario + " | " + descripcion;
    }
}