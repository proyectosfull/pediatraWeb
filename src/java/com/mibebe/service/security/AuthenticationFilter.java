package com.mibebe.service.security;

import com.mibebe.dao.PediatraDao;
import com.mibebe.dao.UsuarioDao;
import com.mibebe.util.JWTHandler;
import java.io.IOException;
import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 * @author Sergio Cabrera
 */

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    
    @Context ServletContext context;
    public static final String AUTHENTICATION_SCHEME = "Bearer";
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        //Se valida que la autenticación sea Bearer
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        //Obtiene el token del header
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            //Si no es válido aborta
            JWTHandler jwt = new JWTHandler();            
            if(!jwt.isValid(context, token))
                abortWithUnauthorized(requestContext);   
            
            if(!isValidUser(jwt))
                abortWithUnauthorized(requestContext); 
            
            SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            
            requestContext.setSecurityContext(new MySecurityContext(currentSecurityContext, AUTHENTICATION_SCHEME, jwt.getUserId()));
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }
    }
    
    public static boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
     
    private boolean isValidUser(JWTHandler jwt) {
        if(jwt.isUsuario()) {
            UsuarioDao dao = new UsuarioDao();
            dao.validarUsuario(jwt.getUserId(), jwt.getJwtControl(), jwt.getJti());
            return dao.isSuccess();
        } else if(jwt.isPediatra()) {
            PediatraDao dao = new PediatraDao();
            dao.validarUsuario(jwt.getUserId(), jwt.getJwtControl(), jwt.getJti());
            return dao.isSuccess();
        }
        return false;
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    
}
