package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListeningTicketEventCompletionOrUpdateCommand {

    private final TicketEventServiceInterface service;

    private final EmailServiceInterface emailService;

    private final ParkingServiceInterface parkingService;

    @RabbitListener(queues = "${queue.time.to.notify}")
    public void onMessage(TicketEvent event) {
        Integer ticketId = event.getTicketId();
        log.info("Ticket expired: {}", ticketId);

        parkingService.findById(ticketId).ifPresentOrElse(
                parking -> {
                    try {
                        handleParkingEvent(event, parking);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> log.info("Ticket {} not found.", ticketId)
        );
    }

    private void handleParkingEvent(TicketEvent event, Parking parking) throws IOException {
        if (parking.isFinished()) {
            log.info("Ticket {} already finished.", event.getTicketId());
            return;
        }

        if (event.isStatusWarnUser()) {
            updateEvent(event, event.isHourlyEvent() ? 10 : 5, TicketEvent.TicketStatus.UPDATED);
            sendEmailWarn(event);
        } else if (event.isHourlyEvent()) {
            processHourlyCharge(event);
        } else {
            endFixedParkingPeriod(parking);
        }
    }

    private void sendEmailWarn(TicketEvent event) {
        if (event.isFixedEvent()) {
            emailService.sendFixedWarnMessage(event.getEmail());
        } else {
            emailService.sendHourlyWarnMessage(event.getEmail());
        }
    }

    private void processHourlyCharge(TicketEvent event) throws IOException {
        updateEvent(event, 50, TicketEvent.TicketStatus.TO_BE_UPDATED);
        emailService.sendHourlyAdditionTime(event.getStartDate(), event.getExpirationTime(), event.getPrice(), event.getEmail());
    }

    private void endFixedParkingPeriod(Parking parking) {
        parking.setFinished(true);
        parkingService.update(parking);
        emailService.sendPeriodClose(parking);
    }

    private void updateEvent(TicketEvent event, long minutesToAdd, TicketEvent.TicketStatus newStatus) throws IOException {
        event.setExpirationTime(event.getExpirationTime().plusMinutes(minutesToAdd));
        event.setStatus(newStatus);
        service.update(event);
    }
}
