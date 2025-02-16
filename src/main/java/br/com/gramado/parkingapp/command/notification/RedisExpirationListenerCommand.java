package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.service.notification.QueueServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisExpirationListenerCommand implements MessageListener {

    @Value("${queue.name.expired}")
    private String expiredTicketQueue;

    private final QueueServiceInterface queueService;

    @Autowired
    public RedisExpirationListenerCommand(QueueServiceInterface queueService) {
        this.queueService = queueService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredTicket = message.toString();

        System.out.println("Redis ticket expired: " + expiredTicket);

        queueService.sendMessageToQueue(expiredTicket, expiredTicketQueue);
    }
}
