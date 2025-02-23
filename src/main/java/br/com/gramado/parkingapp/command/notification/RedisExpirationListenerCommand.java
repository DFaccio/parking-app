package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.notification.QueueServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class RedisExpirationListenerCommand implements MessageListener {

    private final RedisTemplate<Integer, TicketEvent> redisTemplate;

    private final EmailServiceInterface emailService;

    private final FinishParkingCommand finishParkingCommand;

    private final ObjectMapper objectMapper;

    @Autowired
    public RedisExpirationListenerCommand(RedisTemplate<Integer, TicketEvent> redisTemplate, EmailServiceInterface emailService, FinishParkingCommand finishParkingCommand, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.finishParkingCommand = finishParkingCommand;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredTicket = message.toString();

        System.out.println("Redis ticket expired: " + expiredTicket);

        try {
            TicketEvent ticketEvent = objectMapper.readValue(pattern, TicketEvent.class);

            if (TypeCharge.HOUR.equals(ticketEvent.getTypeCharge())) {
                processHourlyCharge(ticketEvent);
            } else {
                processFixedCharge(ticketEvent);
            }
        } catch (IOException e) {
            System.out.println("Message couldn't be converted: " + expiredTicket);
            throw new RuntimeException(e);
        }
    }

    private void processHourlyCharge(TicketEvent event) {
        if (event.getStatus() == TicketEvent.TicketStatus.TO_BE_UPDATED) {
            updateEvent(event, 10, TicketEvent.TicketStatus.UPDATED);
            emailService.sendHourlyWarnMessage(event.getEmail());
        } else if (event.getStatus() == TicketEvent.TicketStatus.UPDATED) {
            updateEvent(event, 50, TicketEvent.TicketStatus.TO_BE_UPDATED);
            emailService.sendHourlyAdditionTime(event.getStartDate(), event.getExpirationTime(), event.getPrice(), event.getEmail());
        }
    }

    private void processFixedCharge(TicketEvent event) {
        if (event.getStatus() == TicketEvent.TicketStatus.TO_BE_UPDATED) {
            updateEvent(event, 5, TicketEvent.TicketStatus.UPDATED);
            emailService.sendFixedWarnMessage(event.getEmail());
        } else if (event.getStatus() == TicketEvent.TicketStatus.UPDATED) {
            event.setExpirationTime(event.getExpirationTime().plusMinutes(5));
            finishParkingCommand.execute(event);
        }
    }

    private void updateEvent(TicketEvent event, long minutesToAdd, TicketEvent.TicketStatus newStatus) {
        event.setExpirationTime(event.getExpirationTime().plusMinutes(minutesToAdd));
        event.setStatus(newStatus);

        Duration ttl = Duration.between(LocalDateTime.now(), event.getExpirationTime());
        redisTemplate.opsForValue().set(event.getTicketId(), event, ttl);
    }
}
