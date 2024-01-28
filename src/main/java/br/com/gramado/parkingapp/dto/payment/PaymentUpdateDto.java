package br.com.gramado.parkingapp.dto.payment;

import br.com.gramado.parkingapp.dto.Dto;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUpdateDto extends Dto {

    @Schema(example = "false")
    private boolean isPaid;

    @NotNull
    private TypePayment typePayment;
}
