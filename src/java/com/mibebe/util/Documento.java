package com.mibebe.util;
/*
import com.google.common.collect.Range;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
*/

public class Documento {
/*
    public static void main(String... arg) throws InvalidFormatException, IOException, DocumentException {
        XWPFDocument docx = new XWPFDocument(OPCPackage.open("C:/Users/GS-Server/Desktop/test.docx"));
        PdfOptions options = PdfOptions.create();
         OutputStream out = new FileOutputStream(new File(""));
         PdfConverter.getInstance(docx, out, options);
        
         
        /*XWPFDocument doc = new XWPFDocument(OPCPackage.open("input.docx"));
        
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("needle")) {
                        text = text.replace("needle", "haystack");
                        r.setText(text, 0);
                    }
                }
            }
        }
        PdfWriter writer = PdfWriter.getInstance(document, file);  
        doc.write(new FileOutputStream("output.docx"));
        XWPFDocument docx = new XWPFDocument(OPCPackage.open("C:/Users/GS-Server/Desktop/test.docx"));
        // using XWPFWordExtractor Class
        XWPFWordExtractor we = new XWPFWordExtractor(docx);
        
        String k = we.getText();
        System.out.println(k);
        OutputStream  fileForPdf = new FileOutputStream(new File("C:/Users/GS-Server/Desktop/test.pdf"));
        we.close();
        
        Document document = new Document();
            PdfWriter.getInstance(document, fileForPdf);

            document.open();

            document.add(new Paragraph(k));

            document.close();
            fileForPdf.close();
    }
    */
   
}
