package com.mibebe.service.controller;

import com.google.gson.Gson;
import com.mibebe.bean.Administrador;
import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.util.RecetaTemplate;
import com.mibebe.dao.AdministradorDao;
import com.mibebe.dao.RecetaDao;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author emmanuel
 */
@Path("/administrador")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdministradorService {

    private Map<String, Object> response = new HashMap();
    private AdministradorDao dao = new AdministradorDao();
    @Context
    private HttpServletRequest request;

    @PUT
    @Path("/login")
    public String login(String data) throws JSONException {
        JSONObject json = new JSONObject(data); //Convierte los datos a JSON
        Administrador administrador = new Gson().fromJson(data, Administrador.class);

        administrador = dao.login(administrador);
        System.out.println(administrador);
        //LOGEO DESDE WEB

        if (administrador != null) {

            response.put("OK", true);
            response.put("idusuario", administrador.getId());

            request.getSession(true).setAttribute("logged", administrador);
        } else {

            System.out.println("entra a el else");
            response.put("OK", false);
            response.put("message", "Usuario o Contraseña incorrecta");
        }

        return new Gson().toJson(response);
    }

    
    
    @POST
    @Path("/updatePediatra/{id}/{email}")
    public String updatePediatra(@PathParam("id") int id, @PathParam("email") String email) throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        System.out.println(admin);
        if (admin != null) {
            response.put("data", dao.updatePediatra(id));
            
            HtmlEmailSender htmlEmailSender = new HtmlEmailSender();
            try {
                htmlEmailSender.sendHtmlEmail(email);
            } catch (AddressException ex) {
                Logger.getLogger(AdministradorService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            response.put("OK", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }
    
    @POST
    @Path("/getPediatras")
    public String getPediatras() throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        System.out.println(admin);
        if (admin != null) {

            // PediatraDao Dao = new PediatraDao();
            response.put("data", dao.obtenerLista());
        } else {
            response.put("data", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @POST
    @Path("/getrecetas/")
    public String getrecetas() throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");

        if (admin != null) {

            // PediatraDao Dao = new PediatraDao();
            response.put("data", dao.obtenerListaRecetas());

        } else {
            response.put("OK", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @PUT
    @Path("/insertR")
    public String insertR(String data) throws JSONException {

        JSONObject json = new JSONObject(data); //Convierte los datos a JSON
        RecetaTemplate receta = new Gson().fromJson(data, RecetaTemplate.class);

        RecetaDetalle recetaDe = new Gson().fromJson(data, RecetaDetalle.class);
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        RecetaDao DaoR = new RecetaDao();

        Boolean va = false;
        if (admin != null) {
            System.out.println("entra a insertar receta");
            dao.insert(receta, recetaDe);
            va = DaoR.isSuccess();
            System.out.println(va);
            // PediatraDao Dao = new PediatraDao();
            response.put("data", va);
        } else {
            response.put("data", va);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }
    
    @PUT
    @Path("/receta")
    public String insert(RecetaTemplate receta) throws JSONException {

        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        RecetaDao dao = new RecetaDao();

        Boolean va = false;
        if (admin != null) {
            System.out.println(receta.getNombre());
            System.out.println(receta.getDetalles().size());
            
            dao.insert(receta);
            response.put("data", dao.isSuccess());
            if(!dao.isSuccess()) {
                System.out.println(dao.getErrorMessage());
                response.put("error", dao.getErrorMessage());
            }
        } else {
            response.put("data", va);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @PUT
    @Path("/updateR")
    public String updateR(RecetaTemplate receta) throws JSONException {

        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
      //  RecetaDao dao = new RecetaDao();

        Boolean va = false;
        if (admin != null) {
         dao.updateReceta(receta);
            
            response.put("data", dao.isSuccess());
            if(!dao.isSuccess()) {
                System.out.println(dao.getErrorMessage());
                response.put("error", dao.getErrorMessage());
            }
        } else {
            response.put("data", va);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @POST
    @Path("/getDatosById/{id}")
    public String getDatosById(@PathParam("id") int id) throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        RecetaDao dao = new RecetaDao();
        if (admin != null) {
            // PediatraDao Dao = new PediatraDao();
            response.put("data", dao.obtenerReceta(id));
        } else {
            response.put("OK", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @POST
    @Path("/getDetallesById/{id}")
    public String getDetallesById(@PathParam("id") int id) throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        RecetaDao DAO = new RecetaDao();
        if (admin != null) {

            // PediatraDao Dao = new PediatraDao();
            response.put("data", DAO.obtenerLista(id));

        } else {
            response.put("OK", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

    @DELETE
    @Path("/deleteRecetas/{id}")
    public String deleteRecetas(@PathParam("id") int id) throws JSONException, SQLException {
        Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");

        if (admin != null) {

            response.put("data", dao.deleteReceta(id));

        } else {
            response.put("OK", false);
            response.put("mensaje", "Aun no ha iniciado session");
        }
        return new Gson().toJson(response);
    }

     @DELETE
    @Path("/logout")
    public String logout() {
          Administrador admin = (Administrador) request.getSession(false).getAttribute("logged");
        request.getSession(true).removeAttribute("logged");
        request.getSession(true).removeAttribute("lastProvider");
        request = null;
        response.put("OK", true);
        return new Gson().toJson(response);
    }
}