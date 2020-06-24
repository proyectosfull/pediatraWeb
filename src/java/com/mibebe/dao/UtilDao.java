package com.mibebe.dao;

import java.sql.SQLException;
import java.sql.Date;

/**
 * Consultas en general
 * @author Sergio Cabrera
 */
public class UtilDao extends Dao {
    
    /**
     * Añade el token a la lista negra
     * @param jti id unico de token
     * @param exp fecha de expiración del token
     */
    public void putJWTInBlackList(String jti, long exp) {
        try {
            openConnection();
            PSTM = CONNECTION.prepareStatement("INSERT INTO jwt_blacklist(jti, exp) VALUES(?,?)");
            PSTM.setString(1, jti);
            PSTM.setDate(2, new Date(exp));
            if(PSTM.executeUpdate() < 1)
                setErrorMessage("Ninguna fila fué afectada");
        } catch(SQLException e) {
            setErrorMessage(e.getMessage());
        } finally {
            closeConnection();
        }
    }
}
