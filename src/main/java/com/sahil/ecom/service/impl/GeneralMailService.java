package com.sahil.ecom.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GeneralMailService {
    private final EmailSenderService emailSenderService;
    private final MessageSource messageSource;

    public void sendAccountRegistrationAckForSeller(String email) {
        String emailBody = "ACCOUNT CREATED WAITING FOR APPROVAL";
        String subject = messageSource.getMessage("seller.registered.successful", null, "Ecom Account Activation:", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("seller.register.ack", null, "Your Ecom account is active now..", LocaleContextHolder.getLocale());
        emailSenderService.sendEmail(email, subject, body);
    }


    public void sendAccountActivationAck(String email) {

        String subject = messageSource.getMessage("activation.ack.subject", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("activation.ack.body", null, "message", LocaleContextHolder.getLocale());

        emailSenderService.sendEmail(email, subject, body);

    }


    public void sendAccountDeActivationAck(String email) {

        String subject = messageSource.getMessage("user.account.deactivated", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("user.account.deactivated", null, "message", LocaleContextHolder.getLocale());

        emailSenderService.sendEmail(email, subject, body);

    }


    public void sendAccountActivationUrlCustomer(String email, String url) {

        String subject = messageSource.getMessage("user.account.activation.subject", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("user.account.activation.body", null, "message", LocaleContextHolder.getLocale());

        log.info("ACTIVATION URL" + url);
        emailSenderService.sendEmail(email, subject, body + url);
    }


    public void sendAccountLockedAck(String email) {

        String subject = messageSource.getMessage("user.account.locked.subject", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("user.account.locked.body", null, "message", LocaleContextHolder.getLocale());
        emailSenderService.sendEmail(email, subject, body);

    }


    public void sendForgotPasswordEmail(String email, String url) {

        String subject = messageSource.getMessage("forgot.password.email.subject", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("forgot.password.email.body", null, "message", LocaleContextHolder.getLocale());
        emailSenderService.sendEmail(email, subject, body + url);

    }


    public void sendPasswordUpdateAck(String email) {

        String subject = messageSource.getMessage("user.password.updated.subject", null, "message", LocaleContextHolder.getLocale());
        String body = messageSource.getMessage("user.password.updated", null, "message", LocaleContextHolder.getLocale());
        emailSenderService.sendEmail(email, subject, body);

    }


}
