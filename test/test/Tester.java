/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.mibebe.bean.Usuario;
import com.mibebe.util.JWTHandler;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Sergio Cabrera
 */
public class Tester {
    
    public static void main(String... arg) throws IllegalArgumentException, IllegalAccessException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        /*Usuario u = new Usuario("{id: 5, correo: \"micorreo@correo\", password: \"pass\", sesion: false}");
        System.out.println(u.getId());
        System.out.println(u.getCorreo());
        System.out.println(u.getPassword());
        System.out.println(u.isSesion());*/
//        JWTHandler y = new JWTHandler();
//        y.generateKeyPair();
        
        //Key t = JWTHandler.getPrivateKey();
        //System.out.println(t);
        
        //secure();
              
        System.out.println(UUID.randomUUID().toString().length());
/*byte[] thedigest = md.digest(bytesOfMessage);
StringBuffer sb = new StringBuffer();
        for (int i = 0; i < thedigest.length; ++i) {
          sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1,3));
       }
        System.out.println(sb.toString());*/
    }
    
    public static void secure() throws NoSuchAlgorithmException, InvalidKeySpecException {
         SecureRandom random = new SecureRandom();        
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        
        String saltString = Base64.getEncoder().encodeToString(salt);
        System.out.println(saltString.length());
        
        KeySpec spec = new PBEKeySpec("hetstdhandle".toCharArray(), salt, 65536, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        
        String pass = Base64.getEncoder().encodeToString(hash);
        
        byte[] arr = Base64.getDecoder().decode(pass);
        String pass2 = Base64.getEncoder().encodeToString(arr);
        System.out.println(pass.length());
        System.out.println(pass2);
    }
    
}
