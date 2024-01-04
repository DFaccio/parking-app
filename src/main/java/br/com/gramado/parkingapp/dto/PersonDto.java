package br.com.gramado.parkingapp.dto;

import br.com.gramado.parkingapp.util.TypePayment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto extends DtoIdString {

    @Schema(example = "Isabela")
    @NotBlank(message = "É um campo obrigatório")
    private String name;

    @Schema(example = "false")
    private boolean active;

    @NotBlank(message = "É um campo obrigatório")
    @Pattern(regexp = "^\\+55[0-9]{2}9[0-9]{4}-[0-9]{4}")
    private String phone;

    @Email
    @Schema(example = "isabela@gmail.com")
    private String email;

    @NotNull
    private TypePayment preferentialPayment;
}
