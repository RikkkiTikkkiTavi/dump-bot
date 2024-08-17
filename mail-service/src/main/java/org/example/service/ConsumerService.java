package org.example.service;

import org.example.dto.MailParams;

public interface ConsumerService {
    void consumeRegistrationMail(MailParams mailParams);
}
