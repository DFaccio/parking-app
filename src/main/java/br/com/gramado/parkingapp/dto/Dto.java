package br.com.gramado.parkingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dto {

    @Schema(example = "1")
    private Integer id;
}
