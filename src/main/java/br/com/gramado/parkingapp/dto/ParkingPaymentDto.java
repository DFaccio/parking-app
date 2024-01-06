package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.TypePayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPaymentDto extends Dto{
    @NotNull(message = "Parking deve ser informado")
    private ParkingDto parkingDto;

    @Schema(example = "false")
    private boolean isPaid;

    @NotBlank(message = "É necessário informar um tipo de pagamento")
    private TypePayment PaymentType;

    @NotBlank(message = "É necessário informar uma hora inicial")
    private LocalDateTime dateTimePayment;

    @NotBlank(message = "Valor deve ser informado")
    private double price;

}
