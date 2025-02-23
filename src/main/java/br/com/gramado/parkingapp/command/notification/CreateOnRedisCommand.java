package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class CreateOnRedisCommand {

    private final RedisTemplate<Integer, TicketEvent> redisTemplate;

    public CreateOnRedisCommand(RedisTemplate<Integer, TicketEvent> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void createRedisExpirationEvent(Parking parking) {
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

        Duration ttl = Duration.between(LocalDateTime.now(), event.getExpirationTime());

        redisTemplate.opsForValue().set(event.getTicketId(), event, ttl);
    }
}
