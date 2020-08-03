package com.mibebe.util;

import com.mibebe.bean.util.FileBase64;
import com.mibebe.bean.util.MediaToken;
import static com.mibebe.util.Constants.CONFIG_BUNDLE;
import static com.mibebe.util.Constants.ROOT_FOLDER;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

/**
 *
 * @author Sergio Cabrera
 */
public class Multimedia {
    public static final String TAG = "Multimedia";
    private static String errorMessage;
    
    private ServletContext context;
    private String path;

    public Multimedia(ServletContext context) {
        this.context = context;
    }

    public void setPath(String path) throws IOException {
        this.path = context.getRealPath(ROOT_FOLDER).concat(path);
        File directory = new File(this.path);
        if (!directory.exists()) {
            Path dir = Paths.get(this.path);
            Files.createDirectory(dir);
        }
    }
    
    public static String getErrorMessage() {
        return errorMessage;
    }

    public List<String> createFiles(List<FormDataBodyPart> bodyParts) {
        List<String> nombreArchivos = new ArrayList();
        BodyPartEntity bodyPartEntity;
        String fileName;
        String[] helper;
        for (int i = 0; i < bodyParts.size(); i++) {
            try {
                bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
                helper = bodyParts.get(i).getContentDisposition().getFileName().split("\\.");
                fileName = helper[0] + "__" + String.valueOf(System.nanoTime()) + "." +  helper[1];

                Files.copy(bodyPartEntity.getInputStream(), Paths.get(path.concat("\\").concat(fileName)));
                bodyPartEntity.close();
                nombreArchivos.add(fileName);
            } catch (IOException e) {
                AppLog.Log(TAG, "Error al intentar crear archivos", e);
                System.out.println(e.getMessage());
            }
        }
        return nombreArchivos;
    }

    public String createFile(FormDataBodyPart bodyPart, String fileName) throws IOException {
        try {
            BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
            String extension = bodyPart.getContentDisposition().getFileName().split("\\.")[1];

            StringBuilder builder = new StringBuilder();
            String fullPath = builder.append(path)
                    .append("\\")
                    .append(fileName)
                    .append(".")
                    .append(extension)
                    .toString();

            Files.deleteIfExists(Paths.get(fullPath));
            Files.copy(bodyPartEntity.getInputStream(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
            bodyPartEntity.close();
            return fileName + "." + extension;
        } catch (IOException e) {
            AppLog.Log(TAG, "Error al intentar crear archivo", e);
            return null;
        }
    }

    public String createFile(FormDataBodyPart bodyPart) {
        String nombreArchivo = null;
        try {
            BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
            
            String[] helper = bodyPart.getContentDisposition().getFileName().split("\\.");
            String fileName = helper[0] + String.valueOf(System.nanoTime()) + "." + helper[1];
            
            StringBuilder builder = new StringBuilder();
            String fullPath = builder.append(path)
                    .append("/")
                    .append(fileName)
                    .toString();

            Files.copy(bodyPartEntity.getInputStream(), Paths.get(fullPath));
            bodyPartEntity.close();
            nombreArchivo = fileName;
        } catch (IOException e) {
            AppLog.Log(TAG, "Error al intentar crear archivo", e);
        }
        return nombreArchivo;
    }

    public File getFile(String fileName) {
        String path = this.path.concat("\\").concat(fileName);
        System.err.println("PATH: " + path);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    public String createFile(final FileBase64 fileData) {
        String fileName;
        try {
            byte[] fileByteArray = java.util.Base64.getDecoder().decode(fileData.getData());
            
            fileName = fileData.getName() + "__" + String.valueOf(System.currentTimeMillis());
            
            String fullPath = new StringBuilder()
                    .append(path)
                    .append("\\")
                    .append(fileName)
                    .append(".")
                    .append(fileData.getExtension())
                    .toString();

            try (FileOutputStream out = new FileOutputStream(fullPath)) {
                out.write(fileByteArray);
                out.flush();
                System.out.println("DOCUMENTO CREADO CORRECTAMENTE");
            }
            System.err.println("FullPath: " + fullPath);
            System.err.println("Return: " + fileName + "." + fileData.getExtension());
            return fileName + "." + fileData.getExtension();
        } catch (IOException e) {
            AppLog.Log(TAG, "Error al intentar crear archivo", e);
        }
        return null;
    }
    
    public static MediaToken createToken() {
        ResourceBundle config = ResourceBundle.getBundle(CONFIG_BUNDLE);
        String secret = config.getString("secret");
        int exp = Integer.parseInt(config.getString("media_token_expiration"));
        
        Long expire = System.currentTimeMillis() + (exp * 60 * 60 * 1000);

        try {
            String urlData = secret + expire.toString();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(urlData.getBytes());
            String token = Base64.getEncoder().encodeToString(messageDigest).replace("=", "").replace("/", "");
            
            MediaToken mediaToken = new MediaToken();
            mediaToken.setToken(token);
            mediaToken.setExp(expire);
            return mediaToken;
        } catch (NoSuchAlgorithmException e) {
            AppLog.Log(TAG, "Error al intentar crear token de multimedia", e);
            return null;
        }
    }
    
    public static boolean assertToken(MediaToken mediaToken) {
        boolean result = false;
        if(System.currentTimeMillis() >= mediaToken.getExp()) {
            errorMessage = "El token ha expirado";
            return result;
        }
        
        ResourceBundle config = ResourceBundle.getBundle(CONFIG_BUNDLE);
        String secret = config.getString("secret");
        
        try {
            String urlData = secret + mediaToken.getExp().toString();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(urlData.getBytes());
            String originalToken = Base64.getEncoder().encodeToString(messageDigest).replace("=", "").replace("/", ""); 
            result = mediaToken.getToken().equals(originalToken);
            if(!result) {
                errorMessage = "El token es inválido";
            }
        } catch (NoSuchAlgorithmException e) {
            AppLog.Log(TAG, "Error al intentar crear MessageDigest con algoritmo SHA-512 para comparar token", e);
        }
        return result;
    }
}