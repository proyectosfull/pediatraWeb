package com.mibebe.bean;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sergio Cabrera
 */
@XmlRootElement(name="dependiente")
public class Dependiente implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private Integer edad;
    @XmlElement(name="fecha_nacimiento")
    private String fecha_acimiento;
    private String codigo;
    private String imagen;
    @XmlElement(name="fecha_registro")
    private String fecRegistro;
    private String notas;
    private Integer sexo;
    private String imagenVacuna;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public String getFecNacimiento() {
        return fecha_acimiento;
    }
    
    public void setFecNacimiento(String fecNacimiento) {
        this.fecha_acimiento = fecNacimiento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }   

    public String getImagenVacuna() {
        return imagenVacuna;
    }

    public void setImagenVacuna(String imagenVacuna) {
        this.imagenVacuna = imagenVacuna;
    }
    
    
    /*Otros*/
    @XmlTransient
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }
}
