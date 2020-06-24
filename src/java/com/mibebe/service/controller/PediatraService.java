package com.mibebe.service.controller;

import com.mibebe.bean.Pediatra;
import com.mibebe.bean.util.FileBase64;
import com.mibebe.dao.PediatraDao;
import com.mibebe.service.security.MySecurityContext;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import static com.mibebe.util.Constants.PEDIATRA_FOLDER;
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
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/pediatra")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PediatraService {
    
    private final static String TAG = "PediatraService";
    @Context ServletContext context;
    @Context ContainerRequestContext containerRequest;
    
    @POST
    @Path("/nuevo")    
    public Response registrar(Pediatra pediatra) { 
        try {   
            JSONResponse jres = new JSONResponse();
            PediatraDao dao = new PediatraDao();
            
            dao.insert(pediatra);
            jres.setOK(dao.isSuccess());
            
            if(dao.isSuccess()) {
                jres.getData().put("pediatra_id", pediatra.getId());                
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            System.out.println(jres.toString());
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
    
    @POST
    @Secured
    @Path("/actualizar")
    public Response actualizar(Pediatra dependiente) {
        try {
            dependiente.setId(((MySecurityContext) containerRequest.getSecurityContext()).getUserId());
            
            JSONResponse jres = new JSONResponse();
            PediatraDao dao = new PediatraDao();
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
    
    @POST
    @Secured
    @Path("/costoconsulta")
    public Response actualizarCostoConsulta(String data) {
        try {            
            int id = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
            
            JSONObject jdata = new JSONObject(data);
            double costo = jdata.getDouble("costo");
            
            JSONResponse jres = new JSONResponse();
            PediatraDao dao = new PediatraDao();
            dao.actualizarCostoConsulta(id, costo);
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
    @Path("/actualizar-foto")
    @Consumes(MULTIPART_FORM_DATA)
    public Response actualizarFoto(final FormDataMultiPart multiPart) throws IOException {
        try {
            int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
            
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
            multimedia.setPath(PEDIATRA_FOLDER + String.valueOf(idPediatra));
            imagen = multimedia.createFile(foto, "foto_principal");
            if (imagen == null) {
                jres.setMessage("Ocurrió un error al intentar guardar la imagen, intente mas tarde");
                return Response.ok(jres.toString()).build();
            }

            PediatraDao dao = new PediatraDao();
            dao.actualizarPathFoto(idPediatra, imagen);
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
    
    @PUT
    @Secured
    @Path("/actualizar-foto64")
    public Response actualizarFoto(final FileBase64 data) throws IOException {
        try {
            int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
            
            JSONResponse jres = new JSONResponse();
            jres.setOK(false);

            String imagen;
            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath(PEDIATRA_FOLDER + String.valueOf(idPediatra));
            data.setName("foto_principal");
            imagen = multimedia.createFile(data);
            if (imagen == null) {
                jres.setMessage("Ocurrió un error al intentar guardar la imagen, intente mas tarde");
                return Response.ok(jres.toString()).build();
            }

            PediatraDao dao = new PediatraDao();
            dao.actualizarPathFoto(idPediatra, imagen);
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
    @Path("/lista")    
    public Response consultar() {    
        try {
            JSONResponse jres = new JSONResponse();

            PediatraDao dao = new PediatraDao();
            List<Pediatra> lista = dao.obtenerLista();

            jres.setOK(dao.isSuccess());
            if (dao.isSuccess()) {
                jres.getData().put("pediatras", lista);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }

            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @PUT
    @Secured
    @Path("/firma")    
    public Response agregarFirma(FileBase64 firma) throws IOException {
        int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONResponse jres = new JSONResponse();            
            
            if(firma == null || firma.getData() == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No se encontró ninguna imagen").build();
            }
            if(!firma.getType().contains("png") && !firma.getType().contains("jpg") && !firma.getType().contains("jpeg")) {
                return Response.status(Response.Status.BAD_REQUEST).entity("El archivo debe ser una imagen").build();
            }
            
            //Se establece la firma para que sea reemplazada cada vez que se actualiza
            firma.setName("firma");
            
            Multimedia multimedia = new Multimedia(context);
            multimedia.setPath(PEDIATRA_FOLDER + String.valueOf(idPediatra));
            String nombreArchivo = multimedia.createFile(firma);
            
            PediatraDao dao = new PediatraDao();
            dao.actualizarFirma(idPediatra, nombreArchivo);

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
