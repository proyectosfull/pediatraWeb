package com.mibebe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @author Sergio Cabrera
 */
public class DBConnection {

    private static String ipAddress; 
    private static String dbName;
    private static String user;
    private static String password;
    private static String port;
    private static ResourceBundle properties;
    
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if(properties == null) {
                properties = ResourceBundle.getBundle("com.mibebe.properties.db");
                ipAddress = properties.getString("ipAddress");
                dbName = properties.getString("dbName");
                user = properties.getString("user");
                password = properties.getString("password");
                port = properties.getString("port");
            }        
            String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + dbName + "?serverTimezone=America/Mexico_City";
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String... arg) throws SQLException {
        try {
            System.out.println(getConnection() == null ? "error" : "ok");
        } catch(SQLException e) {
            //AppLog.Log(null, "test", e);
        }
//        SecureRandom s = new SecureRandom();
//        byte[] g = s.generateSeed(1024);        
//        System.out.println(Base64.getEncoder().encodeToString(g));
        
        /*for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
            String key = (String) e.getKey();
            String value = (String) e.getValue();
            System.out.println(key + ": " + value);
          }*/
    }
}