package br.com.gramado.parkingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto extends Dto {

    private PersonDto person;

    @NotBlank(message = "É um campo obrigatório")
    @Schema(example = "amarelo")
    private String color;

    @NotBlank(message = "É um campo obrigatório")
    @Schema(example = "renegade")
    private String model;

    @NotBlank(message = "É um campo obrigatório")
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}")
    private String plate;
}
