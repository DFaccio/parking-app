package br.com.gramado.parkingapp.controller;

import br.com.gramado.parkingapp.command.parking.FindAllParkingCommand;
import br.com.gramado.parkingapp.command.parking.FindParkingById;
import br.com.gramado.parkingapp.command.parking.FinishParkingManuallyCommand;
import br.com.gramado.parkingapp.command.parking.InsertParkingCommand;
import br.com.gramado.parkingapp.dto.ParkingCreateDto;
import br.com.gramado.parkingapp.dto.ParkingDto;
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
@RequestMapping(value = "/parking")
@Tag(name = "Estacionamento", description = "Métodos para registrar estacionamentos")
public class ParkingController {

    @Resource
    private InsertParkingCommand insertParkingCommand;

    @Resource
    private FindAllParkingCommand findAllParkingCommand;

    @Resource
    private FindParkingById findParkingById;

    @Resource
    private FinishParkingManuallyCommand finishParking;

    @Operation(summary = "Cadastrar estacionamento")
    @PostMapping
    public ResponseEntity<ParkingDto> insert(@Valid @RequestBody ParkingCreateDto parkingDto) throws ValidationsException {
        ParkingDto parking = insertParkingCommand.execute(parkingDto);

        return ResponseEntity.ok(parking);
    }

    @Operation(summary = "Recuperar estacionamento com resultado paginado")
    @GetMapping
    public ResponseEntity<PagedResponse<ParkingDto>> findAll(@Parameter(description = "Default value 10", example = "10") @RequestParam(required = false) Integer pageSize,
                                                             @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage,
                                                             @Parameter(example = "false") @RequestParam boolean isFinished) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllParkingCommand.execute(page, isFinished));
    }

    @Operation(summary = "Recuperar estacionamento por identificador")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingDto> findById(@Parameter(example = "15") @PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(findParkingById.execute(id));
    }

    @Operation(summary = "Encerrar estacionamento manualmente")
    @PutMapping(value = "/finished/{id}")
    public ResponseEntity<String> disable(@Parameter(example = "1") @PathVariable Integer id) throws ValidationsException {
        String receipt = finishParking.execute(id);

        return ResponseEntity.ok(receipt);
    }
}