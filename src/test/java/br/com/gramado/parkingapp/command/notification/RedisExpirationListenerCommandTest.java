package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.command.parking.FinishParkingCommand;
import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisExpirationListenerCommandTest {

    // TODO CORRIGIR/VALIDAR

    @Mock
    private RedisTemplate<Integer, TicketEvent> redisTemplate;

    @Mock
    private ValueOperations<Integer, TicketEvent> valueOperations;

    @Mock
    private EmailServiceInterface emailService;

    @Mock
    private FinishParkingCommand finishParkingCommand;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Message message;

    @InjectMocks
    private RedisExpirationListenerCommand listener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    private TicketEvent hourlyEvent(TicketEvent.TicketStatus status){
        return TicketEvent.builder()
                .ticketId(1)
                .typeCharge(TypeCharge.HOUR)
                .status(status)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .email("test@example.com")
                .startDate(LocalDateTime.now())
                .build();
    }

    private TicketEvent fixedEvent(TicketEvent.TicketStatus status){
        return TicketEvent.builder()
                .ticketId(1)
                .typeCharge(TypeCharge.FIXED)
                .status(status)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .email("test@example.com")
                .startDate(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldProcessHourlyChargeToBeUpdated() throws IOException {
        TicketEvent event = hourlyEvent(TicketEvent.TicketStatus.TO_BE_UPDATED);

        when(objectMapper.readValue(any(byte[].class), eq(TicketEvent.class))).thenReturn(event);
        when(message.toString()).thenReturn("testMessage");

        listener.onMessage(message, new byte[]{});

        verify(emailService).sendHourlyWarnMessage(event.getEmail());
        verify(valueOperations).set(eq(event.getTicketId()), eq(event), any(Duration.class));
        verify(event).setStatus(TicketEvent.TicketStatus.UPDATED);
    }

    @Test
    void shouldProcessHourlyChargeUpdated() throws IOException {
        TicketEvent event = hourlyEvent(TicketEvent.TicketStatus.UPDATED);

        when(objectMapper.readValue(any(byte[].class), eq(TicketEvent.class))).thenReturn(event);
        when(message.toString()).thenReturn("testMessage");

        listener.onMessage(message, new byte[]{});

        verify(emailService).sendHourlyAdditionTime(event.getStartDate(), event.getExpirationTime(), event.getPrice(), event.getEmail());
        verify(valueOperations).set(eq(event.getTicketId()), eq(event), any(Duration.class));
        verify(event).setStatus(TicketEvent.TicketStatus.TO_BE_UPDATED);
    }

    @Test
    void shouldProcessFixedChargeToBeUpdated() throws IOException {
        TicketEvent event = fixedEvent(TicketEvent.TicketStatus.TO_BE_UPDATED);

        event.setStatus(TicketEvent.TicketStatus.TO_BE_UPDATED);
        when(objectMapper.readValue(any(byte[].class), eq(TicketEvent.class))).thenReturn(event);
        when(message.toString()).thenReturn("testMessage");

        listener.onMessage(message, new byte[]{});

        verify(emailService).sendFixedWarnMessage(event.getEmail());
        verify(valueOperations).set(eq(event.getTicketId()), eq(event), any(Duration.class));
        verify(event).setStatus(TicketEvent.TicketStatus.UPDATED);
    }

    @Test
    void shouldProcessFixedChargeUpdated() throws IOException {
        TicketEvent event = fixedEvent(TicketEvent.TicketStatus.UPDATED);

        when(objectMapper.readValue(any(byte[].class), eq(TicketEvent.class))).thenReturn(event);
        when(message.toString()).thenReturn("testMessage");

        listener.onMessage(message, new byte[]{});

        verify(finishParkingCommand).execute(event);
    }

    @Test
    void shouldHandleInvalidMessage() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(TicketEvent.class))).thenThrow(JsonProcessingException.class);
        when(message.toString()).thenReturn("invalidMessage");

        assertThrows(RuntimeException.class, () -> listener.onMessage(message, new byte[]{}));
    }
}
