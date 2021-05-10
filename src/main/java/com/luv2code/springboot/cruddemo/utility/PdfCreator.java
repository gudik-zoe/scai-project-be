package com.luv2code.springboot.cruddemo.utility;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.luv2code.springboot.cruddemo.dto.Base64DTO;
import com.luv2code.springboot.cruddemo.entity.Post;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class PdfCreator {

    private List<Post> posts;

    public PdfCreator(){

    }

    public PdfCreator(List<Post> posts) {
        this.posts = posts;
    }

    public void pdfCreatorInResponse(HttpServletResponse response) throws DocumentException, IOException, IllegalAccessException, TransformerException {

        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=listAccounts.pdf ";
        response.setHeader(headerKey, headerValue);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        for (Post post : posts) {
            document.add(new Paragraph(post.getIdPost() + " " + post.getText() + " " + post.getImage()));
        }
        document.close();
    }

    public Base64DTO pdfCreatorInBase64() throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();
        for (Post post : posts) {
            document.add(new Paragraph(post.getIdPost() + " " + post.getText() + " " + post.getImage()));
        }
        document.close();
        Base64DTO base64DTO = new Base64DTO("listAccounts" , Base64.encodeBase64String(out.toByteArray()) , MediaType.APPLICATION_PDF.toString());
            return base64DTO;
    }




}
