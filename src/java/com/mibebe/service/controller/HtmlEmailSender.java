/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mibebe.service.controller;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author casa
 */
public class HtmlEmailSender {

    public void sendHtmlEmail(String toAddress) throws AddressException{
        
        try {  
            
            // sets SMTP server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "mail.pediatria-innovadora.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("contacto-medicos@pediatria-innovadora.com", "Mail.2020ppda");
                }
            };

            Session session = Session.getInstance(properties, auth);
            session.setDebug(true);

            // creates a new e-mail message
            Message msg = new MimeMessage(session);

            String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\n"
                    + " <head> \n"
                    + "  <meta charset=\"UTF-8\"> \n"
                    + "  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"> \n"
                    + "  <meta name=\"x-apple-disable-message-reformatting\"> \n"
                    + "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \n"
                    + "  <meta content=\"telephone=no\" name=\"format-detection\"> \n"
                    + "  <title>Nueva plantilla de correo electrónico 2020-06-28</title> \n"
                    + "<link href=\"https://fonts.googleapis.com/css?family=Lato:400,400i,700,700i\" rel=\"stylesheet\"> \n"
                    + "  <!--<![endif]--> \n"
                    + "  <style type=\"text/css\">\n"
                    + "@media only screen and (max-width:600px) {p, ul li, ol li, a { font-size:16px!important; line-height:150%!important } h1 { font-size:30px!important; text-align:center; line-height:120%!important } h2 { font-size:26px!important; text-align:center; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } h1 a { font-size:30px!important } h2 a { font-size:26px!important } h3 a { font-size:20px!important } .es-menu td a { font-size:16px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:16px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button { font-size:20px!important; display:block!important; border-width:15px 25px 15px 25px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { display:table-row!important; width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } .es-desk-menu-hidden { display:table-cell!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }\n"
                    + "#outlook a {\n"
                    + "	padding:0;\n"
                    + "}\n"
                    + ".ExternalClass {\n"
                    + "	width:100%;\n"
                    + "}\n"
                    + ".ExternalClass,\n"
                    + ".ExternalClass p,\n"
                    + ".ExternalClass span,\n"
                    + ".ExternalClass font,\n"
                    + ".ExternalClass td,\n"
                    + ".ExternalClass div {\n"
                    + "	line-height:100%;\n"
                    + "}\n"
                    + ".es-button {\n"
                    + "	mso-style-priority:100!important;\n"
                    + "	text-decoration:none!important;\n"
                    + "}\n"
                    + "a[x-apple-data-detectors] {\n"
                    + "	color:inherit!important;\n"
                    + "	text-decoration:none!important;\n"
                    + "	font-size:inherit!important;\n"
                    + "	font-family:inherit!important;\n"
                    + "	font-weight:inherit!important;\n"
                    + "	line-height:inherit!important;\n"
                    + "}\n"
                    + ".es-desk-hidden {\n"
                    + "	display:none;\n"
                    + "	float:left;\n"
                    + "	overflow:hidden;\n"
                    + "	width:0;\n"
                    + "	max-height:0;\n"
                    + "	line-height:0;\n"
                    + "	mso-hide:all;\n"
                    + "}\n"
                    + "</style> \n"
                    + " </head>"
                    + "<body style=\"width:100%;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"> \n"
                    + "  <div class=\"es-wrapper-color\" style=\"background-color:#F4F4F4\"> \n"
                    + "   <!--[if gte mso 9]>\n"
                    + "			<v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\">\n"
                    + "				<v:fill type=\"tile\" color=\"#f4f4f4\"></v:fill>\n"
                    + "			</v:background>\n"
                    + "		<![endif]--> \n"
                    + "   <table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top\"> \n"
                    + "     <tr class=\"gmail-fix\" height=\"0\" style=\"border-collapse:collapse\"> \n"
                    + "      <td style=\"padding:0;Margin:0\"> \n"
                    + "       <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;width:600px\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"padding:0;Margin:0;line-height:1px;min-width:600px\" height=\"0\"><img src=\"https://esputnik.com/repository/applications/images/blank.gif\" style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic;max-height:0px;min-height:0px;min-width:600px;width:600px\" alt width=\"600\" height=\"1\"></td> \n"
                    + "         </tr> \n"
                    + "       </table></td> \n"
                    + "     </tr> \n"
                    + "     <tr style=\"border-collapse:collapse\"> \n"
                    + "      <td valign=\"top\" style=\"padding:0;Margin:0\"> \n"
                    + "       <table cellpadding=\"0\" cellspacing=\"0\" class=\"es-content\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:15px;padding-bottom:15px\"> \n"
                    + "               <!--[if mso]><table style=\"width:580px\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"width:282px\" valign=\"top\"><![endif]--> \n"
                    + "               <table class=\"es-left\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;float:left\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td align=\"left\" style=\"padding:0;Margin:0;width:282px\"> \n"
                    + "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table> \n"
                    + "               <!--[if mso]></td><td style=\"width:20px\"></td><td style=\"width:278px\" valign=\"top\"><![endif]--> \n"
                    + "               <!--[if mso]></td></tr></table><![endif]--></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-header\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%;background-color:#FFA73B;background-repeat:repeat;background-position:center top\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" bgcolor=\"#3d85c6\" style=\"padding:0;Margin:0;background-color:#3D85C6\"> \n"
                    + "           <table class=\"es-header-body\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"Margin:0;padding-bottom:10px;padding-left:10px;padding-right:10px;padding-top:20px\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:580px\"> \n"
                    + "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:25px;padding-bottom:25px;font-size:0\"></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td style=\"padding:0;Margin:0;background-color:#3D85C6\" bgcolor=\"#3d85c6\" align=\"center\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"padding:0;Margin:0\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \n"
                    + "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FFFFFF;border-radius:4px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                     </tr> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td align=\"center\" style=\"Margin:0;padding-bottom:5px;padding-left:30px;padding-right:30px;padding-top:35px\"><h1 style=\"Margin:0;line-height:58px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:48px;font-style:normal;font-weight:normal;color:#111111\">Bienvenido!</h1></td> \n"
                    + "                     </tr> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td bgcolor=\"#ffffff\" align=\"center\" style=\"Margin:0;padding-top:5px;padding-bottom:5px;padding-left:20px;padding-right:20px;font-size:0\"> \n"
                    + "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                         <tr style=\"border-collapse:collapse\"> \n"
                    + "                          <td style=\"padding:0;Margin:0px;border-bottom:1px solid #FFFFFF;background:#FFFFFFnone repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td> \n"
                    + "                         </tr> \n"
                    + "                       </table></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"padding:0;Margin:0\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \n"
                    + "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;border-radius:4px;background-color:#FFFFFF\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" role=\"presentation\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td class=\"es-m-txt-l\" bgcolor=\"#ffffff\" align=\"left\" style=\"Margin:0;padding-top:20px;padding-bottom:20px;padding-left:30px;padding-right:30px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:18px;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;color:#666666\">Al panel de médicos pediatras,que ofrecen sus servicios de consulta <span style=\"color:#FFA07A\"><strong>NO DE URGENCIA</strong></span> o considerada para resolver un estado de gravedad, si no más bien como ayuda y orientación de las primeras medidas para llevarse a cabo en las enfermedades comunes de los niños.<br><br>A través de este medio, también se podrá llevar el <span style=\"color:#FFA07A\"><strong>CONTROL RUTINARIO DEL NIÑO SANO</strong></span>, así como proporcionar a los padres, la orientación suficiente para completar su esquema habitual de vacunación. Para poder realizar el depósito correspondiente de la consulta realizada, solicitamos nos envié la siguiente información al correo <span style=\"color:#3399FF\"><u>pediatriainnovadora@gmail.com</u></span><br></p> \n"
                    + "                       <ol> \n"
                    + "                        <li style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:18px;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;Margin-bottom:15px;color:#666666\">Identificación oficial (archivo PDF)</li> \n"
                    + "                        <li style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-size:18px;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;line-height:27px;Margin-bottom:15px;color:#666666\">Carátula del estado de cuenta bancaria (archivo PDF) donde se muestre la CLABE Interbancaria.</li> \n"
                    + "                       </ol></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"padding:0;Margin:0\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \n"
                    + "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td align=\"center\" style=\"Margin:0;padding-top:10px;padding-bottom:20px;padding-left:20px;padding-right:20px;font-size:0\"> \n"
                    + "                       <table width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                         <tr style=\"border-collapse:collapse\"> \n"
                    + "                          <td style=\"padding:0;Margin:0px;border-bottom:1px solid #F4F4F4;background:#FFFFFFnone repeat scroll 0% 0%;height:1px;width:100%;margin:0px\"></td> \n"
                    + "                         </tr> \n"
                    + "                       </table></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"padding:0;Margin:0\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:600px\"> \n"
                    + "                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:separate;border-spacing:0px;background-color:#FFECD1;border-radius:4px\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffecd1\" role=\"presentation\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:30px;padding-left:30px;padding-right:30px\"><h3 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:lato, 'helvetica neue', helvetica, arial, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#111111\">Enhorabuena!</h3><br><br></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table> \n"
                    + "       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \n"
                    + "         <tr style=\"border-collapse:collapse\"> \n"
                    + "          <td align=\"center\" style=\"padding:0;Margin:0\"> \n"
                    + "           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:transparent;width:600px\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> \n"
                    + "             <tr style=\"border-collapse:collapse\"> \n"
                    + "              <td align=\"left\" style=\"Margin:0;padding-left:20px;padding-right:20px;padding-top:30px;padding-bottom:30px\"> \n"
                    + "               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                 <tr style=\"border-collapse:collapse\"> \n"
                    + "                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"> \n"
                    + "                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \n"
                    + "                     <tr style=\"border-collapse:collapse\"> \n"
                    + "                      <td align=\"center\" style=\"padding:0;Margin:0;display:none\"></td> \n"
                    + "                     </tr> \n"
                    + "                   </table></td> \n"
                    + "                 </tr> \n"
                    + "               </table></td> \n"
                    + "             </tr> \n"
                    + "           </table></td> \n"
                    + "         </tr> \n"
                    + "       </table></td> \n"
                    + "     </tr> \n"
                    + "   </table> \n"
                    + "  </div>  \n"
                    + " </body>"
                    + "</html>";

            msg.setFrom(new InternetAddress("contacto-medicos@pediatria-innovadora.com"));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject("Pediatría Innovadora");
            msg.setSentDate(new Date());
            // set plain tex/plantillaEmail.html"t message
            msg.setContent(message, "text/html");

            // sends the e-mail
            Transport.send(msg);

            System.out.println("Email sent.");
            System.out.println(toAddresses);

        } catch (MessagingException e) {
            System.out.println("Failed to sent email.");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test the send html e-mail method
     *
     */
//    public static void main(String[] args) {
//        HtmlEmailSender mailer = new HtmlEmailSender();
//
//        try {
//            mailer.sendHtmlEmail("yadzziry.arrieta@gmail.com");
//            System.out.println("Email sent.");
//        } catch (Exception ex) {
//            System.out.println("Failed to sent email.");
//            ex.printStackTrace();
//        }
//    }
}
