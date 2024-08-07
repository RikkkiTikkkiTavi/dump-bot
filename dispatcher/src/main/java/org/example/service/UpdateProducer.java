package org.example.service;

import org.telegram.telegrambots.meta.api.objects.Update;

//сервис передаёт апдейты в rabbitmq
public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
