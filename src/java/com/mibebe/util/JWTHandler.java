package com.mibebe.util;

import com.mibebe.bean.Pediatra;
import com.mibebe.bean.Usuario;
import static com.mibebe.util.Constants.CONFIG_BUNDLE;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.servlet.ServletContext;

/**
 * Controlador de JSON Web Tokens
 * @author Sergio Cabrera
 */
public class JWTHandler {
    public static final String TAG = "JWTHandler";
    public static String ALGORITHM = "RSA";
    private static String privName = "private.key";
    private static String pubName = "public.key";
    private static final String CONTROL_KEY_NAME = "ctrl";
    private static final String GROUP_KEY_NAME = "group";
    private static final String USER_GROUP = "5aee7e04";
    private static final String PEDIATRIC_GROUP = "9ee27e04";
    
    private int userId;
    private String jwtControl;
    private String jti;
    private String grupo;
    private Claims payload;

    public Claims getPayload() {
        return payload;
    }
    
    public int getUserId() {
        return this.userId;
    }

    public String getJwtControl() {
        return jwtControl;
    }

    public String getJti() {
        return jti;
    }
    
    public boolean isUsuario() {
        return this.grupo.equals(USER_GROUP);
    }
    
    public boolean isPediatra() {
        return this.grupo.equals(PEDIATRIC_GROUP);
    }
    
    public static String create(ServletContext context, Usuario usuario) {
        return create(context, usuario.getId(), usuario.getJwtControl(), USER_GROUP);
    }
    
    public static String create(ServletContext context, Pediatra pediatra) {
        return create(context, pediatra.getId(), pediatra.getJwtControl(), PEDIATRIC_GROUP);
    }
    
    private static String create(ServletContext context, int id, String jwtControl, String grupo) {
        ResourceBundle config = ResourceBundle.getBundle(CONFIG_BUNDLE);
        String token_expiration = config.getString("token_expiration");
        
        Calendar calendar = Calendar.getInstance();
        Date iat = calendar.getTime();
        calendar.add(Calendar.DATE, Integer.parseInt(token_expiration));
        Date exp = calendar.getTime();
        
        PrivateKey privKey = JWTHandler.getPrivateKey(context.getRealPath("/WEB-INF/"));
        
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .claim(CONTROL_KEY_NAME, jwtControl) //Unico por usuario
                .claim(GROUP_KEY_NAME, grupo) //usuario o pediatra
                .setSubject(String.valueOf(id))                
                .setIssuedAt(iat)
                .setExpiration(exp)                
                .signWith(privKey)
                .compact();
    }
    
    public boolean isValid(ServletContext context, String token) {
            String path = context.getRealPath("/WEB-INF/");
            try {                
                this.payload = Jwts.parser().setSigningKey(JWTHandler.getPublicKey(path)).parseClaimsJws(token).getBody();
                this.userId = Integer.parseInt(payload.getSubject());
                this.jwtControl = payload.get(CONTROL_KEY_NAME).toString();
                this.jti = payload.getId();
                this.grupo = payload.get(GROUP_KEY_NAME).toString();
                return true;
            } catch (JwtException | NumberFormatException ex) {
                AppLog.Log(TAG, "error en método isValid", ex);
                return false;
            }
    }

    /**
     * Generates new key pair
     */
    public void generateKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();

            KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
            RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

            saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
            saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            AppLog.Log(TAG, "Error al intentar generar par de claves", e);
        }
    }

    /**
     * Create new key file
     *
     * @param fileName filename of the key
     * @param mod modul of key
     * @param exp exponent of key
     * @throws IOException When cannot create file
     */
    public void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (IOException e) {
            throw new IOException("Unexpected error", e);
        } finally {
            oout.close();
        }
    }

    /**
     * Obtains public key from file
     *
     * @return Public key
     */
    public static PublicKey getPublicKey(String path) {
        File file = new File(path + pubName);
        System.out.println(file.getAbsolutePath());

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(fis));
            BigInteger mod = (BigInteger) oin.readObject();
            BigInteger exp = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
            KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = fact.generatePublic(keySpec);
            fis.close();
            oin.close();
            return pubKey;
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Spurious serialisation error", e);
        }
    }

    /**
     * Obtains private key from file
     *
     * @return Public key
     */
    public static PrivateKey getPrivateKey(String path) {
        File file = new File(path + privName);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(fis));
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
            PrivateKey privKey = fact.generatePrivate(keySpec);
            fis.close();
            oin.close();
            return privKey;
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Spurious serialisation error", e);
        }
    }
}
