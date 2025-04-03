package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitializeTicketEventCommand {

    private final TicketEventServiceInterface service;

    public void createExpirationEvent(Parking parking) throws JsonProcessingException {
        LocalDateTime expirationTime;

        if (TypeCharge.FIXED.equals(parking.getPriceTable().getTypeCharge())) {
            expirationTime = parking.getDateTimeEnd().minusMinutes(5);
        } else {
            expirationTime = parking.getDateTimeStart().plusMinutes(50);
        }

        TicketEvent event = TicketEvent.builder()
                .ticketId(parking.getId())
                .typeCharge(parking.getPriceTable().getTypeCharge())
                .expirationTime(expirationTime)
                .status(TicketEvent.TicketStatus.CREATED)
                .email(parking.getVehicle().getPerson().getEmail())
                .price(parking.getPriceTable().getValue())
                .startDate(parking.getDateTimeStart())
                .build();

        service.create(event);
    }
}
