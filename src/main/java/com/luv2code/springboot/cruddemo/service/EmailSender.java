package com.luv2code.springboot.cruddemo.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.AccountJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AccountJpaRepo accountJpaRepo;

    private String createRandomTemporaryPassword(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public  Account  sendEmail(String email) throws MessagingException, IOException {
        String temporaryPassword = createRandomTemporaryPassword();
        Account account = accountJpaRepo.findByEmail(email);
        if(account == null){
            throw new NotFoundException("email doesn't exist");
        }
        account.setTemporaryPassword(temporaryPassword);
        account.setTemporaryPasswordExpiryDate(new Date(System.currentTimeMillis()));
        accountJpaRepo.save(account);
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(email);
        helper.setFrom("tonykhoury@gmail.com");
        helper.setSubject("reset password ");
        helper.setText("your temporary password is " + temporaryPassword );

        javaMailSender.send(msg);
        return account;
    }

    private ByteArrayOutputStream createInMemoryDocument(byte[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data);
        return outputStream;
    }

    public void sendEmailWithAttachment( String email , String subject , String text) throws MessagingException, DocumentException {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(msg, true);
          helper.setTo(email);
          helper.setFrom("tonykhoury@gmail.com");
          helper.setSubject(subject);
          helper.setText(text);
          helper.addAttachment("the-post.pdf", createPdfDocumentToBeAttached(text));
          javaMailSender.send(msg);
      }catch (Exception e){
        throw new InternalError("cannot send email with attachment");
      }
    }

    //could be implemented also for excel document
   public InputStreamSource createPdfDocumentToBeAttached(String text) throws DocumentException {
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       Document document = new Document(PageSize.A4);
       PdfWriter.getInstance(document , baos);
       document.open();
       document.add(new Paragraph("you have added a new post" + "\n"  + "postText: " + text ));
       document.close();
        InputStreamSource attachment = new ByteArrayResource(baos.toByteArray());
       return attachment;

   }
}
