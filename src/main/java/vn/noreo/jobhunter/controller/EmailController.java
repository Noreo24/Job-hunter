package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.noreo.jobhunter.service.EmailService;
import vn.noreo.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send email")
    public String sendEmail() {
        // this.emailService.sendEmail();
        // this.emailService.sendEmailSync("buianhtuan2003kb@gmail.com", "TEST SEND
        // EMAIL", "<h1> <b> Hello </b> </h1>",
        // false, true);
        this.emailService.sendEmailFromTemplateSync("buianhtuan2003kb@gmail.com", "TEST SEND EMAIL", "job");
        return "ok";
    }

}
