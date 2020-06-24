package com.mibebe.dao;

import com.mibebe.bean.Consulta;
import com.mibebe.bean.Dependiente;
import com.mibebe.bean.Pago;
import com.mibebe.bean.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergio Cabrera
 */
public class UsuarioDao extends Dao {
        
    public void login(Usuario usuario) {
        try {
            openConnection();
            String query = "SELECT salt FROM usuario WHERE email = ?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, usuario.getCorreo());
            RS = PSTM.executeQuery();
            if(!RS.next()) {
                setErrorMessage("Correo o contraseña incorrectos");
                return;
            }            
            usuario.setSalt(RS.getString("salt"));
            RS.close();
            PSTM.close();
            
            PSTM = CONNECTION.prepareStatement("CALL spLoginUsuario(?,?,?)");
            PSTM.setString(1, usuario.getCorreo());
            PSTM.setString(2, usuario.getPassword());
            PSTM.setString(3, usuario.getFCMToken());
            RS = PSTM.executeQuery();
            if(RS.next()) {
                usuario.setId(RS.getInt("idUsuario"));
                usuario.setNombre(RS.getString("nombre"));
                usuario.setApellidos(RS.getString("apellidos"));
                usuario.setJwtControl(RS.getString("jwt_control"));
                
                usuario.setPassword(null);
                usuario.setSalt(null);
            }            
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }
    
    public void validarUsuario(int id, String jwtControl, String jti) {
        try {
            openConnection();
            String query = "CALL spValidarUsuario(?,?,?)";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setInt(i++, id);
            PSTM.setString(i++, jwtControl);
            PSTM.setString(i++, jti);         
            
            RS = PSTM.executeQuery();
            if(RS.next()) {
                System.out.println(!RS.getBoolean("OK"));
                if(!RS.getBoolean("OK"))
                    setErrorMessage("Los datos no son correctos");
            } else {
                setErrorMessage("Ocurrió un error");
            }
        } catch(SQLException e) {
            setErrorMessage(e.getErrorCode() > 5000 ? e.getMessage() : "Ocurrió un error desconocido");     
        } catch(Exception e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public void insert(Usuario usuario) {
        try {
            openConnection();
            String query = "CALL spInsertUsuario(?,?,?,?,?,?,?)";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setString(i++, usuario.getCorreo());
            PSTM.setString(i++, usuario.getPassword());
            PSTM.setString(i++, usuario.getNombre());
            PSTM.setString(i++, usuario.getApellidos());
            PSTM.setString(i++, usuario.getFecNacimiento());
            PSTM.setString(i++, usuario.getImagen());
            PSTM.setString(i++, usuario.getSalt());            
            
            RS = PSTM.executeQuery();
            if(RS.next()) {
                usuario.setId(RS.getInt("id"));
            } else {
                setErrorMessage("No se insertó el usuario");
            }
        } catch(SQLException e) {
            setErrorMessage(e.getErrorCode() > 5000 ? e.getMessage() : "Ocurrió un error desconocido");     
        } catch(Exception e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public void loginViaFacebook(Usuario usuario) {
        try {
            openConnection();
            String query = "CALL spLoginUsuarioFacebook(?,?,?,?,?,?)";                    
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setString(i++, usuario.getCorreo());
            PSTM.setString(i++, usuario.getPassword());
            PSTM.setString(i++, usuario.getNombre());
            PSTM.setString(i++, usuario.getApellidos());
            PSTM.setString(i++, usuario.getSalt());            
            PSTM.setString(i++, usuario.getFCMToken());
            
            RS = PSTM.executeQuery();
            if(RS.next()) {
                usuario.setId(RS.getInt("idUsuario"));
                usuario.setJwtControl(RS.getString("jwt_control"));                
                usuario.setPassword(null);
                usuario.setSalt(null);
            } else {
                setErrorMessage("Ocurrió un problema intentelo mas tarde");
            }
        } catch(SQLException e) {
            setErrorMessage(e.getErrorCode() > 5000 ? e.getMessage() : "Ocurrió un error desconocido");     
        } catch(Exception e) {
            handleException(e);
        }finally {
            closeConnection();
        }
    }
    
    public void actualizarContrasena(Usuario usuario, String newPassword) {
        try {
            openConnection();
            String oldPassword;
            String query = "SELECT salt FROM usuario WHERE idUsuario = ?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, usuario.getId());
            RS = PSTM.executeQuery();
            if(!RS.next()) {
                setErrorMessage("Ocurrió un error");
                return;
            }            
            usuario.setSalt(RS.getString("salt"));
            oldPassword = usuario.getPassword();
            usuario.setPassword(newPassword);
            RS.close();
            PSTM.close();
            
            PSTM = CONNECTION.prepareStatement("UPDATE usuario SET `password` = ? WHERE idUsuario = ? AND `password` LIKE ?;");
            PSTM.setString(1, usuario.getPassword());
            PSTM.setInt(2, usuario.getId());
            PSTM.setString(3, oldPassword);
            if(PSTM.executeUpdate() != 1) {
                setErrorMessage("No se actualizó la contraseña, verifica que la contraseña sea correcta");
            }         
        } catch(NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }
    
    public List<Pago> listarPagos(int idUsuario){
        List<Pago> listaPago = new ArrayList();
        
        try{
            openConnection();
            String query = "SELECT c.idconsulta, c.fechaRegistro, nombreArchivo, u.idUsuario, u.nombre, u.apellidos, c.dependiente_id FROM consulta c\n" +
                            "JOIN usuario u ON c.usuario_id = u.idUsuario\n" +
                            "JOIN pago p ON c.pago_id = p.idPago\n" +
                            "WHERE c.pediatra_id = ? AND c.pagado = 0";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, idUsuario);
            RS = PSTM.executeQuery();
            
            while(RS.next()){
                Pago pago = new Pago();
                pago.setNombre(RS.getString("nombreArchivo"));
                
                Consulta consulta = new Consulta();                
                consulta.setId(RS.getInt("idconsulta"));                
                consulta.setFecRegistro(RS.getString("fechaRegistro"));
                
                Usuario usuario = new Usuario();
                usuario.setId(RS.getInt("idUsuario"));
                usuario.setNombre(RS.getString("nombre"));
                usuario.setApellidos(RS.getString("apellidos"));
                
                Dependiente dependiente = new Dependiente();
                dependiente.setId(RS.getInt("dependiente_id"));
                                
                consulta.setUsuario(usuario);
                consulta.setDependiente(dependiente);
                pago.setConsulta(consulta);
                listaPago.add(pago);
            }
        }catch(NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        
        return listaPago;
    }
    
    public String setFCMToken(Usuario usuario){        
        String token = null;        
        try{
            openConnection();
            String query = "SELECT FCMToken, ios FROM usuario WHERE idUsuario = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, usuario.getId());
            RS = PSTM.executeQuery();
            
            if(RS.next()){
                usuario.setFCMToken(RS.getString("FCMToken"));
                usuario.setIOS(RS.getBoolean("ios"));
            }
        }catch(NullPointerException | SQLException e) {
            handleException(e);
        }finally {
            closeConnection();
        }        
        return token;
    }
}
