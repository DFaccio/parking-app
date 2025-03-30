package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListeningTicketEventCompletionOrUpdateCommand {

    private final TicketEventServiceInterface service;

    private final EmailServiceInterface emailService;

    private final ParkingServiceInterface parkingService;

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${queue.time.to.notify}")
    public void onMessage(Message message) throws IOException {
        TicketEvent event = objectMapper.readValue(message.getBody(), TicketEvent.class);

        Integer tickedId = event.getTicketId();

        log.info("Ticket expired: " + tickedId);

        Optional<Parking> parkingOptional = parkingService.findById(tickedId);

        if (parkingOptional.isEmpty()) {
            log.info("Ticket " + tickedId + " not found.");
        } else if (parkingOptional.get().isFinished()) {
            log.info("Ticket " + tickedId + " already finished.");
        } else {
            if (TypeCharge.HOUR.equals(event.getTypeCharge())) {
                processHourlyCharge(event);
            } else {
                processFixedCharge(event, parkingOptional.get());
            }
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

    private void processFixedCharge(TicketEvent event, Parking parking) throws JsonProcessingException {
        if (event.getStatus() == TicketEvent.TicketStatus.TO_BE_UPDATED) {
            updateEvent(event, 5, TicketEvent.TicketStatus.UPDATED);
            emailService.sendFixedWarnMessage(event.getEmail());
        } else if (event.getStatus() == TicketEvent.TicketStatus.UPDATED) {
            parking.setFinished(true);

            parkingService.update(parking);

            emailService.sendPeriodClose(parking);
        }
    }

    private void updateEvent(TicketEvent event, long minutesToAdd, TicketEvent.TicketStatus newStatus) throws JsonProcessingException {
        LocalDateTime newerExpiration = event.getExpirationTime().plusMinutes(minutesToAdd);

        event.setExpirationTime(newerExpiration);
        event.setStatus(newStatus);

        service.update(event);
    }
}
