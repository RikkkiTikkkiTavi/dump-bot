package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.configuration.RabbitConfiguration;
import org.example.service.UpdateProducer;
import org.example.utils.MessageUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j
@RequiredArgsConstructor
public class UpdateProcessor {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private final RabbitConfiguration rabbitConfiguration;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update.getMessage() != null) {
           distributeMessageByType(update);
        } else {
            log.error("Received unsupported message type" + update);
        }
    }

    //сортируем сообщения
    private void distributeMessageByType(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            processTextMessageUpdate(update);
        } else if (message.hasDocument()) {
            processDocumentMessageUpdate(update);
        } else if (message.hasPhoto()) {
            processPhotoMessageUpdate(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        SendMessage sendMessage = messageUtils.generateSendMessageWithText(update, "Unsupported type of message!");
        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswer(sendMessage);
    }

    private void processPhotoMessageUpdate(Update update) {
        updateProducer.produce(rabbitConfiguration.getPhotoMessageUpdateQueue(), update);
        setFileIsReceivedView(update);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "Photo processing in progress");
        setView(sendMessage);
    }

    private void processDocumentMessageUpdate(Update update) {
        updateProducer.produce(rabbitConfiguration.getDocMessageUpdateQueue(), update);
    }

    private void processTextMessageUpdate(Update update) {
        updateProducer.produce(rabbitConfiguration.getTextMessageUpdateQueue(), update);
    }
}
