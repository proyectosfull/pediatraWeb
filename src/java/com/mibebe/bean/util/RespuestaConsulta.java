package com.mibebe.bean.util;

import com.mibebe.bean.Consulta;
import com.mibebe.bean.Receta;
import com.mibebe.bean.Usuario;

public class RespuestaConsulta extends Receta {
    private String notas;
    private Usuario usuario;
    private Consulta consulta;

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
    
}
