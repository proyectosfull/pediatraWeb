package com.mibebe.bean;

import java.io.Serializable;

/**
 *
 * @author Sergio Cabrera
 */
public class Consulta implements Serializable {
    
    private long id;
    private Float temperatura;
    private Float peso;
    private String sintomas;
    private Boolean pagado;
    private Boolean atendido;
    private String fecRegistro;
    private String fecAtendido;
    private Usuario usuario;
    private Dependiente dependiente;
    private Pediatra pediatra;
    private Receta receta;
    private String notaPediatra;
    private Pago pago;
    private String recetaPdf;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Boolean isPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(Boolean atendido) {
        this.atendido = atendido;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public String getFecAtendido() {
        return fecAtendido;
    }

    public void setFecAtendido(String fecAtendido) {
        this.fecAtendido = fecAtendido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Dependiente getDependiente() {
        return dependiente;
    }

    public void setDependiente(Dependiente dependiente) {
        this.dependiente = dependiente;
    }

    public Pediatra getPediatra() {
        return pediatra;
    }

    public void setPediatra(Pediatra pediatra) {
        this.pediatra = pediatra;
    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Pago getPago() {
        return pago;
    }

    public String getNotaPediatra() {
        return notaPediatra;
    }

    public void setNotaPediatra(String notaPediatra) {
        this.notaPediatra = notaPediatra;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public String getRecetaPdf() {
        return recetaPdf;
    }

    public void setRecetaPdf(String recetaPdf) {
        this.recetaPdf = recetaPdf;
    }
    
            
}
