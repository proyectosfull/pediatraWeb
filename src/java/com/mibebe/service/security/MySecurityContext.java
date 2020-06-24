package com.mibebe.service.security;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Sergio Cabrera
 */
public class MySecurityContext implements SecurityContext {
    
    private final String authScheme;
    private final int userID;
    private final SecurityContext currentSecurityContext;
    
    public MySecurityContext(SecurityContext currentSecurityContext, String authScheme, int userID) {
        this.currentSecurityContext = currentSecurityContext;
        this.authScheme = authScheme;
        this.userID = userID;
    }
    
    public int getUserId() {
        return this.userID;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isUserInRole(String string) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return currentSecurityContext.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return this.authScheme;
    }
    
}
