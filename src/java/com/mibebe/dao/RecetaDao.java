package com.mibebe.dao;

import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.util.RecetaTemplate;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaDao extends Dao {
    
    public void insert(RecetaTemplate receta) {
        try {
            openConnection();
            CONNECTION.setAutoCommit(false);
            String query = "INSERT INTO cat_recetas(nombre) VALUES(?);";                    
            PSTM = CONNECTION.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            PSTM.setString(1, receta.getNombre());
            if(PSTM.executeUpdate() != 1)
                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
            RS = PSTM.getGeneratedKeys();
            if(RS.next()) {
                receta.setId(RS.getInt(1));
                PSTM.close();
                RS.close();
            } else {
                 throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
            }
            
            if(receta.getDetalles() != null && receta.getDetalles().size() > 0) {
                query = "INSERT INTO cat_receta_detalle(medicamento, dosis, duracion, intervalo, receta_id) VALUES(?,?,?,?,?) ";
                PSTM = CONNECTION.prepareStatement(query);
                int i = 1;
                List<RecetaDetalle> detalles = receta.getDetalles();
                for(RecetaDetalle detalle : detalles) {
                    PSTM.setString(i++, detalle.getMedicamento());
                    PSTM.setString(i++, detalle.getDosis());
                    PSTM.setString(i++, detalle.getDuracion());
                    PSTM.setString(i++, detalle.getIntervalo());
                    PSTM.setInt(i++, receta.getId());
                    
                    PSTM.execute();
                    PSTM.clearParameters();
                    i = 1;                    
                }
            }
            CONNECTION.commit();
        } catch(NullPointerException | SQLException e) {
            try{CONNECTION.rollback();}catch(SQLException ex){handleException(ex);}
            handleException(e);  
        } finally {
            closeConnection();
        }
    }
    
    public List<RecetaTemplate> obtenerLista() {
        List<RecetaTemplate> recetas = new ArrayList();
        try {
            openConnection();
            String query = "SELECT id, nombre FROM cat_recetas;";                    
            PSTM = CONNECTION.prepareStatement(query);
            RS = PSTM.executeQuery();
            while(RS.next()) {
                RecetaTemplate receta = new RecetaTemplate();
                receta.setId(RS.getInt("id"));
                receta.setNombre(RS.getString("nombre"));
                recetas.add(receta);
            }           
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return recetas;
    }
    
    public List<RecetaTemplate> obtenerReceta(int id) {
        List<RecetaTemplate> recetas = new ArrayList();
        try {
            openConnection();
            String query = "SELECT id, nombre FROM cat_recetas where id=?;";                    
            PSTM = CONNECTION.prepareStatement(query);
             PSTM.setInt(1, id);
            RS = PSTM.executeQuery();
            while(RS.next()) {
                RecetaTemplate receta = new RecetaTemplate();
                receta.setId(RS.getInt("id"));
                receta.setNombre(RS.getString("nombre"));
                recetas.add(receta);
            }           
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return recetas;
    }
    
    public List<RecetaDetalle> obtenerLista(int recetaId) {
        List<RecetaDetalle> detalles = new ArrayList();
        try {
            openConnection();
            String query = "SELECT medicamento, dosis, duracion, intervalo FROM cat_recetas r\n" +
                    "JOIN cat_receta_detalle d ON r.id = d.receta_id WHERE id = ?;";                    
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, recetaId);
            RS = PSTM.executeQuery();
            while(RS.next()) {
                RecetaDetalle detalle = new RecetaDetalle();
                detalle.setMedicamento(RS.getString("medicamento"));
                detalle.setDosis(RS.getString("dosis"));
                detalle.setDuracion(RS.getString("duracion"));
                detalle.setIntervalo(RS.getString("intervalo"));
                detalles.add(detalle);
            }           
        } catch(NullPointerException | SQLException e) {
            handleException(e);  
        } finally {
            closeConnection();
        }
        return detalles;
    }
}
