package com.mibebe.dao;

import com.mibebe.bean.Especialidad;
import com.mibebe.bean.LugarAtencion;
import com.mibebe.bean.Pediatra;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sergio Cabrera
 */
public class PediatraDao extends Dao {

    public void login(Pediatra pediatra) throws SQLException {
        try {
            openConnection();
            String query = "SELECT salt, fotografia, firma FROM pediatra WHERE email = ?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, pediatra.getCorreo());
            RS = PSTM.executeQuery();
            if (!RS.next()) {
                setErrorMessage("correo o contraseña incorrectos");
                return;
            }
            pediatra.setSalt(RS.getString("salt"));
            
            if(RS.getString("fotografia") == null){
                pediatra.setImagen("");
            }else{
                pediatra.setImagen(RS.getString("fotografia"));
            }
            
            if(RS.getString("firma") == null){
                pediatra.setFirma("");
            }else{
                pediatra.setFirma(RS.getString("firma"));
            }
            
            RS.close();
            PSTM.close();

            PSTM = CONNECTION.prepareStatement("CALL spLoginPediatra(?,?,?)");
            PSTM.setString(1, pediatra.getCorreo());
            PSTM.setString(2, pediatra.getPassword());
            PSTM.setString(3, pediatra.getFCMToken());
            RS = PSTM.executeQuery();
            if (RS.next()) {
                pediatra.setId(RS.getInt("idpediatra"));
                pediatra.setNombre(RS.getString("nombre"));
                pediatra.setApellidos(RS.getString("apellidos"));
                pediatra.setJwtControl(RS.getString("jwt_control"));

                pediatra.setPassword(null);
                pediatra.setSalt(null);
            }
        } catch (NullPointerException | SQLException e) {
            System.out.println(e.getMessage());
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public JSONObject obtainPhotos(int idPediatra) {
        JSONObject data = new JSONObject();
        String foto = "", firma = "";
        try {
            openConnection();
            String query = "SELECT fotografia, firma FROM pediatra WHERE idpediatra =30";
            PSTM = CONNECTION.prepareStatement(query);
            //PSTM.setInt(1, 30);

            RS = PSTM.executeQuery();
            System.out.println(RS.first());
            if (RS.first()) {

                foto = RS.getString("fotografia");
                firma = RS.getString("firma");
                System.out.println("Aquí estoy, idPediatra: " + idPediatra);

            } else {
                setErrorMessage("Ocurrió un error");
            }

            data.put("foto", foto);
            data.put("firma", firma);

        } catch (NullPointerException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            handleException(e);
        } catch (JSONException ex) {
            Logger.getLogger(PediatraDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        System.out.println(data.toString() + "id: " + idPediatra);
        return data;
    }

    public void validarUsuario(int id, String jwtControl, String jti) {
        try {
            openConnection();
            String query = "CALL spValidarPediatra(?,?,?)";
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setInt(i++, id);
            PSTM.setString(i++, jwtControl);
            PSTM.setString(i++, jti);

            RS = PSTM.executeQuery();
            if (RS.next()) {
                if (!RS.getBoolean("OK")) {
                    setErrorMessage("Los datos no son correctos");
                }
            } else {
                setErrorMessage("Ocurrió un error");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void insert(Pediatra pediatra) {
        try {
            openConnection();
            String query = "CALL spInsertPediatra(?,?,?,?,?,?,?,?,?,?);";
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setString(i++, pediatra.getCorreo());
            PSTM.setString(i++, pediatra.getPassword());
            PSTM.setString(i++, pediatra.getNombre());
            PSTM.setString(i++, pediatra.getApellidos());
            PSTM.setString(i++, pediatra.getCurp());
            PSTM.setString(i++, pediatra.getCedula());
            PSTM.setString(i++, pediatra.getCedulaEspecialidad());
            PSTM.setString(i++, pediatra.getCmcp());
            PSTM.setString(i++, pediatra.getLugarEstudios());
            PSTM.setString(i++, pediatra.getSalt());
            RS = PSTM.executeQuery();
            if (RS.next()) {
                pediatra.setId(RS.getInt("id"));
                System.out.println("OK");
            } else {
                setErrorMessage("Ocurrió un error, no se realizó el registro");
                System.out.println("nada");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void actualizar(Pediatra pediatra) {
        try {
            openConnection();
            String query = "UPDATE pediatra SET nombre = IFNULL(?, nombre), apellidos = IFNULL(?, apellidos), cedula_profesional = IFNULL(?, cedula_profesional) WHERE idpediatra = ?";
            PSTM = CONNECTION.prepareStatement(query);
            int i = 1;
            PSTM.setString(i++, pediatra.getNombre());
            PSTM.setString(i++, pediatra.getApellidos());
            PSTM.setString(i++, pediatra.getCedula());
            PSTM.setInt(i++, pediatra.getId());
            if (PSTM.executeUpdate() < 1) {
                setErrorMessage("Ocurrió un error al intentar actualizar la información, intentelo nuevamente");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void actualizarContrasena(Pediatra pediatra, String newPassword) {
        try {
            openConnection();
            String oldPassword;
            String query = "SELECT salt FROM pediatra WHERE idpediatra = ?;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, pediatra.getId());
            RS = PSTM.executeQuery();
            if (!RS.next()) {
                setErrorMessage("Ocurrió un error");
                return;
            }
            pediatra.setSalt(RS.getString("salt"));
            oldPassword = pediatra.getPassword();
            pediatra.setPassword(newPassword);
            RS.close();
            PSTM.close();

            PSTM = CONNECTION.prepareStatement("UPDATE pediatra SET `password` = ? WHERE idpediatra = ? AND `password` LIKE ?;");
            PSTM.setString(1, pediatra.getPassword());
            PSTM.setInt(2, pediatra.getId());
            PSTM.setString(3, oldPassword);
            if (PSTM.executeUpdate() != 1) {
                setErrorMessage("No se actualizó la contraseña, verifica que la contraseña original sea correcta");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void actualizarPathFoto(int id, String nombreArchivo) {
        try {
            openConnection();
            String query = "UPDATE pediatra SET fotografia = ? WHERE idpediatra = ?";
            PSTM = CONNECTION.prepareStatement(query);

            PSTM.setString(1, nombreArchivo);
            PSTM.setInt(2, id);
            if (PSTM.executeUpdate() < 1) {
                setErrorMessage("No se actualizó la foto, intente mas tarde");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public List<Pediatra> obtenerLista() {
        List<Pediatra> lista = new ArrayList();
        try {
            openConnection();

            String query = "SELECT ped.email, ped.password, ped.idpediatra, ped.nombre, ped.apellidos, ped.cedula_profesional, ped.disponibilidad, ped.tarifa, ped.fotografia,\n"
                    + "esp.idespecialidad, esp.nombre AS especialidad, ub.idLugarAtencion, ub.latitud, ub.longitud, ub.direccion, t_ub.nombre AS tipo\n"
                    + "FROM pediatra ped\n"
                    + "JOIN especialidad esp ON ped.especialidad_id = esp.idespecialidad\n"
                    + "JOIN lugar_atencion ub ON ped.lugar_atencion_id = ub.idLugarAtencion\n"
                    + "JOIN tipo_lugar_atencion t_ub ON ub.tipo_lugar_atencion_id = t_ub.idLugar\n"
                    + "WHERE ped.aprobado =1\n"
                    + "ORDER BY ped.orden, ped.nombre ASC";

            PSTM = CONNECTION.prepareStatement(query);
            RS = PSTM.executeQuery();
            while (RS.next()) {
                Pediatra pediatra = new Pediatra();
                pediatra.setId(RS.getInt("idpediatra"));
                pediatra.setNombre(RS.getString("nombre"));
                pediatra.setApellidos(RS.getString("apellidos"));
                pediatra.setCedula(RS.getString("cedula_profesional"));
                pediatra.setDisponible(RS.getBoolean("disponibilidad"));
                pediatra.setImagen(RS.getString("fotografia"));
                pediatra.setTarifa(RS.getDouble("tarifa"));

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
                System.out.println(RS.getString("email") + " " + RS.getString("password"));
                lista.add(pediatra);
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return lista;
    }

    public String setFCMTokenPediatra(Pediatra pediatra) {
        String token = "";
        try {
            openConnection();
            String query = "SELECT FCMToken, ios FROM pediatra WHERE idpediatra = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, pediatra.getId());
            RS = PSTM.executeQuery();

            if (RS.next()) {
                pediatra.setFCMToken(RS.getString("FCMToken"));
                pediatra.setIOS(RS.getBoolean("ios"));
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }

        return token;
    }

    public void actualizarFirma(int pediatraId, String nombreArchivo) {
        try {
            openConnection();
            String query = "UPDATE pediatra SET firma = ? WHERE idpediatra = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, nombreArchivo);
            PSTM.setInt(2, pediatraId);

            if (PSTM.executeUpdate() != 1) {
                setErrorMessage("No se actualizó la firma, intente mas tarde");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void actualizarCostoConsulta(int pediatraId, double costo) {
        try {
            openConnection();
            String query = "UPDATE pediatra SET tarifa = ? WHERE idpediatra = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setDouble(1, costo);
            PSTM.setInt(2, pediatraId);

            if (PSTM.executeUpdate() != 1) {
                setErrorMessage("No se actualizó la firma, intente mas tarde");
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
    }
}
