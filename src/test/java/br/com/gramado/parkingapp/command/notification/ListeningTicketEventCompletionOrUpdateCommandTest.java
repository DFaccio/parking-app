package br.com.gramado.parkingapp.command.notification;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class ListeningTicketEventCompletionOrUpdateCommandTest {

    /*@Mock
    private TicketEventServiceInterface service;

    @Mock
    private EmailServiceInterface emailService;

    @Mock
    private ParkingServiceInterface parkingService;

    @InjectMocks
    private ListeningTicketEventCompletionOrUpdateCommand listener;

    private Parking parking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parking = new Parking();
        parking.setId(1);
        parking.setFinished(false);
        parking.setDateTimeStart(LocalDateTime.now().minusHours(2));
        parking.setPriceTable(new PriceTable());
        parking.getPriceTable().setTypeCharge(TypeCharge.FIXED);
        parking.getPriceTable().setValue(BigDecimal.TEN);
        parking.setPayment(new Payment());
    }

    private TicketEvent createTicketEvent(TicketEvent.TicketStatus status, TypeCharge charge){
        return TicketEvent.builder()
                .ticketId(1)
                .typeCharge(charge)
                .expirationTime(LocalDateTime.now().minusMinutes(5))
                .status(status)
                .email("test@example.com")
                .price(BigDecimal.TEN)
                .startDate(LocalDateTime.now().minusHours(2))
                .build();
    }

    @Test
    void testOnMessageWithNonExistingTicket() throws JsonProcessingException {
        when(parkingService.findById(1)).thenReturn(Optional.empty());

        TicketEvent ticketEvent = createTicketEvent(TicketEvent.TicketStatus.TO_BE_UPDATED, TypeCharge.HOUR);

        listener.onMessage(ticketEvent);

        verify(service, never()).update(any(TicketEvent.class));
        verify(emailService, never()).sendHourlyWarnMessage(anyString());
    }

    @Test
    void testOnMessageWithFinishedParking() throws JsonProcessingException {
        parking.setFinished(true);
        when(parkingService.findById(1)).thenReturn(Optional.of(parking));

        TicketEvent ticketEvent = createTicketEvent(TicketEvent.TicketStatus.TO_BE_UPDATED, TypeCharge.HOUR);

        listener.onMessage(ticketEvent);

        verify(service, never()).update(any(TicketEvent.class));
        verify(emailService, never()).sendHourlyWarnMessage(anyString());
    }

    @Test
    void testProcessHourlyChargeToBeUpdated() throws JsonProcessingException {
        when(parkingService.findById(1)).thenReturn(Optional.of(parking));

        doNothing().when(service).update(any(TicketEvent.class));

        TicketEvent ticketEvent = createTicketEvent(TicketEvent.TicketStatus.TO_BE_UPDATED, TypeCharge.HOUR);

        listener.onMessage(ticketEvent);

        verify(service).update(any(TicketEvent.class));
        verify(emailService).sendHourlyWarnMessage(ticketEvent.getEmail());
    }

    @Test
    void testProcessFixedChargeToBeUpdated() throws JsonProcessingException {
        TicketEvent ticketEvent = createTicketEvent(TicketEvent.TicketStatus.TO_BE_UPDATED, TypeCharge.FIXED);

        when(parkingService.findById(1)).thenReturn(Optional.of(parking));

        listener.onMessage(ticketEvent);

        verify(service).update(any(TicketEvent.class));
        verify(emailService).sendFixedWarnMessage(ticketEvent.getEmail());
    }

    @Test
    void testProcessFixedChargeUpdated() throws JsonProcessingException {
        TicketEvent ticketEvent = createTicketEvent(TicketEvent.TicketStatus.UPDATED, TypeCharge.FIXED);

        when(parkingService.findById(1)).thenReturn(Optional.of(parking));

        listener.onMessage(ticketEvent);

        verify(parkingService).update(parking);
        verify(emailService).sendPeriodClose(parking);
    }*/
}
