package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListeningTicketEventCompletionOrUpdateCommand {

    private final TicketEventServiceInterface service;

    private final EmailServiceInterface emailService;

    private final FinishParkingCommand finishParkingCommand;

    @RabbitListener(queues = "${queue.time.to.notify}")
    public void onMessage(TicketEvent event) throws JsonProcessingException {
        log.info("Ticket expired: " + event.getTicketId());

        if (TypeCharge.HOUR.equals(event.getTypeCharge())) {
            processHourlyCharge(event);
        } else {
            processFixedCharge(event);
        }
    }

    private void processHourlyCharge(TicketEvent event) throws JsonProcessingException {
        if (event.getStatus() == TicketEvent.TicketStatus.TO_BE_UPDATED) {
            updateEvent(event, 10, TicketEvent.TicketStatus.UPDATED);
            emailService.sendHourlyWarnMessage(event.getEmail());
        } else if (event.getStatus() == TicketEvent.TicketStatus.UPDATED) {
            updateEvent(event, 50, TicketEvent.TicketStatus.TO_BE_UPDATED);
            emailService.sendHourlyAdditionTime(event.getStartDate(), event.getExpirationTime(), event.getPrice(), event.getEmail());
        }
    }

    private void processFixedCharge(TicketEvent event) throws JsonProcessingException {
        if (event.getStatus() == TicketEvent.TicketStatus.TO_BE_UPDATED) {
            updateEvent(event, 5, TicketEvent.TicketStatus.UPDATED);
            emailService.sendFixedWarnMessage(event.getEmail());
        } else if (event.getStatus() == TicketEvent.TicketStatus.UPDATED) {
            event.setExpirationTime(event.getExpirationTime().plusMinutes(5));
            finishParkingCommand.execute(event);
        }
    }

    private void updateEvent(TicketEvent event, long minutesToAdd, TicketEvent.TicketStatus newStatus) throws JsonProcessingException {
        LocalDateTime newerExpiration = event.getExpirationTime().plusMinutes(minutesToAdd);

        event.setExpirationTime(newerExpiration);
        event.setStatus(newStatus);

        service.update(event);
    }
}
