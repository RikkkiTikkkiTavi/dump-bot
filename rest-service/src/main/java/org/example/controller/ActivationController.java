package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserActivationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        boolean res = userActivationService.activation(id);
        if (res) {
            return ResponseEntity.ok().body("Регистрация прошла успешно");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
