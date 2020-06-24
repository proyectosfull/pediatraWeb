package com.mibebe.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joseraymundo
 */
@XmlRootElement(name="crecimiento")
public class Crecimiento {
    private int id;
    private double peso;
    private double talla;
    private double perimetroCraneal;
    private int dias;
    private String fechaRegistro;
    private Dependiente dependiente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTalla() {
        return talla;
    }

    public void setTalla(double talla) {
        this.talla = talla;
    }

    public double getPerimetroCraneal() {
        return perimetroCraneal;
    }

    public void setPerimetroCraneal(double perimetroCraneal) {
        this.perimetroCraneal = perimetroCraneal;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Dependiente getDependiente() {
        return dependiente;
    }

    public void setDependiente(Dependiente dependiente) {
        this.dependiente = dependiente;
    }
}
