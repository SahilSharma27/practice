package com.sahil.ecom.service.impl;


import com.sahil.ecom.RIUtilizationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

//    @Value("classpath:/templates/images/cloudkeeperlogo.jpg")
//    Resource cloudKeeperLogo;
    @Value("images/cloudkeeperlogo.jpg")
    Resource cloudKeeperLogo;

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    @Async
    public void sendEmail(String toEmail,String subject,String body){
        SimpleMailMessage mailMessage =  new SimpleMailMessage();
        mailMessage.setFrom("sahil.sharma@tothenew.com");
        mailMessage.setTo(toEmail);
        mailMessage.setText(body);
        mailMessage.setSubject(subject);

        javaMailSender.send(mailMessage);

    }

    @Async
    public void sendHtmlMessage(List<RIUtilizationDto> riUtilizationList) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        Map<String, Object> properties = new HashMap<>();
//        riUtilizationDetails
        properties.put("riUtilizationDetails",riUtilizationList);
        properties.put("numberOfUtilization",riUtilizationList.size());
        properties.put("name","XYZ");
//        properties.put("cloudkeeperlogo",cloudKeeperLogo);
//        properties.put("subscriptionDate", LocalDate.now().toString());
//        properties.put("technologies", Arrays.asList("Python", "Go", "C#"));
        context.setVariables(properties);
        helper.setFrom("sahil.sharma@tothenew.com");
        helper.setTo("sharma.sahil1560@gmail.com");
        helper.setSubject("TEMP TEST");
        String html = templateEngine.process("temp1.html",context);
        helper.setText(html, true);

        logger.info("Sending email with html body");
        javaMailSender.send(message);
    }


}
