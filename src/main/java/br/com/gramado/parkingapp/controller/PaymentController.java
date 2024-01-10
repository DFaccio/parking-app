package br.com.gramado.parkingapp.controller;

import br.com.gramado.parkingapp.command.payment.FindPaymentById;
import br.com.gramado.parkingapp.command.payment.UpdatePaymentCommand;
import br.com.gramado.parkingapp.dto.payment.PaymentDto;
import br.com.gramado.parkingapp.dto.payment.PaymentUpdateDto;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/parking-payment")
@Tag(name = "Pagamento", description = "Métodos para registrar o pagamento do estacionamentos")
public class PaymentController {

    @Resource
    private UpdatePaymentCommand updatePaymentCommand;

    @Resource
    private FindPaymentById findPaymentById;

    @Operation(summary = "Atualizar pagamento")
    @PutMapping
    public ResponseEntity<PaymentDto> update(@Valid @RequestBody PaymentUpdateDto paymentDto) throws NotFoundException, ValidationsException {
        PaymentDto parkingPaymentDto = updatePaymentCommand.execute(paymentDto);

        return ResponseEntity.ok(parkingPaymentDto);
    }

    @Operation(summary = "Recuperar pagamento por identificador")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDto> findById(@Parameter(example = "1") @PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(findPaymentById.execute(id));
    }
}