package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TicketEvent {

    private Integer ticketId;

    @Setter
    private LocalDateTime expirationTime;

    private LocalDateTime startDate;

    private TypeCharge typeCharge;

    @Setter
    private TicketStatus status;

    private String email;

    private BigDecimal price;

    public enum TicketStatus {
        CREATED, TO_BE_UPDATED, UPDATED
    }
}
