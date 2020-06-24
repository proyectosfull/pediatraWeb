package com.mibebe.bean.util;

import com.mibebe.bean.Archivos;
import com.mibebe.bean.Consulta;
import java.util.List;

public class ConsultaCompleta extends Consulta {   
    private List<Archivos> archivos;

    public List<Archivos> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<Archivos> archivos) {
        this.archivos = archivos;
    }
    
    
}
