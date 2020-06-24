package com.mibebe.bean;

import java.util.List;

/**
 *
 * @author Sergio Cabrera
 */
public class Receta {
    private int id;
    private String qrcode;
    private List<RecetaDetalle> detalles;
    private Boolean leido;
    private String fecRegistro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
    
    public List<RecetaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<RecetaDetalle> detalles) {
        this.detalles = detalles;
    }

    public Boolean isLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }
    
    
}
