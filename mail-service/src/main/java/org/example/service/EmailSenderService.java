package org.example.service;

import org.example.dto.MailParams;

public interface EmailSenderService {
    void send(MailParams mailParams);
}
