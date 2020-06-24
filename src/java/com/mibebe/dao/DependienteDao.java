package com.mibebe.dao;

import com.mibebe.bean.Dependiente;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergio Cabrera
 */
public class DependienteDao extends Dao {
    
    public void insert(Dependiente dependiente, int idUsuario, int idParentesco) {
        try {
            openConnection();
            String query = "CALL spInsertDependiente(?,?,?,?,?,?,?,?)";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setInt(i++, idUsuario);
            PSTM.setString(i++, dependiente.getNombre());
            PSTM.setString(i++, dependiente.getApellidos());
            PSTM.setString(i++, dependiente.getFecNacimiento());
            PSTM.setString(i++, dependiente.getImagen());
            PSTM.setString(i++, dependiente.getNotas());
            PSTM.setInt(i++, dependiente.getSexo());
            PSTM.setInt(i++, idParentesco);
            
            RS = PSTM.executeQuery();
            if(RS.next()) {
                dependiente.setId(RS.getInt("id"));
            } else {
                setErrorMessage("Ocurrió un error al intentar registrar al dependiente, intentelo nuevamente");
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public void actualizar(Dependiente dependiente) {
        try {
            openConnection();
            String query = "UPDATE dependiente SET nombre = IFNULL(?, nombre), apellidos = IFNULL(?, apellidos), notas = IFNULL(?, notas), genero = IFNULL(?, genero) WHERE iddependiente = ?";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;           
            PSTM.setString(i++, dependiente.getNombre());
            PSTM.setString(i++, dependiente.getApellidos());
            PSTM.setString(i++, dependiente.getNotas());
            if(dependiente.getSexo() == null)
                PSTM.setNull(i++, java.sql.Types.NULL);
            else
                PSTM.setInt(i++, dependiente.getSexo());            
            PSTM.setInt(i++, dependiente.getId());            
            if(PSTM.executeUpdate() < 1) {
                setErrorMessage("Ocurrió un error al intentar actualizar la información, intentelo nuevamente");
            }            
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public List<Dependiente> obtenerLista(int idUsuario) {
        List<Dependiente> lista = new ArrayList();
        try {
            openConnection();
            String query = "SELECT d.iddependiente, d.nombre, d.apellidos, d.nacimiento, d.codigo, d.fotografia, d.notas, d.genero, d.fechaRegistro, d.nombre_archivo_vacuna, p.descripcion\n" +
                            "FROM dependiente d JOIN usuario_tiene_dependiente ud ON ud.dependiente_id = d.iddependiente JOIN parentesco p ON ud.parentesco_id = p.idparentesco\n" +
                            "WHERE ud.usuario_id = ?";                    
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, idUsuario);            
            RS = PSTM.executeQuery();
            while(RS.next()) {
                Dependiente dependiente = new Dependiente();
                dependiente.setId(RS.getInt("iddependiente"));
                dependiente.setNombre(RS.getString("nombre"));
                dependiente.setApellidos(RS.getString("apellidos"));
                dependiente.setFecNacimiento(RS.getString("nacimiento"));
                dependiente.setCodigo(RS.getString("codigo"));
                dependiente.setImagen(RS.getString("fotografia"));
                dependiente.setNotas(RS.getString("notas"));
                dependiente.setSexo(RS.getInt("genero"));
                dependiente.setFecRegistro(RS.getString("fechaRegistro"));
                dependiente.setImagenVacuna(RS.getString("nombre_archivo_vacuna"));
                lista.add(dependiente);
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }
        return lista;
    }
    
    public void actualizarPathFoto(int id, String nombreArchivo) {
        try {
            openConnection();
            String query = "UPDATE dependiente SET fotografia = ? WHERE iddependiente = ?";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setString(i++, nombreArchivo);
            PSTM.setInt(i++, id);
            if(PSTM.executeUpdate() < 1) {
                setErrorMessage("No se actualizó la foto, intente mas tarde");
            }
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public void addFotoVacuna(int dependienteId, String nombreArchivo) {
        try {
            openConnection();
            String query = "UPDATE dependiente SET nombre_archivo_vacuna = ? WHERE iddependiente = ?";                    
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, nombreArchivo);
            PSTM.setInt(2, dependienteId);          
            if(PSTM.executeUpdate() < 1) {
                setErrorMessage("Ocurrió un error al intentar guardar la información");
            }            
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
}
