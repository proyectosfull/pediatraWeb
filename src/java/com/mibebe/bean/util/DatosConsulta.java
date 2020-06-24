package com.mibebe.bean.util;

import com.mibebe.bean.Consulta;
import java.util.List;

/**
 *
 * @author Sergio Cabrera
 */
public class DatosConsulta extends Consulta {
 
    private FileBase64 comprobantePago;
    private List<FileBase64> archivos;
    private List<String> nombreArchivos;
    private String nombreArchivoPago;
    private PaypalInfo paypal;

    public FileBase64 getComprobantePago() {
        return comprobantePago;
    }

    public void setComprobantePago(FileBase64 comprobantePago) {
        this.comprobantePago = comprobantePago;
    }

    public List<FileBase64> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<FileBase64> archivos) {
        this.archivos = archivos;
    }

    public List<String> getNombreArchivos() {
        return nombreArchivos;
    }

    public void setNombreArchivos(List<String> nombreArchivos) {
        this.nombreArchivos = nombreArchivos;
    }

    public String getNombreArchivoPago() {
        return nombreArchivoPago;
    }

    public void setNombreArchivoPago(String nombreArchivoPago) {
        this.nombreArchivoPago = nombreArchivoPago;
    }

    public PaypalInfo getPaypal() {
        return paypal;
    }

    public void setPaypal(PaypalInfo paypal) {
        this.paypal = paypal;
    }
    
    
    
}
