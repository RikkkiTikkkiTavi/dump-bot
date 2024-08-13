package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.MailParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.uri}")
    private String activationServiceUri;

    @Override
    public void send(MailParams mailParams) {
        String subject = "Активация учетной записи";
        String messageBody = getActivateMailBody(mailParams.getId());
        String emailTo = mailParams.getEmailTo();

        SimpleMailMessage mailMessage = new SimpleMailMessage();//спринговый мессаж для рассылки
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        javaMailSender.send(mailMessage);
    }

    private String getActivateMailBody(String id) {
        var msg = String.format("Для завершения регистрации перейдите по ссылке:\n%s",
                activationServiceUri);
        return msg.replace("{id}", id);
    }
}
