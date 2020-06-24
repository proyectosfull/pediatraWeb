package com.mibebe.bean;

import com.mibebe.util.PasswordHash;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sergio Cabrera
 */
@XmlRootElement(name="pediatra")
public class Pediatra implements Serializable {
    private int id;
    private String correo;
    @XmlTransient
    private String password;
    private String nombre;
    private String apellidos;
    private String cedula;
    private String cedulaEspecialidad;
    private String cmcp;
    private String lugarEstudios;
    private LugarAtencion lugarAtencion;
    private boolean disponible;
    private Boolean sesionIniciada;
    private Double tarifa;
    private String FCMToken;
    private String imagen;
    private String fecRegistro;
    private String salt = null;
    private String jwtControl;
    private Especialidad especialidad;
    private Boolean IOS;
    private Boolean aprobado;
    private String firma;
    private String curp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        try {
            if(this.salt != null)
                return PasswordHash.hash(password, salt);
            
            String[] data = PasswordHash.hash(password);
            this.salt = data[0];
            return data[1];
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {}
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCedulaEspecialidad() {
        return cedulaEspecialidad;
    }

    public void setCedulaEspecialidad(String cedulaEspecialidad) {
        this.cedulaEspecialidad = cedulaEspecialidad;
    }

    public String getCmcp() {
        return cmcp;
    }

    public void setCmcp(String cmcp) {
        this.cmcp = cmcp;
    }

    public String getLugarEstudios() {
        return lugarEstudios;
    }

    public void setLugarEstudios(String lugarEstudios) {
        this.lugarEstudios = lugarEstudios;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public LugarAtencion getLugarAtencion() {
        return lugarAtencion;
    }

    public void setLugarAtencion(LugarAtencion lugarAtencion) {
        this.lugarAtencion = lugarAtencion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Boolean isSesionIniciada() {
        return sesionIniciada;
    }

    public void setSesionIniciada(Boolean sesionIniciada) {
        this.sesionIniciada = sesionIniciada;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }
    
    /*Otros*/
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getSalt() {
        return this.salt;
    }

    public String getJwtControl() {
        return jwtControl;
    }

    public void setJwtControl(String jwtControl) {
        this.jwtControl = jwtControl;
    }

    public Boolean isIOS() {
        return IOS;
    }

    public void setIOS(Boolean IOS) {
        this.IOS = IOS;
    }
    
    public Boolean getAprobado() {
        return aprobado;
    }

    public void setAprobado(Boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }
    
}
