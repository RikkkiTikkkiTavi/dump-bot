package org.example.service;

import org.telegram.telegrambots.meta.api.objects.Update;

interface ConsumerService {
    void consumeTextMessageUpdate(Update update);

    void consumePhotoMessageUpdate(Update update);
    void consumeDocMessageUpdate(Update update);
}
