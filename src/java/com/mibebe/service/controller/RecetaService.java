package com.mibebe.service.controller;

import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.util.RecetaTemplate;
import com.mibebe.dao.RecetaDao;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import com.mibebe.util.JSONResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;
import org.json.JSONException;

@Path("/receta")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class RecetaService {
    private static final String TAG = "RecetaService";
    
    @POST
    @Secured
    @Path("/")
    public Response nuevo(RecetaTemplate receta) {
        try {
            JSONResponse jres = new JSONResponse();
            RecetaDao dao = new RecetaDao();
            dao.insert(receta);
            
            
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
    
    @GET
    @Secured
    @Path("/")
    public Response obtenerLista() {
        try {
            JSONResponse jres = new JSONResponse();
            RecetaDao dao = new RecetaDao();
            List<RecetaTemplate> recetas = dao.obtenerLista();
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("recetas", recetas);
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
    @Path("/{recetaId}")
    public Response obtenerLista(@PathParam("recetaId") int recetaId) {
        try {
            JSONResponse jres = new JSONResponse();
            RecetaDao dao = new RecetaDao();
            List<RecetaDetalle> detalles = dao.obtenerLista(recetaId);
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("medicamentos", detalles);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }            
            return Response.ok(jres.toString()).build();
        } catch (JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
}
