package com.mibebe.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Sergio Cabrera
 * Estructura básica de la respuesta
 */
public class JSONResponse extends JSONObject {
    private final static String OK = "OK";
    private final static String MESSAGE = "message";
    private final static String DATA = "data";
    
    public JSONResponse() throws JSONException{
        super.put(DATA, new JSONObject());
    }
    
    public void setOK(boolean ok) throws JSONException {
        if(super.has(OK))
            super.remove(OK);
        super.put(OK, ok);
    }
    
    public void setMessage(String message) throws JSONException {
        super.put(MESSAGE, message);
    }  
    
    public JSONObject getData() throws JSONException {
        return getJSONObject(DATA);
    }
}
