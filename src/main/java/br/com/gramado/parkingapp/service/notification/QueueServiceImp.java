package br.com.gramado.parkingapp.service.notification;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class QueueServiceImp implements QueueServiceInterface {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueServiceImp(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessageToQueue(String bodyMessage, String queueName) {
        Message message = new Message(bodyMessage.getBytes(StandardCharsets.UTF_8));
        message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.name());
        message.getMessageProperties().setContentType("application/json");

        rabbitTemplate.send(queueName, message);
    }
}
