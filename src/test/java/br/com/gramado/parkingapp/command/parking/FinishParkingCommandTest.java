package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FinishParkingCommandTest {

    // TODO CORRIGIR/VALIDAR
    @Mock
    private EmailServiceInterface emailService;

    @Mock
    private ParkingServiceInterface parkingService;

    @Mock
    private Parking parking;

    @InjectMocks
    private FinishParkingCommand finishParkingCommand;

    private static final LocalDateTime fixedLocalDateTime = LocalDateTime.of(2025, 2, 16, 18, 40);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFinishParkingForHourlyChargeAndSendEmail() {
        TicketEvent event = createMockTicketEvent(TypeCharge.HOUR, TicketEvent.TicketStatus.UPDATED);
        when(parkingService.findById(event.getTicketId())).thenReturn(Optional.of(parking));
        when(parking.isFinished()).thenReturn(false);
        when(parking.getPriceTable().getTypeCharge()).thenReturn(TypeCharge.HOUR);
        when(parking.getPriceTable().getValue()).thenReturn(BigDecimal.valueOf(10));

        Mockito.mockStatic(TimeUtils.class);
        when(TimeUtils.getDurationInHoursRoundedUp(any(), any())).thenReturn(new BigDecimal(2));

        finishParkingCommand.execute(event);

        verify(parkingService).update(parking);
        verify(emailService).sendPeriodClose(parking);
        verify(parking).setFinished(true);
        verify(parking).setDateTimeEnd(event.getExpirationTime());
        verify(parking.getPayment()).setPrice(BigDecimal.valueOf(20));
    }

    @Test
    void shouldNotFinishParkingIfAlreadyFinished() {
        TicketEvent event = createMockTicketEvent(TypeCharge.HOUR, TicketEvent.TicketStatus.UPDATED);
        when(parkingService.findById(event.getTicketId())).thenReturn(Optional.of(parking));
        when(parking.isFinished()).thenReturn(true);

        finishParkingCommand.execute(event);

        verify(parkingService, never()).update(parking);
        verify(emailService, never()).sendPeriodClose(parking);
    }

    @Test
    void shouldNotFinishParkingIfParkingNotFound() {
        TicketEvent event = createMockTicketEvent(TypeCharge.HOUR, TicketEvent.TicketStatus.UPDATED);
        when(parkingService.findById(event.getTicketId())).thenReturn(Optional.empty());

        finishParkingCommand.execute(event);

        verify(parkingService, never()).update(parking);
        verify(emailService, never()).sendPeriodClose(parking);
    }

    private TicketEvent createMockTicketEvent(TypeCharge typeCharge, TicketEvent.TicketStatus status) {
        return TicketEvent.builder()
                .ticketId(1)
                .typeCharge(typeCharge)
                .expirationTime(fixedLocalDateTime.plusHours(2))
                .status(status)
                .email("test@example.com")
                .price(BigDecimal.valueOf(100))
                .startDate(fixedLocalDateTime)
                .build();
    }
}