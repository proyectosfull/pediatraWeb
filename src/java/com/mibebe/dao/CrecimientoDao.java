/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibebe.dao;

import com.mibebe.bean.Crecimiento;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Joseraymundo
 */
public class CrecimientoDao extends Dao{
    
    public void registrarCrecimiento(Crecimiento crecimiento){
        
        try{
            openConnection();
            String query = "CALL spInsertCrecimiento(?,?,?,?,?)";
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setDouble(i++, crecimiento.getPeso());
            PSTM.setDouble(i++, crecimiento.getTalla());
            PSTM.setDouble(i++, crecimiento.getPerimetroCraneal());            
            PSTM.setString(i++, crecimiento.getFechaRegistro());
            PSTM.setInt(i++, crecimiento.getDependiente().getId());
            
            RS = PSTM.executeQuery();
            if(RS.next()) {
                crecimiento.setId(RS.getInt("id"));
            }
        }catch(NullPointerException | SQLException e){
            handleException(e);
        }finally{
            closeConnection();
        }
    }
    
    public List<Crecimiento> obtenerLista(int dependienteId){
        List<Crecimiento> lista = new ArrayList();
        try{
            openConnection();
            String query = "SELECT idcrecimiento, peso, talla, perimetro_craneal, dias, fechaRegistro FROM crecimiento\n"
                    + "WHERE dependiente_id = ? ORDER BY fechaRegistro ASC";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, dependienteId);            
            RS = PSTM.executeQuery();
            while(RS.next()) {
                Crecimiento crecimiento = new Crecimiento();
                crecimiento.setId(RS.getInt("idcrecimiento"));
                crecimiento.setPeso(RS.getDouble("peso"));
                crecimiento.setTalla(RS.getDouble("talla"));
                crecimiento.setPerimetroCraneal(RS.getDouble("perimetro_craneal"));
                crecimiento.setDias(RS.getInt("dias"));
                crecimiento.setFechaRegistro(RS.getString("fechaRegistro"));
                lista.add(crecimiento);
            }
        }catch(NullPointerException | SQLException e){
            handleException(e);
        }finally{
            closeConnection();
        }
        return lista;
    }
}
