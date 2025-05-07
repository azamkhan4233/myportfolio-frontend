 package com.myportfolio.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     private final JavaMailSender mailSender;

//     @Autowired
//     public EmailService(JavaMailSender mailSender) {
//         this.mailSender = mailSender;
//     }

//     public void sendSimpleMessage(String to, String subject, String text) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject(subject);
//         message.setText(text);
//         mailSender.send(message);
//     }
// }


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     private final JavaMailSender mailSender;

//     @Autowired
//     public EmailService(JavaMailSender mailSender) {
//         this.mailSender = mailSender;
//     }

//     public void sendSimpleMessage(String to, String subject, String text, String replyTo) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(to);
//         message.setSubject(subject);
//         message.setText(text);
//         message.setReplyTo(replyTo); // ✅ Set client's email as Reply-To
//         mailSender.send(message);
//     }
// }




// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import javax.mail.internet.MimeMessage; 
// import org.springframework.stereotype.Service;

// import javax.mail.internet.MimeMessage;

// @Service
// public class EmailService {

//     private final JavaMailSender mailSender;

//     @Autowired
//     public EmailService(JavaMailSender mailSender) {
//         this.mailSender = mailSender;
//     }

//     public void sendSimpleMessage(String to, String subject, String text, String replyTo) throws Exception {
//         MimeMessage message = mailSender.createMimeMessage();
//         MimeMessageHelper helper = new MimeMessageHelper(message, true);

//         helper.setTo(to);
//         helper.setSubject(subject);
//         helper.setText(text);
//         helper.setReplyTo(replyTo); // Set the reply-to to the user's email

//         mailSender.send(message); // Send the email
//     }
// }

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an email with proper reply-to address
     * @param to Recipient email address
     * @param subject Email subject
     * @param text Email content
     * @param replyToEmail Email address to set as Reply-To
     * @throws RuntimeException if email sending fails
     */
    public void sendEmailWithReplyTo(String to, String subject, String text, String replyToEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false); // false = text/plain, true = text/html
            helper.setReplyTo(replyToEmail);
            
            // Optionally set From name (if your SMTP allows it)
            // helper.setFrom("no-reply@yourdomain.com", "Your Portfolio Site");
            
            mailSender.send(message);
            logger.info("Email sent to {} with reply-to {}", to, replyToEmail);
        } catch (MailException e) {
            logger.error("MailException while sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        } catch (MessagingException e) {
            logger.error("MessagingException while sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to create email message", e);
        }
    }
}