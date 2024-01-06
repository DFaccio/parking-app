package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.TypeParking;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDto extends Dto {

    private VehicleDto vehicleDto;

    @NotBlank(message = "É necessário informar um tipo de pagamento")
    private TypeParking typeParking;

    @NotBlank(message = "É necessário informar uma hora inicial")
    private LocalDateTime dateTimeStart;

    @NotBlank(message = "É necessário informar uma hora final")
    private LocalDateTime dateTimeEnd;

    private String plate;


}
