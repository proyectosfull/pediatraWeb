package com.mibebe.service.controller;

import com.mibebe.bean.Crecimiento;
import com.mibebe.bean.Dependiente;
import com.mibebe.bean.Usuario;
import com.mibebe.dao.CrecimientoDao;
import com.mibebe.dao.DependienteDao;
import com.mibebe.service.security.MySecurityContext;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import static com.mibebe.util.Constants.DEPENDIENTE_FOLDER;
import com.mibebe.util.JSONResponse;
import com.mibebe.util.Multimedia;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sergio Cabrera
 */
@Path("/dependiente")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class DependienteService {

    private final static String TAG = "DependienteService";
    @Context
    ServletContext context;
    @Context
    ContainerRequestContext containerRequest;

    @POST
    @Secured
    @Path("/v1/nuevo")
    @Consumes(MULTIPART_FORM_DATA)
    public Response registrar(final FormDataMultiPart multiPart) throws IOException {
        try {
            int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();

            //Se guarda la fotografía
            FormDataBodyPart foto = multiPart.getField("foto");

            String imagen = "foto_perfil";
            if (foto != null) {
                Multimedia multimedia = new Multimedia(context);
                multimedia.setPath("/dependientes/" + String.valueOf(idUsuario));
                imagen = multimedia.createFile(foto, imagen);
            }

            Dependiente dependiente = new Dependiente();
            dependiente.setNombre(multiPart.getField("nombre").getValue());
            dependiente.setApellidos(multiPart.getField("apellidos").getValue());
            dependiente.setFecNacimiento(multiPart.getField("fecha_nacimiento").getValue());
            dependiente.setNotas(multiPart.getField("notas").getValue());
            dependiente.setSexo(Integer.parseInt(multiPart.getField("sexo").getValue()));
            int parentesco = Integer.parseInt(multiPart.getField("parentesco").getValue());

            dependiente.setImagen(imagen);

            JSONResponse jres = new JSONResponse();
            DependienteDao dao = new DependienteDao();
            dao.insert(dependiente, idUsuario, parentesco);
            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("dependiente_id", dependiente.getId());
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }

    @POST
    @Secured
    @Path("/nuevo")
    public Response nuevo(String data) {
        try {
            int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();

            JSONObject json = new JSONObject(data);
            int parentesco = json.getInt("parentesco");

            json = json.getJSONObject("dependiente");
            Dependiente dependiente = new Dependiente();
            dependiente.setNombre(json.getString("nombre"));
            dependiente.setApellidos(json.getString("apellidos"));
            dependiente.setFecNacimiento(json.getString("fecha_nacimiento"));
            dependiente.setNotas(json.getString("notas"));
            dependiente.setSexo(json.getInt("sexo"));

            JSONResponse jres = new JSONResponse();
            DependienteDao dao = new DependienteDao();
            dao.insert(dependiente, idUsuario, parentesco);
            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("dependiente_id", dependiente.getId());
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }

    @POST
    @Secured
    @Path("/actualizar")
    public Response actualizar(Dependiente dependiente) {
        try {
            JSONResponse jres = new JSONResponse();
            DependienteDao dao = new DependienteDao();
            dao.actualizar(dependiente);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }

    @PUT
    @Secured
    @Path("/actualizar-foto/{id}")
    @Consumes(MULTIPART_FORM_DATA)
    public Response actualizarFoto(@PathParam("id") int id, final FormDataMultiPart multiPart) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            jres.setOK(false);

            FormDataBodyPart foto = multiPart.getField("foto");
            if (foto == null) {
                jres.setMessage("No se recibió la imagen");
                return Response.ok(jres.toString()).build();
            }

            if (!foto.getMediaType().toString().startsWith("image")) {
                jres.setMessage("El archivo recibido no es una imagen");
                return Response.ok(jres.toString()).build();
            }

            String imagen;
            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath("/dependientes/" + String.valueOf(id));
            imagen = multimedia.createFile(foto, "foto_principal");
            if (imagen == null) {
                jres.setMessage("Ocurrió un error al intentar guardar la imagen, intente mas tarde");
                return Response.ok(jres.toString()).build();
            }

            DependienteDao dao = new DependienteDao();
            dao.actualizarPathFoto(id, imagen);
            if (dao.isSuccess()) {
                jres.setOK(true);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }

    @GET
    @Secured
    @Path("/consulta")
    public Response obtenerLista() {
        try {
            int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
            JSONResponse jres = new JSONResponse();

            DependienteDao dao = new DependienteDao();
            List<Dependiente> lista = dao.obtenerLista(idUsuario);

            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("dependientes", lista);
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
    @Path("/crecimiento")
    public Response registrarCrecimiento(Crecimiento crecimiento){
        try{
            JSONResponse jres = new JSONResponse();
            CrecimientoDao dao = new CrecimientoDao();
            
            dao.registrarCrecimiento(crecimiento);
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess()){
                jres.setMessage(dao.getErrorMessage());
            }         
            return Response.ok(jres.toString()).build();            
        }catch(JSONException e){
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar JSON").build();
        }
    }
    
    @GET
    @Secured
    @Path("/crecimiento/{dependienteId}")
    public Response obtenerListaCrecimiento(@PathParam("dependienteId") int dependienteId){
        try{
            JSONResponse jres = new JSONResponse();
            CrecimientoDao dao = new CrecimientoDao();
            
            List<Crecimiento> listaCrecimiento = dao.obtenerLista(dependienteId);
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()){
                jres.getData().put("crecimiento", listaCrecimiento);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }       
            return Response.ok(jres.toString()).build();            
        }catch(JSONException e){
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar JSON").build();
        }
    }
    
    @PUT
    @Secured
    @Path("/vacuna")    
    @Consumes(MULTIPART_FORM_DATA)
    public Response crear(final FormDataMultiPart multiPart) throws IOException {
        int id = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();        
        try {  
            System.out.println("ENTRA");
            //System.out.println(data.toString(4));
            JSONResponse jres = new JSONResponse();
            
            //Se asignan valores del json al objeto Consulta
            Usuario usuario = new Usuario();
            usuario.setId(id);
            
            Dependiente dependiente = new Dependiente();
            dependiente.setId(Integer.parseInt(multiPart.getField("id").getEntityAs(String.class)));
            
            
            //Creación de archivos
            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath(DEPENDIENTE_FOLDER + String.valueOf(dependiente.getId()));
            
            FormDataBodyPart pagoFilePart = multiPart.getField("vacuna");           
            String nombreArchivo = multimedia.createFile(pagoFilePart);

            //Se inserta en la base de datos
            DependienteDao dao = new DependienteDao();
            dao.addFotoVacuna(dependiente.getId(), nombreArchivo);
            jres.setOK(dao.isSuccess());
            if (!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("los parametros son incoprrectos").build();
        }
        
    }
    
}
