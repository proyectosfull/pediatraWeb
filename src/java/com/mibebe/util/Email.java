package com.mibebe.util;

import java.util.Properties;
import java.util.ResourceBundle;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * @author Sergio Cabrera Cirilo
 */
public class Email {

    private String from;
    private String password;
    private String[] to = null;
    private String subject;
    private String content;
    private String serverHost;

    private String starttls;
    private String host;
    private String auth;
    private String port;
    private String name;

    public Email() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.mibebe.properties.mail");
        if (bundle != null) {
            this.subject = bundle.getString("subject");
            this.from = bundle.getString("mail_address");
            this.password = bundle.getString("mail_password");
            this.starttls = bundle.getString("starttls");
            this.host = bundle.getString("host");
            this.auth = bundle.getString("auth");
            this.port = bundle.getString("port");
            this.name = bundle.getString("name");
            this.serverHost = bundle.getString("server_host");
        }

    }

    public void send() throws Exception {
        Properties props = new Properties();
        //GMAIL
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        
        session.setDebug(false);
        
        Address[] destinos = new Address[this.to.length];//Aqui usamos el arreglo de destinatarios
        if (this.to.length == 0) {
            throw new Exception("No Hay Destinatarios");
        }

        for (int i = 0; i < destinos.length; i++) {
            destinos[i] = new InternetAddress(this.to[i]);
        }

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.from, name));
        message.setRecipients(Message.RecipientType.TO, destinos);
        message.setSubject(this.subject);

        MimeMultipart multiPart = new MimeMultipart();
        BodyPart texto = new MimeBodyPart();
        texto.setContent(this.content, "text/html;charset=UTF-8");
        multiPart.addBodyPart(texto);

        /*if (!(this.image == null || this.image.isEmpty())) {
            try {
                String delimiter = this.image.contains("\\") ? "\\" : "/";
                String[] array = this.image.split(delimiter);
                String fileName = array[array.length - 1];
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(this.image)));
                adjunto.setFileName(fileName);
                multiPart.addBodyPart(adjunto);
            } catch (Exception ex) {
                System.out.println("Error en Imagen");
            }
        }*/
        message.setContent(multiPart);

        Transport.send(message);
        System.out.println("OK");
    }


    /**
     * @param to the to to set
     */
    public void setTo(String... to) {
        this.to = to;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        String fontColor = "#00AE65";
        String fontColor2 = "#5E5E5E";
        String url = this.serverHost + "/?token=" + token;
        
        this.content = new StringBuilder()
                .append("<h3>Reestablecer contraseña</h3>")
                .append("<p>Este enlace será valido durante 24hr, despues de ese periodo deberás solicitar una nueva</p>")
                .append("<a href=\"").append(url).append("\">")
                .append(url).append("</a>")
                .toString();        
    }

    public static void main(String arg[]) {
        try {
            Email email = new Email();
            email.setTo("cabrera-627@hotmail.com");
            email.setToken("asdsadsadsadasdasd");
            email.send();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
