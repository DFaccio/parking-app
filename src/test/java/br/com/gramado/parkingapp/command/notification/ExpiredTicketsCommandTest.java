package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExpiredTicketsCommandTest {

    @Mock
    private RedisTemplate<Integer, TicketEvent> redisTemplate;

    @Mock
    private ValueOperations<Integer, TicketEvent> valueOperations;

    @Mock
    private EmailServiceInterface emailService;

    @Mock
    private FinishParkingCommand finishParkingCommand;

    @InjectMocks
    private ExpiredTicketsCommand expiredTicketsCommand;

    private static final LocalDateTime fixedLocalDateTime = LocalDateTime.of(2025, 2, 16, 18, 40);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessHourlyChargeToBeUpdated() {
        TicketEvent event = createMockEvent(TypeCharge.HOUR, TicketEvent.TicketStatus.TO_BE_UPDATED);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        expiredTicketsCommand.handleQueueMessages(event);

        verify(emailService).sendHourlyWarnMessage(event.getEmail());
        verify(redisTemplate.opsForValue()).set(eq(event.getTicketId()), any(TicketEvent.class), any(Duration.class));
        assertEquals(fixedLocalDateTime.plusMinutes(10), event.getExpirationTime());
    }

    @Test
    void shouldProcessHourlyChargeUpdated() {
        TicketEvent event = createMockEvent(TypeCharge.HOUR, TicketEvent.TicketStatus.UPDATED);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        expiredTicketsCommand.handleQueueMessages(event);

        verify(emailService).sendHourlyAdditionTime(event.getStartDate(), event.getExpirationTime(), event.getPrice(), event.getEmail());
        verify(redisTemplate.opsForValue()).set(eq(event.getTicketId()), any(TicketEvent.class), any(Duration.class));
        assertEquals(fixedLocalDateTime.plusMinutes(50), event.getExpirationTime());
    }

    @Test
    void shouldProcessFixedChargeToBeUpdated() {
        TicketEvent event = createMockEvent(TypeCharge.FIXED, TicketEvent.TicketStatus.TO_BE_UPDATED);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        expiredTicketsCommand.handleQueueMessages(event);

        verify(emailService).sendFixedWarnMessage(event.getEmail());
        verify(redisTemplate.opsForValue()).set(eq(event.getTicketId()), any(TicketEvent.class), any(Duration.class));
        assertEquals(fixedLocalDateTime.plusMinutes(5), event.getExpirationTime());
    }

    @Test
    void shouldProcessFixedChargeUpdated() {
        TicketEvent event = createMockEvent(TypeCharge.FIXED, TicketEvent.TicketStatus.UPDATED);

        expiredTicketsCommand.handleQueueMessages(event);

        verify(finishParkingCommand).execute(event);
        assertEquals(fixedLocalDateTime.plusMinutes(5), event.getExpirationTime());
    }

    private TicketEvent createMockEvent(TypeCharge typeCharge, TicketEvent.TicketStatus status) {
        return TicketEvent.builder()
                .ticketId(1)
                .typeCharge(typeCharge)
                .expirationTime(fixedLocalDateTime)
                .status(status)
                .email("test@example.com")
                .price(BigDecimal.valueOf(100.0))
                .startDate(LocalDateTime.now().minusHours(1))
                .build();
    }
}
