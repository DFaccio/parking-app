package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ExpiredTicketsCommand {

    private final RedisTemplate<Integer, TicketEvent> redisTemplate;
    private final EmailServiceInterface emailService;

    private final FinishParkingCommand finishParkingCommand;

    @Autowired
    public ExpiredTicketsCommand(RedisTemplate<Integer, TicketEvent> redisTemplate, EmailServiceInterface emailService, FinishParkingCommand finishParkingCommand) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.finishParkingCommand = finishParkingCommand;
    }

    @RabbitListener(queues = "${queue.name}")
    public void handleQueueMessages(TicketEvent event) {
        if (TypeCharge.HOUR.equals(event.getTypeCharge())) {
            processHourlyCharge(event);
        } else {
            processFixedCharge(event);
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
