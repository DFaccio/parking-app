package br.com.gramado.parkingapp.service.email;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
class EmailService implements EmailServiceInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Resource
    private JavaMailSender javaMailSender;

    private static final String TITLE = "Parking App - ";

    public void sendEmail(String email, String subject, String message) {
        String title = TITLE + subject;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(title);
        mailMessage.setText(message);

        try {
            javaMailSender.send(mailMessage);

            LOGGER.info("{}: e-mail enviado para {}", title, email);
        } catch (MailException exception) {
            LOGGER.error("Error on send e-mail to {}" + email, exception);
        }
    }
}
