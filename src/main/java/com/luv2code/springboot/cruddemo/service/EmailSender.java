package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.AccountJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
}
