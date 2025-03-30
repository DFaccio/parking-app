package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class InitializeTicketEventCommandTest {

    @Mock
    private TicketEventServiceInterface service;

    @InjectMocks
    private InitializeTicketEventCommand initializeTicketEventCommand;

    private Parking parking;

    private PriceTable priceTable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Person person = new Person();
        person.setEmail("test@example.com");

        Vehicle vehicle = new Vehicle();
        vehicle.setPerson(person);

        priceTable = new PriceTable();
        priceTable.setTypeCharge(TypeCharge.FIXED);
        priceTable.setValue(BigDecimal.TEN);

        parking = new Parking();
        parking.setId(1);
        parking.setDateTimeStart(LocalDateTime.now().minusHours(1));
        parking.setDateTimeEnd(LocalDateTime.now().plusHours(1));
        parking.setPriceTable(priceTable);
        parking.setVehicle(vehicle);
    }

    @Test
    void testCreateExpirationEvent_FixedCharge() throws Exception {
        initializeTicketEventCommand.createExpirationEvent(parking);

        verify(service).create(any(TicketEvent.class));
    }

    @Test
    void testCreateExpirationEvent_HourlyCharge() throws Exception {
        priceTable.setTypeCharge(TypeCharge.HOUR);

        initializeTicketEventCommand.createExpirationEvent(parking);

        verify(service).create(any(TicketEvent.class));
    }
}
