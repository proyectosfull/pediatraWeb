package com.mibebe.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Genera un hash para la contraseña
 * @author Sergio Cabrera
 */
public abstract class PasswordHash {
    
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 512;
    private static final int SALT_LENGTH = 24;
    
    public final static synchronized String[] hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Genera salt aleatorio
        SecureRandom random = new SecureRandom();        
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt); 
        
        String strSalt = Base64.getEncoder().encodeToString(salt);        
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();        
        String strPwdHashed = Base64.getEncoder().encodeToString(hash);
        
        String[] data = {strSalt, strPwdHashed};
        return data;
    }
    
    public final static synchronized String hash(String password, String strSalt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = Base64.getDecoder().decode(strSalt);
               
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();        
        String strPwdHashed = Base64.getEncoder().encodeToString(hash);
        
        return strPwdHashed;
    }
    
}
