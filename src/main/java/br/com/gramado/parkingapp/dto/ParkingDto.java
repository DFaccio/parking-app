package br.com.gramado.parkingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private VehicleDto vehicle;

    @NotBlank(message = "É necessário informar uma hora inicial")
    private LocalDateTime dateTimeStart;

    @NotBlank(message = "É necessário informar uma hora final")
    private LocalDateTime dateTimeEnd;

    @Schema(example = "EST-01")
    private String plate;
}