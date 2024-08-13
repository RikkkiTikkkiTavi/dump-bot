package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.MailParams;
import org.example.service.EmailSenderService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final EmailSenderService emailSenderService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParams mailParams) {
        emailSenderService.send(mailParams);
        return ResponseEntity.ok().build();
    }
}
