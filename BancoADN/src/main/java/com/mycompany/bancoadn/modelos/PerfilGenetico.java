package com.mycompany.bancoadn.modelos;

import java.io.Serializable;

public class PerfilGenetico implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPerfil;

    private String fotoPerfil;

    private TipoSangre tipoSangre;

    private String colorOjos;

    private String colorPelo;

    private String tendenciaConductual;

    private double altura;

    private double peso;

    private double imc;

    private String estado;

    private int idCliente;

    private int idAdmin;

    public PerfilGenetico(
        int idPerfil,
        String fotoPerfil,
        TipoSangre tipoSangre,
        String colorOjos,
        String colorPelo,
        String tendenciaConductual,
        double altura,
        double peso,
        String estado,
        int idCliente,
        int idAdmin
    ) {

        this.idPerfil = idPerfil;
        this.fotoPerfil = fotoPerfil;
        this.tipoSangre = tipoSangre;
        this.colorOjos = colorOjos;
        this.colorPelo = colorPelo;
        this.tendenciaConductual = tendenciaConductual;
        this.altura = altura;
        this.peso = peso;

        recalcularIMC();

        this.estado = estado;
        this.idCliente = idCliente;
        this.idAdmin = idAdmin;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public TipoSangre getTipoSangre() {
        return tipoSangre;
    }

    public String getColorOjos() {
        return colorOjos;
    }

    public String getColorPelo() {
        return colorPelo;
    }

    public String getTendenciaConductual() {
        return tendenciaConductual;
    }

    public double getAltura() {
        return altura;
    }

    public double getPeso() {
        return peso;
    }

    public double getImc() {
        return imc;
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

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setTipoSangre(TipoSangre tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public void setColorOjos(String colorOjos) {
        this.colorOjos = colorOjos;
    }

    public void setColorPelo(String colorPelo) {
        this.colorPelo = colorPelo;
    }

    public void setTendenciaConductual(String tendenciaConductual) {
        this.tendenciaConductual = tendenciaConductual;
    }

    public void setAltura(double altura) {
        this.altura = altura;
        recalcularIMC();
    }

    public void setPeso(double peso) {
        this.peso = peso;
        recalcularIMC();
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    private void recalcularIMC() {

        if (altura > 0) {

            imc = peso / (altura * altura);

        } else {

            imc = 0;
        }
    }
}