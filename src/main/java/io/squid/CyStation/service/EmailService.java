package io.squid.CyStation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String username, String token) {
        String activationLink = "http://localhost:8080/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ton-email@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Confirmation de votre inscription à CYStation");
        message.setText(
                "Bonjour " + username + ",\n\n" +
                        "Nous sommes ravis de vous compter parmi nous. Ce message vous confirme que votre inscription à CYStation a bien été prise en compte.\n\n" +
                        "La sécurité de la station repose sur une hiérarchie stricte. Vous ne pouvez pas tout contrôler dès le début. " +
                        "Plus vous interagissez avec les systèmes sécurisés, plus vous gagnez de points qui débloquent de nouvelles responsabilités.\n\n" +
                        "𝗠𝗲𝗺𝗯𝗿𝗲 𝗱'é𝗾𝘂𝗶𝗽𝗮𝗴𝗲 : Accès aux équipements sans dangers pour la station.\n" +
                        "𝗦𝗰𝗶𝗲𝗻𝘁𝗶𝗳𝗶𝗾𝘂𝗲 : Accès aux outils de surveillance et des ressources vitales.\n" +
                        "𝗔𝗱𝗺𝗶𝗻𝗶𝘀𝘁𝗿𝗮𝘁𝗲𝘂𝗿 : Contrôle total. Supervision des paramètres critiques.\n\n" +
                        "Cliquez sur le lien ci-dessous pour activer votre compte :\n" +
                        activationLink + "\n\n" +
                        "Spatialement,\n" +
                        "CYStation"
        );

        mailSender.send(message);
        System.out.println("Email envoyé avec succès à " + toEmail);
    }
}