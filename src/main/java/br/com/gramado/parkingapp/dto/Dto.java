package br.com.gramado.parkingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dto {

    @Schema(example = "1")
    @NotBlank(message = "É um campo obrigatório")
    private Integer identifier;
}
