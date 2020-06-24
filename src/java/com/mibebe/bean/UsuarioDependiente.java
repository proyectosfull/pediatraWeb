package com.mibebe.bean;

/**
 *
 * @author Sergio Cabrera
 */
public class UsuarioDependiente {
    private Usuario paciente;
    private Dependiente dependiente;
    private String fecRegistro;

    public Usuario getPaciente() {
        return paciente;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public Dependiente getDependiente() {
        return dependiente;
    }

    public void setDependiente(Dependiente dependiente) {
        this.dependiente = dependiente;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }
}
