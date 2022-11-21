package com.sahil.Ecom.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Async
    public void sendEmail(String toEmail,String subject,String body){
        SimpleMailMessage mailMessage =  new SimpleMailMessage();
        mailMessage.setFrom("sahil.sharma@tothenew.com");
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        javaMailSender.send(mailMessage);

        logger.info("ACTIVATION MAIL SENT TO" + toEmail);
    }


}
