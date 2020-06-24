package com.mibebe.bean.util;

import com.mibebe.bean.Dependiente;
import com.mibebe.bean.Vacuna;
import com.mibebe.bean.VacunaAplicacion;

public class VacunaDependiente {
    private Vacuna vacuna;
    private VacunaAplicacion aplicacion;
    private Dependiente dependiente;
    private String fecha;
    private int edad;

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }

    public VacunaAplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(VacunaAplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Dependiente getDependiente() {
        return dependiente;
    }

    public void setDependiente(Dependiente dependiente) {
        this.dependiente = dependiente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    
}
