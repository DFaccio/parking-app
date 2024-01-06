package br.com.gramado.parkingapp.controller;


import br.com.gramado.parkingapp.command.parkingpayment.FindAllParkingPaymentCommand;
import br.com.gramado.parkingapp.command.parkingpayment.FindParkingPaymentById;
import br.com.gramado.parkingapp.command.parkingpayment.InsertParkingPaymentCommand;
import br.com.gramado.parkingapp.command.parkingpayment.UpdateParkingPaymentCommand;
import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/parkingpayment")
@Tag(name = "ParkingPayment", description = "Métodos para registrar o pagamento dos veículos estacionamentos")
public class ParkingPaymentController {
    @Resource
    private InsertParkingPaymentCommand insertParkingPaymentCommand;

    @Resource
    private FindAllParkingPaymentCommand findAllParkingPaymentCommand;

    @Resource
    private UpdateParkingPaymentCommand updateParkingPaymentCommand;

    @Resource
    private FindParkingPaymentById findParkingPaymentById;

    @Operation(summary = "Cadastrar pagamento")
    @PostMapping
    public ResponseEntity<ParkingPaymentDto> insert(@Valid @RequestBody ParkingPaymentDto parkingPaymentDto) throws ValidationsException {
        ParkingPaymentDto parkingPayment = insertParkingPaymentCommand.execute(parkingPaymentDto);

        return ResponseEntity.ok(parkingPayment);
    }

    @Operation(summary = "Recuperar pagamento com resultado paginado")
    @GetMapping
    public ResponseEntity<PagedResponse<ParkingPaymentDto>> findAll(@Parameter(description = "Default value 10", example = "10") @RequestParam(required = false) Integer pageSize,
                                                             @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllParkingPaymentCommand.execute(page));
    }

    @Operation(summary = "Atualizar pagamento")
    @PutMapping
    public ResponseEntity<ParkingPaymentDto> update(@Valid @RequestBody ParkingPaymentDto parkingPaymentDto) throws NotFoundException, ValidationsException {
        ParkingPaymentDto parkingPayment = updateParkingPaymentCommand.execute(parkingPaymentDto);

        return ResponseEntity.ok(parkingPayment);
    }

    @Operation(summary = "Recuperar pagamento por identificador")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingPaymentDto> findById(@Parameter(example = "1") @PathVariable Integer identifier) throws NotFoundException {
        return ResponseEntity.ok(findParkingPaymentById.execute(identifier));
    }
}
