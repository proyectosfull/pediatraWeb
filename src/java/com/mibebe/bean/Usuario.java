package com.mibebe.bean;

import com.mibebe.util.PasswordHash;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sergio Cabrera
 */
@XmlRootElement(name="usuario")
public class Usuario implements Serializable {
    private int id;
    private String correo;
    @XmlTransient
    private String password;
    private String nombre;
    private String apellidos;
    private Integer edad;
    @XmlElement(name="fecha_nacimiento")
    private String fecNacimiento;
    @XmlTransient
    private Boolean sesion;
    @XmlElement(name="fcm_token")
    private String FCMToken;
    private String imagen;
    @XmlElement(name="fecha_registro")
    private String fecRegistro;
    @XmlTransient
    private String salt;
    @XmlTransient
    private String jwtControl;
    private Boolean IOS;
    
    public Usuario(){}
    
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public boolean isSesion() {
        return sesion;
    }

    public void setSesion(boolean sesion) {
        this.sesion = sesion;
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

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
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
}
