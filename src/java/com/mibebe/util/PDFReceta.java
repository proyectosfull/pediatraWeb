package com.mibebe.util;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.mibebe.bean.RecetaDetalle;
import com.mibebe.bean.util.ResumenConsulta;
import static com.mibebe.util.Constants.ROOT_FOLDER;
import static com.mibebe.util.Constants.PEDIATRA_FOLDER;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.ServletContext;

public class PDFReceta {
    private final static String TAG = "PDFReceta";
    private ServletContext context;
    private String path;

    public PDFReceta(ServletContext context) {
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

    public String crearReceta(ResumenConsulta consulta) {
        String nombre = "receta__" + System.nanoTime() + ".pdf";
        try {

            Document document = new Document(PageSize.LETTER);

            List<String> lines = Files.readAllLines(Paths.get(context.getRealPath("/WEB-INF/receta.html")));
            String content = String.join("", lines);
            content = content.replace("{{pediatra}}", consulta.getPediatra().getNombreCompleto());
            content = content.replace("{{lugar_estudios}}", consulta.getPediatra().getLugarEstudios());
            content = content.replace("{{cedula_profesional}}", consulta.getPediatra().getCedula());
            content = content.replace("{{cecula_especialidad}}", consulta.getPediatra().getCedulaEspecialidad());
            content = content.replace("{{cmcp}}", consulta.getPediatra().getCmcp());
            content = content.replace("{{paciente}}", consulta.getDependiente().getNombreCompleto());
            content = content.replace("{{fecha}}", consulta.getFecAtendido());
            content = content.replace("{{peso}}", String.valueOf(consulta.getPeso()) + " KG");
            content = content.replace("{{temperatura}}", String.valueOf(consulta.getTemperatura()) + "°");
            content = content.replace("{{notas_pediatra}}", consulta.getNotaPediatra());

            List<RecetaDetalle> medicamentos = consulta.getReceta().getDetalles();
            StringBuilder builder = new StringBuilder();
            medicamentos.forEach((detalle) -> {
                builder.append("<tr>")
                        .append("<td>").append(detalle.getMedicamento()).append("</td>")
                        .append("<td>").append(detalle.getDosis()).append("</td>")
                        .append("<td>").append(detalle.getDuracion()).append("</td>")
                        .append("<td>").append(detalle.getIntervalo()).append("</td>")
                        .append("</tr>");
            });
            content = content.replace("{{medicamentos}}", builder.toString());

            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(this.path + "/" + nombre));
            document.open();
            document.addAuthor("Pediatría Innovadora");
            document.addCreator("Pediatría Innovadora");
            document.addSubject("Receta");
            document.addCreationDate();
            document.addTitle("Receta");

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new StringReader(content));

            addLogo(document);
            addQRCode(document, consulta.getReceta().getQrcode());
            if( consulta.getPediatra().getFirma() != null)
                addFirma(document, consulta.getPediatra().getId() + "/" + consulta.getPediatra().getFirma());
            
            ColumnText ct = new ColumnText(pdfWriter.getDirectContent());
            Phrase myText = new Phrase(consulta.getPediatra().getCorreo(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            ct.setSimpleColumn(myText, 0, 0, 580, document.bottomMargin(), 15, Element.ALIGN_CENTER);
            ct.go();

            document.close();
        } catch (DocumentException | IOException e) {
            System.out.println(e.getMessage());
            nombre = null;
            AppLog.Log(TAG, "Error al intentar crear receta", e);
        }
        return nombre;
    }

    private void addLogo(Document document) {
        try {
            Image image = Image.getInstance(context.getRealPath("/WEB-INF/logo.png"));
            float size = (float) ((document.getPageSize().getWidth() - (document.leftMargin() + document.rightMargin())) * .20);
            float marginTop = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin() - (size / 2);

            image.scaleAbsolute(size, size);
            image.setAbsolutePosition(document.leftMargin(), marginTop);
            document.add(image);
        } catch(DocumentException | IOException e) {
            AppLog.Log(TAG, "Añadir Logo", e);
        }
    }

    private void addQRCode(Document document, String code) {        
        try {
            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(code, 170, 170, null);
            Image codeQrImage = barcodeQRCode.getImage();
            codeQrImage.setAbsolutePosition(document.leftMargin(), document.bottomMargin() + 15);
            document.add(codeQrImage);
        } catch(DocumentException e) {
            AppLog.Log(TAG, "Añadir QR", e);
        }
    }

    private void addFirma(Document document, String pathFirma) {
        try {
            Image firma = Image.getInstance(context.getRealPath(ROOT_FOLDER + PEDIATRA_FOLDER + pathFirma));
            float h = 140;
            float w = firma.getWidth() - (firma.getHeight() - 140);
            firma.scaleAbsolute(w, h);
            firma.setAbsolutePosition(document.getPageSize().getWidth() - document.rightMargin() - w, document.bottomMargin() + 15);
        } catch(BadElementException | IOException e) {
            AppLog.Log(TAG, "Añadir firma", e);
        }
    }
}
