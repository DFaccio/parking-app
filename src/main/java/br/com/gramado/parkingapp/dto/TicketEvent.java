package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketEvent {

    private Integer ticketId;

    @Setter
    private LocalDateTime expirationTime;

    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TypeCharge typeCharge;

    @Setter
    private TicketStatus status;

    private String email;

    private BigDecimal price;

    public enum TicketStatus {
        CREATED, TO_BE_UPDATED, UPDATED
    }
}
