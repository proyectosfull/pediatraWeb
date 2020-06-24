package com.mibebe.bean.util;

import com.mibebe.bean.Consulta;
import java.util.List;

/**
 *
 * @author Sergio Cabrera
 */
public class ResumenConsulta extends Consulta {
    private String parentesco;
    private List<String> archivos;

    public List<String> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<String> archivos) {
        this.archivos = archivos;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
