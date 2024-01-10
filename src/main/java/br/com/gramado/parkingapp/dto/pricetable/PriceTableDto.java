package br.com.gramado.parkingapp.dto.pricetable;

import br.com.gramado.parkingapp.dto.Dto;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class PriceTableDto extends Dto {

    @Schema(example = "true")
    private boolean active;

    @NotNull
    @Schema(example = "HOUR")
    private TypeCharge typeCharge;

    @NotNull
    @Schema(example = "5.00")
    private BigDecimal value;

    @Schema(example = "Happy Hour")
    private String name;

    @Pattern(regexp = "[0-9]{2}:[0-9]{2}:[0-9]{2}")
    @Schema(example = "02:00:00")
    private String duration;
}
