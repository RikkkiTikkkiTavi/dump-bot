package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.AppUserDAO;
import org.example.dto.MailParams;
import org.example.entity.AppUser;
import org.example.entity.enams.UserState;
import org.example.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;
    @Value("${service.mail.uri}")
    private String mailServiceUri;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы!";
        }  else if (appUser.getEmail() != null) {
            return "Вам на почту уже было отправлено письмо. Перейдите по ссылке для подтверждения.";
        }
        appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Введите email:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            return "Некорректный email. Пожалуйста, введите email снова, либо воспользуйтесь отменой /cancel";
        }
        Optional<AppUser> opUser = appUserDAO.findByEmail(email);
        if (opUser.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(UserState.BASIC_STATE);
            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            ResponseEntity<String> response = sendRequestToEmailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                String message = String.format("Отправка письма на почту %s не удалась", email);
                appUser.setEmail(null);
                appUserDAO.save(appUser);
                return message;
            }

            return "Вам на почту было отправлено письмо."
                    + "Перейдите по ссылке в письме для подтверждения регистрации.";
        } else {
            return "Этот email уже используется. Введите корректный email."
                    + " Для отмены команды введите /cancel";
        }
    }

    private ResponseEntity<String> sendRequestToEmailService(String cryptoUserId, String email) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MailParams mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        HttpEntity<MailParams> request = new HttpEntity<>(mailParams, headers);
        return restTemplate.exchange(mailServiceUri, HttpMethod.POST, request, String.class);
    }

}
