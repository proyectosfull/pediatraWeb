package com.mibebe.bean;

/**
 *
 * @author Sergio Cabrera
 */
public class RecetaDetalle {
    private String medicamento;
    private String dosis;
    private String duracion;
    private String intervalo;
    private int receta_id;

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(String intervalo) {
        this.intervalo = intervalo;
    }

    public int getReceta_id() {
        return receta_id;
    }

    public void setReceta_id(int receta_id) {
        this.receta_id = receta_id;
    }
    
}
