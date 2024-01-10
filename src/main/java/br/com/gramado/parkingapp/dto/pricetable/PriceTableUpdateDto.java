package br.com.gramado.parkingapp.dto.pricetable;

import br.com.gramado.parkingapp.dto.Dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceTableUpdateDto extends Dto {

    @NotNull
    @NotBlank
    @Schema(example = "5.00")
    private BigDecimal value;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{2}")
    @Schema(example = "02:00:00")
    @NotNull
    @NotBlank
    private String duration;
}
