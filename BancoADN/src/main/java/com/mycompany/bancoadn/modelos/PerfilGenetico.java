package com.mycompany.bancoadn.modelos;

import java.io.Serializable;

public class PerfilGenetico implements Serializable {

    private int idPerfil;
    private String descripcion;
    private String estado;

    private int idCliente;
    private int idAdmin;

    public PerfilGenetico(int idPerfil,
                          String descripcion,
                          String estado,
                          int idCliente,
                          int idAdmin) {

        this.idPerfil = idPerfil;
        this.descripcion = descripcion;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idAdmin = idAdmin;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    @Override
    public String toString() {
        return idPerfil + ";" +
               descripcion + ";" +
               estado + ";" +
               idCliente + ";" +
               idAdmin;
    }
}