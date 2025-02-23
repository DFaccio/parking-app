package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class CreateOnRedisCommandTest {

    // TODO CORRIGIR/VALIDAR

    @Mock
    private RedisTemplate<Integer, TicketEvent> redisTemplate;

    @Mock
    private ValueOperations<Integer, TicketEvent> valueOperations;

    @InjectMocks
    private CreateOnRedisCommand createOnRedisCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateNotificationAndStoreInRedis() {
        Parking parking = createMockParking();
        TicketEvent expectedEvent = TicketEvent.builder()
                .ticketId(parking.getId())
                .typeCharge(parking.getPriceTable().getTypeCharge())
                .expirationTime(parking.getDateTimeStart().plusMinutes(50))
                .status(TicketEvent.TicketStatus.CREATED)
                .email(parking.getVehicle().getPerson().getEmail())
                .price(parking.getPriceTable().getValue())
                .startDate(parking.getDateTimeStart())
                .build();

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        createOnRedisCommand.createRedisExpirationEvent(parking);

        verify(redisTemplate.opsForValue())
                .set(eq(expectedEvent.getTicketId()), any(TicketEvent.class), any(Duration.class));
    }

    private Parking createMockParking() {
        Person person = new Person();
        person.setEmail("test@example.com");

        Vehicle vehicle = new Vehicle();
        vehicle.setPerson(person);

        PriceTable priceTable = new PriceTable();
        priceTable.setTypeCharge(TypeCharge.HOUR);
        priceTable.setValue(BigDecimal.valueOf(100.0));

        Parking parking = new Parking();
        parking.setId(1);
        parking.setVehicle(vehicle);
        parking.setPriceTable(priceTable);
        parking.setDateTimeStart(LocalDateTime.now());
        parking.setDateTimeEnd(LocalDateTime.now().plusHours(1));

        return parking;
    }
}
