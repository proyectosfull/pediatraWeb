package com.mibebe.service.controller;

import com.mibebe.bean.Dependiente;
import com.mibebe.bean.Pago;
import com.mibebe.bean.Pediatra;
import com.mibebe.dao.UsuarioDao;
import com.mibebe.bean.Usuario;
import com.mibebe.bean.util.DatosConsulta;
import com.mibebe.bean.util.FileBase64;
import com.mibebe.bean.util.PaypalInfo;
import com.mibebe.bean.util.RespuestaConsulta;
import com.mibebe.bean.util.ResumenConsulta;
import com.mibebe.dao.ConsultaDao;
import com.mibebe.dao.PediatraDao;
import com.mibebe.service.security.MySecurityContext;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import static com.mibebe.util.Constants.CONSULTA_FOLDER;
import com.mibebe.util.FCM;
import com.mibebe.util.JSONResponse;
import com.mibebe.util.Multimedia;
import com.mibebe.util.PDFReceta;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/consulta")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ConsultaService {

    private final static String TAG = "ConsultaService";
    @Context
    ServletContext context;
    @Context
    ContainerRequestContext containerRequest;

    @Path("/nuevo/m")
    @PUT
    @Secured
    @Consumes(MULTIPART_FORM_DATA)
    public Response crear(final FormDataMultiPart multiPart) throws IOException {
        int id = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();        
        try {            
            JSONObject data = new JSONObject(multiPart.getField("data").getEntityAs(String.class));
            //System.out.println(data.toString(4));
            JSONResponse jres = new JSONResponse();
            
            DatosConsulta consulta = new DatosConsulta();
            
            //Se asignan valores del json al objeto Consulta
            Usuario usuario = new Usuario();
            usuario.setId(id);
            
            Dependiente dependiente = new Dependiente();
            dependiente.setId(data.getJSONObject("dependiente").getInt("id"));
            
            Pediatra pediatra = new Pediatra();
            pediatra.setId(data.getJSONObject("pediatra").getInt("id"));
            
            PaypalInfo paypal = null;
            if(data.has("paypal")) {        
                paypal = new PaypalInfo();
                paypal.setPaymentId(data.getJSONObject("paypal").getString("paymentId"));
                paypal.setTransactionId(data.getJSONObject("paypal").getString("transactionId"));
                paypal.setCreateTime(data.getJSONObject("paypal").getString("createTime"));
                paypal.setState(data.getJSONObject("paypal").getString("state"));
                paypal.setAmount(data.getJSONObject("paypal").getString("amount"));
            }
            
            consulta.setSintomas(data.getString("sintomas"));
            consulta.setTemperatura((float) data.getDouble("temperatura"));
            consulta.setPeso((float) data.getDouble("temperatura"));
            consulta.setUsuario(usuario);
            consulta.setDependiente(dependiente);
            consulta.setPediatra(pediatra);
            consulta.setPaypal(paypal);
            
            //Creación de archivos
            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath(CONSULTA_FOLDER + String.valueOf(consulta.getDependiente().getId()));
            
            List<FormDataBodyPart> fileParts = multiPart.getFields("files");
            FormDataBodyPart pagoFilePart = multiPart.getField("pago");
           
            if(fileParts != null && fileParts.size() > 0)
                consulta.setNombreArchivos(multimedia.createFiles(fileParts));
            if(pagoFilePart != null)
                consulta.setNombreArchivoPago(multimedia.createFile(pagoFilePart));

            //Se inserta en la base de datos
            ConsultaDao dao = new ConsultaDao();
            dao.insert(consulta);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            } else {
                PediatraDao pediatraDao = new PediatraDao();
                pediatraDao.setFCMTokenPediatra(consulta.getPediatra());
                
                FCM.notificarConsulta(consulta);
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("los parametros son incoprrectos").build();
        }
        
    }
    
    @PUT
    @Secured
    @Path("/nuevo")
    public Response nuevaConsulta(final DatosConsulta consulta) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            Usuario usuario = new Usuario();
            usuario.setId(((MySecurityContext) containerRequest.getSecurityContext()).getUserId());

            consulta.setUsuario(usuario);

            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath(CONSULTA_FOLDER + String.valueOf(consulta.getDependiente().getId()));

            if (consulta.getArchivos() != null && consulta.getArchivos().size() > 0) {
                List<FileBase64> files = consulta.getArchivos();
                List<String> nombreArchivos = new ArrayList();
                for (FileBase64 file : files) {
                    nombreArchivos.add(multimedia.createFile(file));
                    file.setData(null);
                }
                consulta.setNombreArchivos(nombreArchivos);
            }

            if (consulta.getComprobantePago() != null) {
                consulta.setNombreArchivoPago(multimedia.createFile(consulta.getComprobantePago()));
            }

            ConsultaDao dao = new ConsultaDao();
            dao.insert(consulta);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            } else {
                PediatraDao pediatraDao = new PediatraDao();
                pediatraDao.setFCMTokenPediatra(consulta.getPediatra());
                
                FCM.notificarConsulta(consulta);
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }

    @GET
    @Secured
    @Path("/lista")
    public Response obtenerConsultas(@QueryParam("status") Integer status) {
        int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONResponse jres = new JSONResponse();

            ConsultaDao dao = new ConsultaDao();
            List<ResumenConsulta> lista = dao.obtenerLista(idPediatra, status);
            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("consultas", lista);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Secured
    @Path("/usuario/historial")
    public Response obtenerConsultasUsuario(@QueryParam("status") Integer status) {
        int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONResponse jres = new JSONResponse();

            ConsultaDao dao = new ConsultaDao();
            List<ResumenConsulta> lista = dao.obtenerListaUsuario(idUsuario, status);
            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("consultas", lista);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    

    @Path("/pagos")
    @GET
    @Secured
    public Response listarPagos() {
        try {
            JSONResponse jres = new JSONResponse();
            UsuarioDao usuarioDao = new UsuarioDao();
            int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();

            List<Pago> listaPago = usuarioDao.listarPagos(idUsuario);
            jres.setOK(usuarioDao.isSuccess());

            if (usuarioDao.isSuccess()) {
                jres.getData().put("pagos", listaPago);
            } else {
                jres.setMessage(usuarioDao.getErrorMessage());
            }

            return Response.ok(jres.toString()).build();
        } catch (Exception e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @GET
    @Secured
    @Path("/{consultaId}")
    public Response obtenerDetalleConsulta(@PathParam("consultaId") Long consultaId) {
        //int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONResponse jres = new JSONResponse();

            ConsultaDao dao = new ConsultaDao();
            ResumenConsulta consulta = dao.obtenerConsulta(consultaId);
            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("consulta", new JSONObject(consulta));
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Secured
    @Path("/atender")
    public Response realizarConsulta(final RespuestaConsulta respuesta) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            
            ConsultaDao dao = new ConsultaDao();
            dao.realizarConsulta(respuesta);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            } else {
                ResumenConsulta consulta = dao.obtenerConsulta(respuesta.getConsulta().getId());
                
                PDFReceta receta = new PDFReceta(context);
                receta.setPath(CONSULTA_FOLDER + String.valueOf(consulta.getDependiente().getId()));
                String nombre = receta.crearReceta(consulta);
                
                if(nombre != null) {
                    dao.addNombreReceta(respuesta.getConsulta().getId(), nombre);
                    UsuarioDao usuarioDao = new UsuarioDao();
                    usuarioDao.setFCMToken(consulta.getUsuario());                
                    FCM.notificarConsultaRealizada(consulta);
                }                
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Secured
    @Path("/pago/{consultaId}")
    public Response confirmarPago(@PathParam("consultaId") int consultaId) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            
            ConsultaDao dao = new ConsultaDao();
            dao.cambiarStatusPago(consultaId);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
 
}
