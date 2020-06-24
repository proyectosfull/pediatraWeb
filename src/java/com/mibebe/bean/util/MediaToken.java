package com.mibebe.bean.util;

/**
 *
 * @author Sergio Cabrera
 */
public class MediaToken {
    private String token;
    private Long exp;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token != null ? token.replaceAll(" ", "+") : token;
    }

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }
}
