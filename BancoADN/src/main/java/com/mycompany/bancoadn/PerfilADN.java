package com.mycompany.bancoadn;

public class PerfilADN {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public PerfilADN(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void eliminar() {
        this.activo = false;
    }

    @Override
    public String toString() {
        return "ID: " + id +
               ", Nombre: " + nombre +
               ", Descripción: " + descripcion +
               ", Activo: " + activo;
    }
}
