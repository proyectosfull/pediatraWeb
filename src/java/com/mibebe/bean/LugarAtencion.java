package com.mibebe.bean;

import java.io.Serializable;

/**
 *
 * @author Sergio Cabrera
 */
public class LugarAtencion implements Serializable {
    private int id;
    private String direccion;
    private double latitud;
    private double longitud;
    private String tipo;//Casa,oficina, etc

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
