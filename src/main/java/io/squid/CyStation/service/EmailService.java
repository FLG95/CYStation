package io.squid.CyStation.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ton-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Bienvenue parmi nous !");
        message.setText("Bonjour " + username + ",\n\nMerci de vous être inscrit sur notre plateforme.");

        mailSender.send(message);
        System.out.println("Email envoyé avec succès à " + toEmail);
    }
}
