package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FinishParkingCommandTest {

    @Mock
    private EmailServiceInterface emailService;

    @Mock
    private ParkingServiceInterface parkingService;

    @InjectMocks
    private FinishParkingCommand finishParkingCommand;

    private MockedStatic<TimeUtils> mockedStatic;

    private Parking parking;
    private static final LocalDateTime fixedLocalDateTime = LocalDateTime.of(2025, 2, 16, 18, 40);
    private static final LocalDateTime endParkingPeriod = LocalDateTime.of(2025, 2, 16, 22, 30);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockedStatic = Mockito.mockStatic(TimeUtils.class);

        when(TimeUtils.getTime())
                .thenReturn(endParkingPeriod);
        when(TimeUtils.getDurationInHoursRoundedUp(any(), any()))
                .thenReturn(new BigDecimal(5));

        parking = new Parking();
        parking.setId(1);
        parking.setFinished(false);
        parking.setDateTimeStart(fixedLocalDateTime);
        parking.setPayment(new Payment());

        PriceTable priceTable = new PriceTable();
        priceTable.setId(2);
        priceTable.setTypeCharge(TypeCharge.HOUR);
        priceTable.setValue(BigDecimal.TEN);
        parking.setPriceTable(priceTable);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void testExecuteWithParkingId() throws ValidationsException {
        when(parkingService.findById(eq(1)))
                .thenReturn(Optional.of(parking));

        finishParkingCommand.execute(1);

        verify(parkingService).update(any(Parking.class));
        verify(emailService).sendPeriodClose(parking);

        assertTrue(parking.isFinished());
        assertEquals(endParkingPeriod, parking.getDateTimeEnd());
        assertEquals(new BigDecimal(50), parking.getPayment().getPrice());
    }

    @Test
    void testExecuteWithParkingIdAndParkingAlreadyFinished() {
        parking.setFinished(true);

        when(parkingService.findById(eq(1)))
                .thenReturn(Optional.of(parking));

        ValidationsException exception = assertThrows(ValidationsException.class,
                () -> finishParkingCommand.execute(1));

        assertEquals("Estacionamento já finalizado!", exception.getMessage());
    }

    @Test
    void testExecuteWithParkingIdAndParkingNotExists() {
        when(parkingService.findById(eq(1)))
                .thenReturn(Optional.empty());

        ValidationsException exception = assertThrows(ValidationsException.class,
                () -> finishParkingCommand.execute(1));

        assertEquals("Estacionamento não encontrado!", exception.getMessage());
    }

    @Test
    void testExecuteWithParkingIdAndTypeChargeFixed() throws ValidationsException {
        parking.getPriceTable().setTypeCharge(TypeCharge.FIXED);
        parking.getPriceTable().setDuration(LocalTime.of(5, 0));
        parking.setDateTimeEnd(fixedLocalDateTime.plusHours(5));
        parking.getPayment().setPrice(parking.getPriceTable().getValue());

        when(parkingService.findById(eq(1)))
                .thenReturn(Optional.of(parking));

        finishParkingCommand.execute(1);

        verify(parkingService).update(parking);
        verify(emailService).sendPeriodClose(parking);

        assertTrue(parking.isFinished());
        assertEquals(endParkingPeriod, parking.getDateTimeEnd());
        assertEquals(parking.getPriceTable().getValue(), parking.getPayment().getPrice());
    }
}