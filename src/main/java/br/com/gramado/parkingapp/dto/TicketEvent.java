package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TicketEvent {

    private final Integer ticketId;

    @Setter
    private LocalDateTime expirationTime;

    private final LocalDateTime startDate;

    private final TypeCharge typeCharge;

    @Setter
    private TicketStatus status;

    private final String email;

    private BigDecimal price;

    public enum TicketStatus {
        CREATED, TO_BE_UPDATED, UPDATED
    }

    @Builder
    public TicketEvent(Integer ticketId, LocalDateTime expirationTime, LocalDateTime startDate, TypeCharge typeCharge, TicketStatus status, String email, BigDecimal price) {
        this.ticketId = ticketId;
        this.expirationTime = expirationTime;
        this.startDate = startDate;
        this.typeCharge = typeCharge;
        this.status = status;
        this.email = email;
        this.price = price;
    }
}
