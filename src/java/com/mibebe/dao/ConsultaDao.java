package com.mibebe.dao;

import com.mibebe.bean.Dependiente;
import com.mibebe.bean.Pediatra;
import com.mibebe.bean.Receta;
import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.Usuario;
import com.mibebe.bean.util.DatosConsulta;
import com.mibebe.bean.util.RespuestaConsulta;
import com.mibebe.bean.util.ResumenConsulta;
import java.sql.SQLException;
import static java.sql.PreparedStatement.RETURN_GENERATED_KEYS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sergio Cabrera
 */
public class ConsultaDao extends Dao {

    public void insert(DatosConsulta consulta) {
        try {
            openConnection();
            CONNECTION.setAutoCommit(false);
            String query;

            Integer idPago = null;
            Integer idConsulta = null;

            //Inserta el comprobante
            if (consulta.getPaypal() != null) {
                query = "INSERT INTO pago(payment_id, transaction_id, payment_amount, payment_status, payment_create_time, verificado)\n"
                        + "VALUES(?,?,?,?,?,1)";
                PSTM = CONNECTION.prepareStatement(query, RETURN_GENERATED_KEYS);
                int i = 1;
                PSTM.setString(i++, consulta.getPaypal().getPaymentId());
                PSTM.setString(i++, consulta.getPaypal().getTransactionId());
                PSTM.setString(i++, consulta.getPaypal().getAmount());
                PSTM.setString(i++, consulta.getPaypal().getState());
                PSTM.setString(i++, consulta.getPaypal().getCreateTime());
            } else {
                query = "INSERT INTO pago(nombreArchivo) VALUES(?);";
                PSTM = CONNECTION.prepareStatement(query, RETURN_GENERATED_KEYS);
                PSTM.setString(1, consulta.getNombreArchivoPago());
            }
            
            if (PSTM.executeUpdate() != 1) {
                throw new SQLException("Ocurrió un error al intentar insertar el método de pago");
            }

            RS = PSTM.getGeneratedKeys();
            idPago = RS.next() ? RS.getInt(1) : null;
            RS.close();
            PSTM.close();

            //Inserta la consulta
            query = "INSERT INTO consulta(temperatura, peso, sintomas, usuario_id, pediatra_id, dependiente_id, lugar_atencion_id, pago_id) "
                    + "VALUES(?,?,?,?,?,?,?,?);";
            PSTM = CONNECTION.prepareStatement(query, RETURN_GENERATED_KEYS);
            int i = 1;
            PSTM.setFloat(i++, consulta.getTemperatura());
            PSTM.setFloat(i++, consulta.getPeso());
            PSTM.setString(i++, consulta.getSintomas());
            PSTM.setInt(i++, consulta.getUsuario().getId());
            PSTM.setInt(i++, consulta.getPediatra().getId());
            PSTM.setInt(i++, consulta.getDependiente().getId());
            PSTM.setInt(i++, 1);
            if (idPago != null) {
                PSTM.setInt(i, idPago);
            } else {
                PSTM.setNull(i, java.sql.Types.NULL);
            }

            if (PSTM.executeUpdate() != 1) {
                throw new SQLException("Ocurrió un error al intentar insertar la consulta");
            }

            RS = PSTM.getGeneratedKeys();
            idConsulta = RS.next() ? RS.getInt(1) : null;
            RS.close();
            PSTM.close();

            //Inserta los archivos
            if (idConsulta == null) {
                throw new SQLException("Ocurrió un error al intentar insertar la consulta");
            }

            if (consulta.getNombreArchivos() != null && consulta.getNombreArchivos().size() > 0) {
                query = "INSERT INTO evidencia(consulta_id, nombreArchivo) VALUES(?,?);";
                PSTM = CONNECTION.prepareStatement(query);
                for (String fileName : consulta.getNombreArchivos()) {
                    PSTM.setInt(1, idConsulta);
                    PSTM.setString(2, fileName);
                    PSTM.executeUpdate();
                    PSTM.clearParameters();
                }
            }

            CONNECTION.commit();
        } catch (NullPointerException | SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
            }
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public List<ResumenConsulta> obtenerLista(int idUsuario, Integer status) {

        List<ResumenConsulta> lista = new ArrayList();
        try {
            openConnection();
            String query = "SELECT c.idconsulta, u.idUsuario, u.nombre unombre, u.apellidos uapellidos, d.iddependiente, d.nombre dnombre, d.apellidos dapellidos, p.descripcion parentesco,  c.fechaRegistro, c.fechaAtendido, c.atendido\n"
                    + "FROM consulta c\n"
                    + "JOIN usuario u ON c.usuario_id = u.idUsuario\n"
                    + "JOIN dependiente d ON c.dependiente_id = d.iddependiente\n"
                    + "JOIN usuario_tiene_dependiente ud ON c.usuario_id = ud.usuario_id AND c.dependiente_id = ud.dependiente_id\n"
                    + "JOIN parentesco p ON ud.parentesco_id = p.idparentesco\n"
                    + "WHERE c.pediatra_id = ? " + (status != null ? "AND atendido = ? " : "")
                    + "ORDER BY c.fechaRegistro DESC;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, idUsuario);
            if (status != null) {
                PSTM.setInt(2, status);
            }

            RS = PSTM.executeQuery();

            while (RS.next()) {
                ResumenConsulta consulta = new ResumenConsulta();
                consulta.setId(RS.getInt("idconsulta"));
                consulta.setFecRegistro(RS.getString("fechaRegistro"));
                consulta.setFecAtendido(RS.getString("fechaAtendido"));
                consulta.setAtendido(RS.getBoolean("atendido"));

                Usuario usuario = new Usuario();
                usuario.setId(RS.getInt("idUsuario"));
                usuario.setNombre(RS.getString("unombre"));
                usuario.setApellidos(RS.getString("uapellidos"));

                Dependiente dependiente = new Dependiente();
                dependiente.setId(RS.getInt("iddependiente"));
                dependiente.setNombre(RS.getString("dnombre"));
                dependiente.setApellidos(RS.getString("dapellidos"));

                consulta.setParentesco(RS.getString("parentesco"));
                consulta.setUsuario(usuario);
                consulta.setDependiente(dependiente);

                lista.add(consulta);
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return lista;
    }

    public List<ResumenConsulta> obtenerListaUsuario(int idUsuario, Integer status) {
        List<ResumenConsulta> lista = new ArrayList();
        try {
            openConnection();
            String query = "SELECT c.idconsulta, p.idpediatra, p.nombre pnombre, p.apellidos papellidos, d.iddependiente, d.nombre dnombre, d.apellidos dapellidos, c.fechaRegistro, c.fechaAtendido, c.atendido\n"
                    + "FROM consulta c\n"
                    + "JOIN dependiente d ON c.dependiente_id = d.iddependiente\n"
                    + "JOIN pediatra p ON c.pediatra_id = p.idpediatra\n"
                    + "WHERE  c.usuario_id = ? " + (status != null ? "AND atendido = ? " : "")
                    + "ORDER BY c.fechaRegistro DESC;";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, idUsuario);

            if (status != null) {
                PSTM.setInt(2, status);
            }

            RS = PSTM.executeQuery();

            while (RS.next()) {
                ResumenConsulta consulta = new ResumenConsulta();
                consulta.setId(RS.getInt("idconsulta"));
                consulta.setAtendido(RS.getBoolean("atendido"));
                consulta.setFecRegistro(RS.getString("fechaRegistro"));
                consulta.setFecAtendido(RS.getString("fechaAtendido"));

                Pediatra pediatra = new Pediatra();
                pediatra.setId(RS.getInt("idpediatra"));
                pediatra.setNombre(RS.getString("pnombre"));
                pediatra.setApellidos(RS.getString("papellidos"));

                Dependiente dependiente = new Dependiente();
                dependiente.setId(RS.getInt("iddependiente"));
                dependiente.setNombre(RS.getString("dnombre"));
                dependiente.setApellidos(RS.getString("dapellidos"));

                consulta.setPediatra(pediatra);
                consulta.setDependiente(dependiente);

                lista.add(consulta);
            }
        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return lista;
    }

    public ResumenConsulta obtenerConsulta(long consultaId) {
        ResumenConsulta consulta = new ResumenConsulta();
        try {
            openConnection();
            String query = "SELECT c.idconsulta, c.temperatura, c.peso, c.sintomas, p.idpediatra, p.email, p.nombre pnombre, p.apellidos papellidos, p.cedula_profesional, p.cedula_especialidad, p.cmcp, p.lugar_estudios, p.firma, u.idusuario uid, u.nombre unombre, u.apellidos uapellidos, d.nombre dnombre, d.iddependiente, d.apellidos dapellidos, pc.descripcion parentesco, c.fechaRegistro, c.fechaAtendido, c.notas_pediatra, c.atendido, c.pagado, c.receta_pdf\n"
                    + "FROM consulta c\n"
                    + "JOIN pediatra p ON p.idpediatra = c.pediatra_id\n"
                    + "JOIN usuario u ON c.idconsulta = ? AND c.usuario_id = u.idUsuario\n"
                    + "JOIN dependiente d ON c.dependiente_id = d.iddependiente\n"
                    + "JOIN usuario_tiene_dependiente ud ON c.usuario_id = ud.usuario_id AND c.dependiente_id = ud.dependiente_id\n"
                    + "JOIN parentesco pc ON ud.parentesco_id = pc.idparentesco";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setLong(1, consultaId);

            RS = PSTM.executeQuery();

            if (!RS.next()) {
                setErrorMessage("No se encontró la consulta, consulta no existente");
                return null;
            }

            //Se llenan los datos de la consulta
            consulta.setId(RS.getInt("idconsulta"));
            consulta.setTemperatura(RS.getFloat("temperatura"));
            consulta.setPeso(RS.getFloat("peso"));
            consulta.setSintomas(RS.getString("sintomas"));
            consulta.setParentesco(RS.getString("parentesco"));
            consulta.setFecRegistro(RS.getString("fechaRegistro"));
            consulta.setFecAtendido(RS.getString("fechaAtendido"));
            consulta.setNotaPediatra(RS.getString("notas_pediatra"));
            consulta.setAtendido(RS.getBoolean("atendido"));
            consulta.setPagado(RS.getBoolean("pagado"));
            consulta.setRecetaPdf(RS.getString("receta_pdf"));

            Pediatra pediatra = new Pediatra();
            pediatra.setId(RS.getInt("idpediatra"));
            pediatra.setNombre(RS.getString("pnombre"));
            pediatra.setApellidos(RS.getString("papellidos"));
            pediatra.setCedula(RS.getString("cedula_profesional"));
            pediatra.setCedulaEspecialidad(RS.getString("cedula_especialidad"));
            pediatra.setCmcp(RS.getString("cmcp"));
            pediatra.setLugarEstudios(RS.getString("lugar_estudios"));
            pediatra.setFirma(RS.getString("firma"));

            Usuario usuario = new Usuario();
            usuario.setId(RS.getInt("uid"));
            usuario.setNombre(RS.getString("unombre"));
            usuario.setApellidos(RS.getString("uapellidos"));

            Dependiente dependiente = new Dependiente();
            dependiente.setId(RS.getInt("iddependiente"));
            dependiente.setNombre(RS.getString("dnombre"));
            dependiente.setApellidos(RS.getString("dapellidos"));

            consulta.setPediatra(pediatra);
            consulta.setUsuario(usuario);
            consulta.setDependiente(dependiente);

            PSTM.close();
            RS.close();

            //Se obtienen los archivos asociados
            PSTM = CONNECTION.prepareStatement("SELECT nombreArchivo FROM evidencia WHERE consulta_id = ?");
            PSTM.setLong(1, consultaId);
            RS = PSTM.executeQuery();
            List<String> listaArchivos = new ArrayList();
            while (RS.next()) {
                listaArchivos.add(RS.getString("nombreArchivo"));
            }
            consulta.setArchivos(listaArchivos);

            //Se obtienen datos de la receta si fué atendida
            Receta receta = new Receta();
            List<RecetaDetalle> detalles = new ArrayList();
            if (consulta.isAtendido()) {
                query = "SELECT r.idreceta, r.qrcode, medicamento, dosis, duracionTratamiento, intervaloDeTiempo\n"
                        + "FROM receta r\n"
                        + "JOIN recetadetalle rd ON r.idreceta = rd.receta_id\n"
                        + "WHERE r.idreceta = (SELECT receta_id FROM consulta WHERE idconsulta = ? LIMIT 1)";
                PSTM = CONNECTION.prepareStatement(query);
                PSTM.setLong(1, consultaId);
                RS = PSTM.executeQuery();

                boolean primerFila = true;
                while (RS.next()) {
                    if (primerFila) {
                        receta.setId(RS.getInt("idreceta"));
                        receta.setQrcode(RS.getString("qrcode"));
                        primerFila = false;
                    }
                    RecetaDetalle detalle = new RecetaDetalle();
                    detalle.setMedicamento(RS.getString("medicamento"));
                    detalle.setDosis(RS.getString("dosis"));
                    detalle.setDuracion(RS.getString("duracionTratamiento"));
                    detalle.setIntervalo(RS.getString("intervaloDeTiempo"));
                    detalles.add(detalle);
                }
                receta.setDetalles(detalles);
            }
            consulta.setReceta(receta);

        } catch (NullPointerException | SQLException e) {
            handleException(e);
        } finally {
            closeConnection();
        }
        return consulta;
    }

    public void realizarConsulta(RespuestaConsulta respuesta) {
        try {
            openConnection();
            CONNECTION.setAutoCommit(false);
            String query;

            //Verifica que haya datos de medicamentos
            boolean isRecetaInsertada = false;
            if (respuesta.getDetalles() != null && respuesta.getDetalles().size() > 0) {
                query = "INSERT INTO receta(qrcode)VALUES(UUID())";
                PSTM = CONNECTION.prepareStatement(query, RETURN_GENERATED_KEYS);
                if (PSTM.executeUpdate() != 1) //Lanza error en caso que no se haya insertado
                {
                    throw new SQLException("Ocurrió al intentar registrar receta");
                }

                RS = PSTM.getGeneratedKeys();
                if (RS.next()) {
                    respuesta.setId(RS.getInt(1));
                }
                PSTM.close();
                RS.close();

                query = "INSERT INTO recetaDetalle(medicamento, dosis, duracionTratamiento, intervaloDeTiempo, receta_id)VALUES(?,?,?,?,?)";
                PSTM = CONNECTION.prepareStatement(query);
                int i;
                for (RecetaDetalle detalle : respuesta.getDetalles()) {
                    i = 1;
                    System.out.println(detalle.getDuracion());
                    PSTM.setString(i++, detalle.getMedicamento());
                    PSTM.setString(i++, detalle.getDosis());
                    PSTM.setString(i++, detalle.getDuracion());
                    PSTM.setString(i++, detalle.getIntervalo());
                    PSTM.setInt(i++, respuesta.getId());
                    if (PSTM.executeUpdate() != 1) {
                        throw new SQLException("Ocurrió al intentar registrar medicamentos");
                    }
                    PSTM.clearParameters();
                }
                isRecetaInsertada = true;
                PSTM.close();
            }

            int i = 1;
            query = "UPDATE consulta SET receta_id = ?, notas_pediatra = ?, atendido = 1, fechaAtendido = current_timestamp() WHERE idconsulta = ?";
            PSTM = CONNECTION.prepareStatement(query);
            if (isRecetaInsertada) {
                PSTM.setInt(i++, respuesta.getId());
            } else {
                PSTM.setNull(i++, java.sql.Types.NULL);
            }
            PSTM.setString(i++, respuesta.getNotas());
            PSTM.setLong(i++, respuesta.getConsulta().getId());
            PSTM.executeUpdate();

            CONNECTION.commit();
        } catch (NullPointerException | SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
            }
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void cambiarStatusPago(int consultaId) {
        try {
            openConnection();
            String query = "UPDATE consulta SET pagado = 1 WHERE idconsulta = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setInt(1, consultaId);
            if (PSTM.executeUpdate() != 1) {
                setErrorMessage("Ocurrió un error al intentar confirmar pago");
            }
        } catch (NullPointerException | SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
            }
            handleException(e);
        } finally {
            closeConnection();
        }
    }

    public void addNombreReceta(long consultaId, String nombreArchivo) {
        try {
            openConnection();
            String query = "UPDATE consulta SET receta_pdf = ? WHERE idconsulta = ?";
            PSTM = CONNECTION.prepareStatement(query);
            PSTM.setString(1, nombreArchivo);
            PSTM.setLong(2, consultaId);
            if (PSTM.executeUpdate() != 1) {
                setErrorMessage("Ocurrió un error al intentar actualizar información");
            }
        } catch (NullPointerException | SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
            }
            handleException(e);
        } finally {
            closeConnection();
        }
    }
}
