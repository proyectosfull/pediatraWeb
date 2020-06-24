package com.mibebe.service.controller;

import com.mibebe.bean.Usuario;
import com.mibebe.dao.UsuarioDao;
import com.mibebe.util.AppLog;
import com.mibebe.util.JSONResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;

/**
 *
 * @author Sergio Cabrera
 */
@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioService {
    private static final String TAG = "UsuarioService";
    
    @POST
    @Path("/nuevo")    
    public Response registrar(Usuario usuario) {    
        try {   
            JSONResponse jres = new JSONResponse();
            UsuarioDao dao = new UsuarioDao();
            dao.insert(usuario);
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("user_id", usuario.getId());                
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }

    
    
    /*@GET
    @Path("/consulta")
    public Response nuevaConsulta() {
        try {
            JSONResponse jres = new JSONResponse();
            jres.setOK(false);
            jres.setMessage("test");
            return Response.ok(jres.toString()).build();
        } catch(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
    }*/
}
