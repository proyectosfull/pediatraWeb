package com.mibebe.service.controller;

import com.mibebe.bean.Pediatra;
import com.mibebe.bean.Usuario;
import com.mibebe.bean.util.MediaToken;
import com.mibebe.dao.PediatraDao;
import com.mibebe.dao.UsuarioDao;
import com.mibebe.dao.UtilDao;
import com.mibebe.util.JSONResponse;
import com.mibebe.util.JWTHandler;
import java.io.IOException;
import javax.servlet.ServletContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static com.mibebe.service.security.AuthenticationFilter.AUTHENTICATION_SCHEME;
import static com.mibebe.service.security.AuthenticationFilter.isTokenBasedAuthentication;
import com.mibebe.service.security.MySecurityContext;
import com.mibebe.service.security.Secured;
import com.mibebe.util.AppLog;
import com.mibebe.util.Multimedia;
import java.sql.SQLException;
import javax.ws.rs.GET;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

/**
 * @author Sergio Cabrera
 */
@Path("/auth")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class Auth {
    private static final String TAG = "Auth";
    @Context private ServletContext context;
    @Context private ContainerRequestContext containerRequest;

    @POST
    @Path("/usuario/login") 
    public Response loginUsuario(Usuario usuario) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            UsuarioDao dao = new UsuarioDao();
            
            dao.login(usuario);
            
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess())  {
                jres.setMessage(dao.getErrorMessage());
                return Response.ok(jres.toString()).build();
            }
            
            MediaToken mediaToken = Multimedia.createToken();
            
            String jws = JWTHandler.create(context, usuario);
            usuario.setJwtControl(null);
            jres.getData().put("token", jws);
            jres.getData().put("usuario", new JSONObject(usuario));
            jres.getData().put("media_token", new JSONObject(mediaToken));
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @POST
    @Path("/usuario/login/fb") 
    public Response loginUsuarioFacebook(Usuario usuario) throws IOException {
        try {
            JSONResponse jres = new JSONResponse();
            UsuarioDao dao = new UsuarioDao();
            usuario.setPassword(String.valueOf(System.currentTimeMillis()));
            dao.loginViaFacebook(usuario);
            
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess())  {
                jres.setMessage(dao.getErrorMessage());
                return Response.ok(jres.toString()).build();
            }
            
            MediaToken mediaToken = Multimedia.createToken();
            
            String jws = JWTHandler.create(context, usuario);
            usuario.setJwtControl(null);
            jres.getData().put("token", jws);
            jres.getData().put("usuario", new JSONObject(usuario));
            jres.getData().put("media_token", new JSONObject(mediaToken));
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @POST
    @Path("/usuario/logout")
    public Response logoutUsuario(@HeaderParam(AUTHORIZATION) String authorization) throws IOException, JSONException {
        System.out.println("Auth: " + authorization);
        if (!isTokenBasedAuthentication(authorization))
            return Response.status(Response.Status.BAD_REQUEST).build();
        
        String token = authorization.substring(AUTHENTICATION_SCHEME.length()).trim();
        JWTHandler jwt = new JWTHandler();            
        if(!jwt.isValid(context, token))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        try {
            JSONResponse jres = new JSONResponse();
            UtilDao dao = new UtilDao();
            dao.putJWTInBlackList(jwt.getJti(), jwt.getPayload().getExpiration().getTime());
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess()) {
                System.out.println(dao.getErrorMessage());
                jres.setMessage("Ocurrió un error, vuelve a intentarlo mas tarde");
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    /*@GET
    @Path("/usuario/password-restore?token={token}") 
    public Response loginUsuario(@PathParam("token") String token) throws IOException {
        JWTHandler jwt = new JWTHandler();        
        if(!jwt.isValid(context, token))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        
    }*/
    
    @POST
    @Secured
    @Path("/usuario/password-restore")
    public Response cambiarPassUsuario(String data) throws IOException {
        int idUsuario = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONObject jdata = new JSONObject(data);
            
            Usuario usuario = new Usuario();
            usuario.setId(idUsuario);
            usuario.setPassword(jdata.getString("current_password"));
            
            UsuarioDao dao = new UsuarioDao();
            dao.actualizarContrasena(usuario, jdata.getString("new_password"));
            jdata.remove("current_password");
            jdata.remove("new_password");
            if(dao.isSuccess()) {                
                jdata.put("OK", true);                
            } else {
                jdata.put("OK", false);
                jdata.put("message", dao.getErrorMessage());
            }
            return Response.ok(jdata.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("la entrada de datos no es correcta").build();
        }
    }
    
    @POST
    @Path("/pediatra/login") 
    public Response loginPediatra(Pediatra pediatra) throws IOException, SQLException {
        try {
            JSONResponse jres = new JSONResponse();
            PediatraDao dao = new PediatraDao();
            
            dao.login(pediatra);
            
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess())  {
                jres.setMessage(dao.getErrorMessage());
                return Response.ok(jres.toString()).build();
            }
            
            MediaToken mediaToken = Multimedia.createToken();
            
            String jws = JWTHandler.create(context, pediatra);
            pediatra.setJwtControl(null);
            jres.getData().put("token", jws);
            jres.getData().put("pediatra", new JSONObject(pediatra));
            jres.getData().put("media_token", new JSONObject(mediaToken));
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    
    @POST
    @Path("/pediatra/logout")
    public Response logoutPediatra(@HeaderParam("authorization") String authorization) throws IOException, JSONException {
        if (!isTokenBasedAuthentication(authorization))
            return Response.status(Response.Status.BAD_REQUEST).build();
        
        String token = authorization.substring(AUTHENTICATION_SCHEME.length()).trim();
        JWTHandler jwt = new JWTHandler();            
        if(!jwt.isValid(context, token))
            return Response.status(Response.Status.UNAUTHORIZED).build();
        
        try {
            JSONResponse jres = new JSONResponse();
            UtilDao dao = new UtilDao();
            dao.putJWTInBlackList(jwt.getJti(), jwt.getPayload().getExpiration().getTime());
            jres.setOK(dao.isSuccess());
            if(!dao.isSuccess()) {
                jres.setMessage("Ocurrió un error, vuelve a intentarlo mas tarde");
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.serverError().build();
        }
    }
    
    @POST
    @Secured
    @Path("/pediatra/password-restore")
    public Response cambiarPassPediatra(String data) throws IOException {
        int idPediatra = ((MySecurityContext) containerRequest.getSecurityContext()).getUserId();
        try {
            JSONObject jdata = new JSONObject(data);
            
            Pediatra pediatra = new Pediatra();
            pediatra.setId(idPediatra);
            pediatra.setPassword(jdata.getString("current_password"));
            
            PediatraDao dao = new PediatraDao();
            dao.actualizarContrasena(pediatra, jdata.getString("new_password"));
            jdata.remove("current_password");
            jdata.remove("new_password");
            
            if(dao.isSuccess()) {                
                jdata.put("OK", true);                
            } else {
                jdata.put("OK", false);
                jdata.put("message", dao.getErrorMessage());
            }
            return Response.ok(jdata.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error al generar json", e);
            return Response.status(Response.Status.BAD_REQUEST).entity("la entrada de datos no es correcta").build();
        }
    }
    
    @GET
    @Secured
    @Path("/mediatoken")
    public Response crearMediaToken() {
        try {
            JSONResponse jres = new JSONResponse();
            MediaToken mediaToken = Multimedia.createToken();
            jres.setOK(mediaToken != null);
            if(mediaToken != null) {
                jres.getData().put("token", mediaToken.getToken());
                jres.getData().put("exp", mediaToken.getExp());
            } else {
                jres.setMessage("Ocurrió un error al intentar generar token");
            }
            return Response.ok(jres.toString()).build();
        } catch(JSONException e) {
            AppLog.Log(TAG, "Error en json", e);
            return Response.serverError().build();
        }
    }
    
    
}
