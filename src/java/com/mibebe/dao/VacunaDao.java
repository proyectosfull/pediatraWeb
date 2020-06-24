package com.mibebe.dao;

import com.mibebe.bean.Vacuna;
import com.mibebe.bean.VacunaAplicacion;
import com.mibebe.bean.util.VacunaDependiente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VacunaDao extends Dao {
    
    public void insert(VacunaDependiente vacunas) {
        try {
            openConnection();
            String query = "INSERT INTO vacunas_de_dependiente(vacuna_id, dependiente_id, vacuna_aplicacion_id, fechaAplicacion, edadAplicacion, vacuna)\n" +
                            "VALUES(?, ?, ?, ?, DATEDIFF(?, (SELECT nacimiento FROM dependiente WHERE iddependiente = ? LIMIT 1)), ?)";            
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, vacunas.getVacuna().getId());
            PSTM.setInt(2, vacunas.getDependiente().getId());
            PSTM.setInt(3, vacunas.getAplicacion().getId());
            PSTM.setString(4, vacunas.getFecha());
            PSTM.setString(5, vacunas.getFecha());
            PSTM.setInt(6, vacunas.getDependiente().getId());
            System.out.println(vacunas.getVacuna().getNombre());
            PSTM.setString(7, vacunas.getVacuna().getNombre());
            if(PSTM.executeUpdate() != 1)
                setErrorMessage("No se registró la vacuna");
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
    }
    
    public List<VacunaDependiente> getPorDependiente(int dependienteId) {
        List<VacunaDependiente> vacunas = new ArrayList();
        try {
            openConnection();
            String query = "SELECT v.id vacuna_id, IFNULL(vd.vacuna, v.nombre) AS vacuna, v.enfermedad, d.nombre, d.apellidos, va.aplicacion, vd.edadAplicacion, vd.fechaAplicacion\n" +
                    "FROM vacunas_de_dependiente vd\n" +
                    "LEFT JOIN vacunas v ON vd.vacuna_id = v.id\n" +
                    "LEFT JOIN dependiente d ON vd.dependiente_id = d.iddependiente\n" +
                    "LEFT JOIN vacuna_aplicacion va ON vd.vacuna_aplicacion_id = va.id\n" +
                    "WHERE vd.dependiente_id = ?\n" +
                    "ORDER BY vd.fechaAplicacion DESC";            
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, dependienteId);
            RS = PSTM.executeQuery();
            while(RS.next()) {
                VacunaDependiente vacunaDependiente = new VacunaDependiente();
                vacunaDependiente.setEdad(RS.getInt("edadAplicacion"));
                vacunaDependiente.setFecha(RS.getString("fechaAplicacion"));
                
                Vacuna vacuna = new Vacuna();
                vacuna.setId(RS.getInt("vacuna_id"));
                vacuna.setNombre(RS.getString("vacuna"));
                
                VacunaAplicacion aplicacion = new VacunaAplicacion();
                aplicacion.setAplicacion(RS.getString("aplicacion"));
                
                vacunaDependiente.setVacuna(vacuna);
                vacunaDependiente.setAplicacion(aplicacion);
                
                vacunas.add(vacunaDependiente);
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return vacunas;
    }
    
    public List<Vacuna> get() {
        List<Vacuna> vacunas = new ArrayList();
        try {
            openConnection();
            String query = "SELECT id, nombre, IFNULL(enfermedad, '-') enfermedad FROM vacunas";            
            PSTM = CONNECTION.prepareStatement(query);
            RS = PSTM.executeQuery();
            while(RS.next()) {                
                Vacuna vacuna = new Vacuna();
                vacuna.setId(RS.getInt("id"));
                vacuna.setNombre(RS.getString("nombre"));
                vacuna.setEnfermedad(RS.getString("enfermedad"));
                
                vacunas.add(vacuna);
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return vacunas;
    }
    
    public List<VacunaAplicacion> getTipoAplicaciones() {
        List<VacunaAplicacion> aplicaciones = new ArrayList();
        try {
            openConnection();
            String query = "SELECT id, aplicacion FROM vacuna_aplicacion";            
            PSTM = CONNECTION.prepareStatement(query);
            RS = PSTM.executeQuery();
            while(RS.next()) {                
                VacunaAplicacion vacuna = new VacunaAplicacion();
                vacuna.setId(RS.getInt("id"));
                vacuna.setAplicacion(RS.getString("aplicacion"));
                
                aplicaciones.add(vacuna);
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return aplicaciones;
    }
}
