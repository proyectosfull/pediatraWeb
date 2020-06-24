package com.mibebe.service.controller;

import com.mibebe.bean.Vacuna;
import com.mibebe.bean.VacunaAplicacion;
import com.mibebe.bean.util.VacunaDependiente;
import com.mibebe.dao.VacunaDao;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import com.mibebe.util.JSONResponse;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import javax.ws.rs.core.Response;
import org.json.JSONException;

@Path("/vacuna")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class VacunaService {
    private static final String TAG = "VacunaController";
    
    @PUT
    @Secured
    @Path("/")    
    public Response registrar(VacunaDependiente vacuna) {    
        try {   
            JSONResponse jres = new JSONResponse();
            VacunaDao dao = new VacunaDao();
            dao.insert(vacuna);
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess()) {
                jres.setMessage(dao.getErrorMessage());             
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
    
    @GET
    @Secured
    @Path("/")    
    public Response getVacunas() {    
        try {   
            JSONResponse jres = new JSONResponse();
            VacunaDao dao = new VacunaDao();
            List<Vacuna> vacunas = dao.get();
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("vacunas", vacunas);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
    
    @GET
    @Secured
    @Path("/tipoaplicacion")    
    public Response getTipoAplicacion() {    
        try {   
            JSONResponse jres = new JSONResponse();
            VacunaDao dao = new VacunaDao();
            List<VacunaAplicacion> tipoAplicacion = dao.getTipoAplicaciones();
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("tipo_aplicacion", tipoAplicacion);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
    
    @GET
    @Secured
    @Path("/dependiente/{dependienteId}")    
    public Response getVacunas(@PathParam("dependienteId") int dependienteId) {    
        try {   
            JSONResponse jres = new JSONResponse();
            VacunaDao dao = new VacunaDao();
            List<VacunaDependiente> vacunas = dao.getPorDependiente(dependienteId);
            jres.setOK(dao.isSuccess());
            if(dao.isSuccess()) {
                jres.getData().put("vacunas", vacunas);
            } else {
                jres.setMessage(dao.getErrorMessage());
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().entity("Error al generar json").build();
        }
    }
}
