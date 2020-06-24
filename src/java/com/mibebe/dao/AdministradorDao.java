/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibebe.dao;

import com.mibebe.bean.Administrador;
import com.mibebe.bean.Especialidad;
import com.mibebe.bean.LugarAtencion;
import com.mibebe.bean.Pediatra;
import com.mibebe.bean.Receta;
import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.util.RecetaTemplate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author demetryo
 */
public class AdministradorDao extends Dao {

    public Administrador login(Administrador administrador) {
        Administrador admin = null;
        try {
            openConnection();
            String query = "select * from administrador where usuario=? and contraseña=MD5(?);";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, administrador.getUsuario());
            PSTM.setString(2, administrador.getPassword());
            RS = PSTM.executeQuery();

            if (RS.next()) {

                admin = new Administrador();
                admin.setUsuario(RS.getString("usuario"));
                admin.setId(RS.getInt("id"));

            }
            closeConnection();
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());

        }
        return admin;
    }

    public List<Pediatra> obtenerLista() {
        List<Pediatra> lista = new ArrayList();
        try {
            openConnection();
            String query = "SELECT ped.fechaRegistro, ped.cedula_especialidad,ped.cmcp,ped.lugar_estudios,ped.aprobado, ped.idpediatra, ped.nombre, ped.apellidos, ped.cedula_profesional, ped.disponibilidad, ped.tarifa, ped.fotografia,\n"
                    + "esp.idespecialidad, esp.nombre AS especialidad, ub.idLugarAtencion, ub.latitud, ub.longitud, ub.direccion, t_ub.nombre AS tipo\n"
                    + "FROM pediatra ped\n"
                    + "JOIN especialidad esp ON ped.especialidad_id = esp.idespecialidad\n"
                    + "JOIN lugar_atencion ub ON ped.lugar_atencion_id = ub.idLugarAtencion\n"
                    + "JOIN tipo_lugar_atencion t_ub ON ub.tipo_lugar_atencion_id = t_ub.idLugar and ped.aprobado=0;";
            PSTM = CONNECTION.prepareStatement(query);
            RS = PSTM.executeQuery();
            while (RS.next()) {
              
                Pediatra pediatra = new Pediatra();
                pediatra.setCedulaEspecialidad(RS.getString("cedula_especialidad"));
                pediatra.setCmcp(RS.getString("cmcp"));
                pediatra.setLugarEstudios(RS.getString("lugar_estudios"));
                pediatra.setId(RS.getInt("idpediatra"));
                pediatra.setNombre(RS.getString("nombre"));
                pediatra.setApellidos(RS.getString("apellidos"));
                pediatra.setCedula(RS.getString("cedula_profesional"));
                pediatra.setDisponible(RS.getBoolean("disponibilidad"));
                pediatra.setImagen(RS.getString("fotografia"));
                pediatra.setAprobado(RS.getBoolean("aprobado"));
                pediatra.setFecRegistro(RS.getString("fechaRegistro"));
                System.out.println(pediatra.getCmcp());
                Especialidad especialidad = new Especialidad();
                especialidad.setId(RS.getInt("idespecialidad"));
                especialidad.setNombre(RS.getString("especialidad"));
               

                LugarAtencion lugar = new LugarAtencion();
                lugar.setId(RS.getInt("idLugarAtencion"));
                lugar.setLatitud(RS.getDouble("latitud"));
                lugar.setLongitud(RS.getDouble("longitud"));
                lugar.setDireccion(RS.getString("direccion"));
                lugar.setTipo(RS.getString("tipo"));

                pediatra.setEspecialidad(especialidad);
                pediatra.setLugarAtencion(lugar);

                lista.add(pediatra);
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return lista;
    }

    public boolean updatePediatra(int id) throws SQLException {
        boolean res = false;
        try {
            openConnection();
            String query = "update pediatra set aprobado=? where idpediatra=?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setBoolean(1, true);
            PSTM.setInt(2, id);
            res = PSTM.executeUpdate() == 1;
            PSTM.close();

            closeConnection();
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    
    
     public boolean deleteReceta(int id) throws SQLException {
        boolean res = false;
         System.out.println("el id" + id);
        try {
            openConnection();
                    
            String query = "DELETE FROM cat_receta_detalle WHERE receta_id=?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, id);
            res = PSTM.executeUpdate() == 1;

            if (res) {
              query = "DELETE FROM cat_recetas WHERE id=?;";
                PSTM = CONNECTION.prepareStatement(query);
                PSTM.setInt(1, id);
                res = PSTM.executeUpdate() == 1;
            }

            PSTM.close();

            closeConnection();
        } catch (SQLException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    
    
    
    public void insert(RecetaTemplate receta, RecetaDetalle r) {
        try {
            openConnection();
            CONNECTION.setAutoCommit(false);
            String query = "INSERT INTO cat_recetas(nombre) VALUES(?);";
            PSTM = CONNECTION.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            PSTM.setString(1, receta.getNombre());
            if (PSTM.executeUpdate() != 1) {
                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
            }
            RS = PSTM.getGeneratedKeys();
            if (RS.next()) {
                System.out.println("entra");
                receta.setId(RS.getInt(1));

                if (receta.getId() != 0) {
                    String query2 = "INSERT INTO cat_receta_detalle(medicamento, dosis, duracion, intervalo, receta_id) VALUES(?,?,?,?,?) ";
                    System.out.println(query2);
                    PSTM = CONNECTION.prepareStatement(query2);

                    PSTM.setString(1, r.getMedicamento());
                    PSTM.setString(2, r.getDosis());
                    PSTM.setString(3, r.getDuracion());
                    PSTM.setString(4, r.getIntervalo());
                    PSTM.setInt(5, receta.getId());

                    PSTM.execute();
                    PSTM.clearParameters();
                }

                PSTM.close();
                RS.close();
            } else {
                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
            }

            CONNECTION.commit();
        } catch (NullPointerException | SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                handleException(ex);
            }
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void updateReceta(RecetaTemplate receta) {
       try {
          
            openConnection();
            CONNECTION.setAutoCommit(false);
      
            String query = "update cat_recetas set nombre=? where id=?;";                    
            PSTM = CONNECTION.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            PSTM.setString(1, receta.getNombre());
            PSTM.setInt(2,receta.getId());
             if(PSTM.executeUpdate() != 1)
                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
            //RS = PSTM.getGeneratedKeys();
          
            
            if(receta.getDetalles() != null && receta.getDetalles().size() > 0) {
              
                query = "update cat_receta_detalle set medicamento=? , dosis=?, duracion=?, intervalo=? where receta_id=? and medicamento=?;";
                PSTM = CONNECTION.prepareStatement(query);
                int i = 1;
                List<RecetaDetalle> detalles = receta.getDetalles();
                for(RecetaDetalle detalle : detalles) {
                 
                    PSTM.setString(i++, detalle.getMedicamento());
                    PSTM.setString(i++, detalle.getDosis());
                    PSTM.setString(i++, detalle.getDuracion());
                    PSTM.setString(i++, detalle.getIntervalo());
                    PSTM.setInt(i++, receta.getId());
                     PSTM.setString(6, detalle.getMedicamento());
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
    
    
//    public void updateReceta(RecetaTemplate receta, RecetaDetalle r) {
//        System.out.println(receta.getId());
//        try {
//            openConnection();
//            CONNECTION.setAutoCommit(false);
//            String query = "update cat_recetas set nombre=? where id=?";
//            PSTM = CONNECTION.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
//            PSTM.setString(1, receta.getNombre());
//            PSTM.setInt(2, receta.getId());
//            if (PSTM.executeUpdate() != 1) {
//                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
//            }
//            RS = PSTM.getGeneratedKeys();
//            if (RS.next()) {
//                System.out.println("entra");
//                receta.setId(RS.getInt(1));
//
//                if (receta.getId() != 0) {
//                    String query2 = "update cat_receta_detalle set medicameneto=? , dosis=?, duracion=?, intervalo=? where receta_id=? ";
//                    System.out.println(query2);
//                    PSTM = CONNECTION.prepareStatement(query2);
//
//                    PSTM.setString(1, r.getMedicamento());
//                    PSTM.setString(2, r.getDosis());
//                    PSTM.setString(3, r.getDuracion());
//                    PSTM.setString(4, r.getIntervalo());
//                    PSTM.setInt(5, receta.getId());
//
//                    PSTM.execute();
//                    PSTM.clearParameters();
//                }
//
//                PSTM.close();
//                RS.close();
//            } else {
//                throw new SQLException("No se registró la receta, favor de intentarlo mas tarde");
//            }
//
//            CONNECTION.commit();
//        } catch (NullPointerException | SQLException e) {
//            try {
//                CONNECTION.rollback();
//            } catch (SQLException ex) {
//                handleException(ex);
//            }
//            handleException(e);
//        } finally {
//            closeConnection();
//        }
//    }
    
    
    public List<RecetaTemplate> obtenerListaRecetas() {
        List<RecetaDetalle> detalles = new ArrayList();
        List<RecetaTemplate> recetasT = new ArrayList();
        try {
            openConnection();
            String query = "SELECT r.id, r.fecha_registro, r.nombre,d.medicamento, d.dosis, d.duracion, d.intervalo,d.receta_id FROM cat_recetas r JOIN cat_receta_detalle d on r.id=d.receta_id ;";
            PSTM = CONNECTION.prepareStatement(query);

            RS = PSTM.executeQuery();
            
            while (RS.next()) {
          RecetaTemplate rt = new RecetaTemplate();
            RecetaDetalle detalle = new RecetaDetalle();
                detalle.setMedicamento(RS.getString("medicamento"));
                detalle.setDosis(RS.getString("dosis"));
                detalle.setDuracion(RS.getString("duracion"));
                detalle.setIntervalo(RS.getString("intervalo"));
                detalle.setReceta_id(RS.getInt("receta_id"));
                detalles.add(detalle);

                rt.setNombre(RS.getString("nombre"));
                rt.setId(RS.getInt("id"));
                rt.setFecRegistro(RS.getString("fecha_registro"));
                rt.setDetalles(detalles);
                recetasT.add(rt);
       
            }

        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return recetasT;
    }

}
