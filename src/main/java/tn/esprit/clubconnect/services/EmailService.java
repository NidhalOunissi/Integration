package tn.esprit.clubconnect.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tn.esprit.clubconnect.entities.EmailTemplateName;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Async
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject

    ) throws MessagingException {
        String templateName = null;

        if (templateName != null) {
            templateName="emailTemplate.name";
        } else {
            templateName="mail_activation_template";
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Map<String,Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("nidhal.ounissi@etudiant-istmt.utm.tn");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);



    }

    public void sendResetPasswordEmail(String to, String username, String resetUrl, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("resetUrl", resetUrl);
        properties.put("subject", subject);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("nidhal.ounissi@etudiant-istmt.utm.tn");
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process("pwd_reset", context);
        helper.setText(template, true);
        mailSender.send(mimeMessage);
    }

}





