package br.com.gramado.parkingapp.service.tickets;

import br.com.gramado.parkingapp.dto.TicketEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.gramado.parkingapp.util.TimeUtils.durationBetweenDate;
import static br.com.gramado.parkingapp.util.TimeUtils.getTime;

@Service
@RequiredArgsConstructor
class TicketEventServiceImpl implements TicketEventServiceInterface {

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Value("${queue.wait.for.expiration}")
    private String waitNotificationQueue;

    @Override
    public void create(TicketEvent event) throws JsonProcessingException {
        sendEvent(event.getStartDate(), event);
    }

    private void sendEvent(LocalDateTime event, TicketEvent event1) throws JsonProcessingException {
        long ttl = durationBetweenDate(event, event1.getExpirationTime());

        MessageProperties properties = new MessageProperties();
        properties.setExpiration(String.valueOf(ttl));

        String bodyAsString = objectMapper.writeValueAsString(event);

        Message amqpMessage = new Message(bodyAsString.getBytes(), properties);

        rabbitTemplate.send(waitNotificationQueue, amqpMessage);
    }

    @Override
    public void update(TicketEvent event) throws JsonProcessingException {
        sendEvent(getTime(), event);
    }
}
