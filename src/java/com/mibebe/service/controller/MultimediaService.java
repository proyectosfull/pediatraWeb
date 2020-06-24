package com.mibebe.service.controller;

import com.mibebe.bean.util.MediaToken;
import com.mibebe.service.security.Secured;
import com.mibebe.util.Constants;
import com.mibebe.util.Multimedia;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Sergio Cabrera
 */
@Path("/multimedia")
public class MultimediaService {
    
    @Context private ServletContext ctx;
    @Context private ContainerRequestContext containerCtx;
    
    @GET
    @Secured
    @Path("/consulta/{fileName}")
    public Response getFile(@PathParam("fileName") String fileName) throws IOException {
        
        int userId = 5;//((MySecurityContext) containerCtx.getSecurityContext()).getUserId();
        
        Multimedia multimedia = new Multimedia(ctx);
        multimedia.setPath("/users/" + String.valueOf(userId));
        File file = multimedia.getFile(fileName);
        
        if(file == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        return Response.ok(file).type(contentType)
                .header("Content-Disposition", "inline; filename=\"".concat(fileName).concat("\""))
                .build();
    }
    
    @GET
    @Path("/usuario/{id}/{exp}/{token}/{name}")    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoUsuario(@PathParam("id") int id, @PathParam("exp") Long exp, @PathParam("token") String token, @PathParam("name") String name) throws IOException {
        String path = Constants.USUARIO_FOLDER + String.valueOf(id);
        return getArchivo(path, name, token, exp);
    }
    
    @GET
    @Path("/pediatra/{id}/{exp}/{token}/{name}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoPediatra(@PathParam("id") int id, @PathParam("exp") Long exp, @PathParam("token") String token, @PathParam("name") String name) throws IOException {
       
        String path = Constants.PEDIATRA_FOLDER + String.valueOf(id);
        return getArchivo(path, name, token, exp);
    }
    
    @GET
    @Path("/dependiente/{id}/{exp}/{token}/{name}") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoDependiente(@PathParam("id") int id, @PathParam("exp") Long exp, @PathParam("token") String token, @PathParam("name") String name) throws IOException {
       
        String path = Constants.DEPENDIENTE_FOLDER + String.valueOf(id);
        return getArchivo(path, name, token, exp);
    }
    
    @GET
    @Path("/consulta/{id}/{exp}/{token}/{name}") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoConsulta(@PathParam("id") int id, @PathParam("exp") Long exp, @PathParam("token") String token, @PathParam("name") String name) throws IOException {
        String path = Constants.CONSULTA_FOLDER + String.valueOf(id);
        return getArchivo(path, name, token, exp);
    }
    
    @GET
    @Path("/vacuna/{dependienteId}/{fileName}") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoVacuna(@PathParam("dependienteId") int dependienteId, @PathParam("fileName") String fileName) throws IOException {     
        String path = Constants.DEPENDIENTE_FOLDER + String.valueOf(dependienteId);
        Multimedia multimedia = new Multimedia(ctx);
        multimedia.setPath(path);
        File file = multimedia.getFile(fileName);
        
        if(file == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }            
        
        String contentType = Files.probeContentType(file.toPath());//MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        System.out.println(contentType);
        return Response.ok(file).type(contentType)
                .header("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
                .header("Content-Disposition", "inline; filename=\"".concat(fileName).concat("\""))
                .build();
    }
    
    @GET
    @Path("/receta/{dependienteId}/{fileName}") 
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getArchivoReceta(@PathParam("dependienteId") int dependienteId, @PathParam("fileName") String fileName) throws IOException {  
        System.out.println("dfsd");
        String path = Constants.CONSULTA_FOLDER + String.valueOf(dependienteId);
        Multimedia multimedia = new Multimedia(ctx);
        multimedia.setPath(path);
        File file = multimedia.getFile(fileName);
        
        if(file == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }            
        
        String contentType = Files.probeContentType(file.toPath());//MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        System.out.println(contentType);
        return Response.ok(file).type(contentType)
                .header("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
                .header("Content-Disposition", "attachment; filename=\"".concat(fileName).concat("\""))
                .build();
    }
    
    private Response getArchivo(String path, String fileName, String token, Long exp) throws IOException {        
        MediaToken mediaToken = new MediaToken();
        mediaToken.setToken(token);
        mediaToken.setExp(exp);
        
        if( !Multimedia.assertToken(mediaToken) ) {
             return Response.status(Response.Status.UNAUTHORIZED).entity(Multimedia.getErrorMessage()).build();
        }
        
        Multimedia multimedia = new Multimedia(ctx);
        multimedia.setPath(path);
        File file = multimedia.getFile(fileName);
        
        if(file == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }            
        
        String contentType = Files.probeContentType(file.toPath());//MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file);
        System.out.println(contentType);
        return Response.ok(file).type(contentType)
                .header("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0")
                .header("Content-Disposition", "inline; filename=\"".concat(fileName).concat("\""))
                .build();
    }
    
    
}
