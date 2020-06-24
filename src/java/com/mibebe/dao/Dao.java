package com.mibebe.dao;

import com.mibebe.util.AppLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mibebe.util.DBConnection;

/**
 * @author Sergio Cabrera
 */
public abstract class Dao {
    private String message;
    
    protected Connection CONNECTION = null;
    protected PreparedStatement PSTM = null;
    protected ResultSet RS = null;
    
    protected Dao(){}

    /**
     * @return Verdadero o Falso si la consulta se realizó con éxito
     */
    public boolean isSuccess() {
        return this.message == null || this.message.trim().isEmpty();
    }

    /**
     * @return Mensaje de error en caso de que haya ocurrido una excepción
     */
    public String getErrorMessage() {
        return message;
    }

    protected void setErrorMessage(String message) {
        this.message = message;
    }
    
    protected void openConnection() throws SQLException {
        message = null;
        CONNECTION = DBConnection.getConnection();
    }
    
    protected void closeConnection() {
        try{RS.close();RS = null;}catch(NullPointerException | SQLException e){}
        try{PSTM.close();PSTM = null;}catch(NullPointerException | SQLException e){}
        try{CONNECTION.close();CONNECTION = null;}catch(NullPointerException | SQLException e){}
    }
    
    /**
     * Selecciona el mensaje de error dependiendo el código de la excepción
     * @param e 
     */
    protected void handleException(Exception e) {    
        if(e instanceof SQLException) {
            
            SQLException ex = (SQLException) e;
            if(ex.getErrorCode() > 5000) {
                setErrorMessage(ex.getMessage());
                return;
            }
            this.message = "Ocurrió un error interno";
            switch(ex.getErrorCode()) {
                case 1062:
                    this.message = "Algún dato ya se había guardado previamente";
                    break;
                case 1048:
                    this.message = "Todos los parámetros deben ser llenados";
                    break;
                case 2627:
                    this.message = "No puedes insertar datos duplicados";
                    break;
                case 2601:
                    this.message = "No puedes insertar datos duplicados";
                    break;
                case 515:
                    this.message = "Algún dato está vacio";
                    break;
                default:
                    this.message = "Ocurrió un error interno\nCódido de error: " + ex.getErrorCode();
                    break;
            }
        }            
        else 
            this.message = "Ocurrió un error: " + e.getMessage();
        AppLog.Log("Dao", message, e);
        //Logger.getLogger("Dao").log(Level.SEVERE, "Error en dao: " + e.getMessage(), e);
    }
}
