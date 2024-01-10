package br.com.gramado.parkingapp.dto.payment;

import br.com.gramado.parkingapp.dto.Dto;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto extends Dto {

    @Schema(example = "false")
    private boolean isPaid;

    @NotBlank(message = "É necessário informar uma hora inicial")
    private LocalDateTime dateTimePayment;

    @NotBlank(message = "Valor deve ser informado")
    private BigDecimal price;

    @NotBlank
    @NotNull
    private TypePayment typePayment;
}