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
        message.setSubject("Confirmation de votre inscription à CYStation");
        message.setText("Bonjour " + username + ",\n\nNous sommes ravis de vous ompter parmi nous. Ce message vous confirme que votre inscription à CYStation a bien été validée.\n\n" +
                "La sécurité de la station repose sur une hiérarchie stricte. Vous ne pouvez pas tout contrôler dès le début. Plus vous interagissez avec les systèmes sécurisés, plus vous gagnez de points qui débloquent de nouvelles responsabilités.\n\n" +
                "\uD835\uDDE0\uD835\uDDF2\uD835\uDDFA\uD835\uDDEF\uD835\uDDFF\uD835\uDDF2 \uD835\uDDF1'\uD835\uDDF2́\uD835\uDDFE\uD835\uDE02\uD835\uDDF6\uD835\uDDFD\uD835\uDDEE\uD835\uDDF4\uD835\uDDF2 : Accès aux équipements sans dangers pour la station.\n" +
                "\uD835\uDDE6\uD835\uDDF0\uD835\uDDF6\uD835\uDDF2\uD835\uDDFB\uD835\uDE01\uD835\uDDF6\uD835\uDDF3\uD835\uDDF6\uD835\uDDFE\uD835\uDE02\uD835\uDDF2 : Accès aux outils de surveillance et des ressources vitales. Vous pouvez demander la suppression d'appareils.\n" +
                "\uD835\uDDD4\uD835\uDDF1\uD835\uDDFA\uD835\uDDF6\uD835\uDDFB\uD835\uDDF6\uD835\uDE00\uD835\uDE01\uD835\uDDFF\uD835\uDDEE\uD835\uDE01\uD835\uDDF2\uD835\uDE02\uD835\uDDFF : Contrôle total. Supervision des paramètres critiques et accès au panel exclusif pour gérer l'équipqge.\n\n" +
                "Veuillez cliquer sur le lien pour activer votre compte : " +
                "Spatialement,\n" +
                "CYStation ");



        mailSender.send(message);
        System.out.println("Email envoyé avec succès à " + toEmail);
    }
}
