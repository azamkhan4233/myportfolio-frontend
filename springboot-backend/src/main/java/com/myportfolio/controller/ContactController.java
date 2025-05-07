// package com.myportfolio.controller;

// import com.myportfolio.dto.ContactForm;
// import com.myportfolio.service.EmailService;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.beans.factory.annotation.Autowired;

// @RestController
// @RequestMapping("/api/contact")
// @CrossOrigin(origins = "*")
// public class ContactController {

//     private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

//     private final EmailService emailService;

//     @Autowired
//     public ContactController(EmailService emailService) {
//         this.emailService = emailService;
//     }

//     // @PostMapping
//     // public ResponseEntity<String> submitContactForm(@RequestBody ContactForm contactForm) {
//     //     logger.info("Received contact form submission: Name={}, Email={}, Subject={}, Message={}",
//     //             contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(), contactForm.getMessage());

//     //     // Send email using EmailService
//     //     String to = "azamkhan19043@gmail.com"; // Replace with your email address
//     //     String subject = "New Contact Form Submission: " + contactForm.getSubject();
//     //     String text = "Name: " + contactForm.getName() + "\n"
//     //                 + "Email: " + contactForm.getEmail() + "\n"
//     //                 + "Message: " + contactForm.getMessage();

//     //     emailService.sendSimpleMessage(to, subject, text);

//     //     return ResponseEntity.ok("Contact form submitted successfully");
//     // }

//     @PostMapping
//     public ResponseEntity<String> submitContactForm(@RequestBody ContactForm contactForm) {
//         logger.info("Received contact form submission: Name={}, Email={}, Subject={}, Message={}",
//                 contactForm.getName(), contactForm.getEmail(), contactForm.getSubject(), contactForm.getMessage());
    
//         String to = "azamkhan19043@gmail.com"; // Your receiving email
//         String subject = "New Contact Form Submission: " + contactForm.getSubject();
//         String text = "Name: " + contactForm.getName() + "\n"
//                     + "Email: " + contactForm.getEmail() + "\n"
//                     + "Message: " + contactForm.getMessage();
    
//         try {
//             // Pass the replyTo (user's email)
//             emailService.sendSimpleMessage(to, subject, text, contactForm.getEmail());
//         } catch (Exception e) {
//             logger.error("Error sending email", e);
//             return ResponseEntity.status(500).body("Error sending message");
//         }
    
//         return ResponseEntity.ok("Contact form submitted successfully");
//     }
    
// }

package com.myportfolio.controller;

import com.myportfolio.dto.ContactForm;
import com.myportfolio.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    
    private final EmailService emailService;
    private static final String RECIPIENT_EMAIL = "your-personal-email@example.com"; // Replace with your email

    @Autowired
    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> submitContactForm(@Valid @RequestBody ContactForm contactForm) {
        try {
            logger.info("New contact submission from {} <{}>", contactForm.getName(), contactForm.getEmail());
            
            String subject = "New Contact: " + contactForm.getSubject();
            String text = buildEmailText(contactForm);
            
            emailService.sendEmailWithReplyTo(
                RECIPIENT_EMAIL,
                subject,
                text,
                contactForm.getEmail()  // Set user's email as reply-to
            );
            
            return ResponseEntity.ok(
                new ApiResponse(true, "Thank you for your message! I'll get back to you soon.")
            );
            
        } catch (Exception e) {
            logger.error("Error processing contact form from {}: {}", contactForm.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Failed to send your message. Please try again later."));
        }
    }

    private String buildEmailText(ContactForm contactForm) {
        return String.format(
            "Name: %s\n" +
            "Email: %s\n" +
            "Subject: %s\n\n" +
            "Message:\n%s\n\n" +
            "---\n" +
            "This message was sent from your portfolio contact form.",
            contactForm.getName(),
            contactForm.getEmail(),
            contactForm.getSubject(),
            contactForm.getMessage()
        );
    }

    // Simple response DTO
    private static class ApiResponse {
        private boolean success;
        private String message;
        
        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}