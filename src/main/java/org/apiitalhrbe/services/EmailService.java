package org.apiitalhrbe.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Data
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    public boolean sendEmail(String emailTo, String token) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject("Restablecer contraseña");

            String text = "¡Hola! 😊\n\n"
                    + "Hemos recibido una solicitud para restablecer tu contraseña.\n\n"
                    + "Tu token temporal es: " + token + " 🔑\n\n"
                    + "Este token es válido durante los próximos 5 minutos. ⏰\n\n"
                    + "Si no has solicitado un restablecimiento de contraseña, por favor ignora este correo electrónico. 🚫\n\n"
                    + "Para mayor seguridad, te recomendamos que no compartas este token con nadie. 🔒\n"
                    + "Si tienes alguna pregunta o necesitas ayuda, no dudes en contactarnos. 📞\n\n"
                    + "¡Saludos cordiales! 🤗\n"
                    + "El equipo de ITALHR";
            helper.setText(text);
            javaMailSender.send(message);
            return true;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
