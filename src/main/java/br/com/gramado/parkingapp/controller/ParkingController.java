package br.com.gramado.parkingapp.controller;


import br.com.gramado.parkingapp.command.parking.FindAllParkingCommand;
import br.com.gramado.parkingapp.command.parking.FindParkingById;
import br.com.gramado.parkingapp.command.parking.InsertParkingCommand;
import br.com.gramado.parkingapp.command.parking.UpdateParkingCommand;
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
@Tag(name = "Parking", description = "Métodos para registrar veículos estacionamentos")
public class ParkingController {
    @Resource
    private InsertParkingCommand insertParkingCommand;

    @Resource
    private FindAllParkingCommand findAllParkingCommand;

    @Resource
    private UpdateParkingCommand updateParkingCommand;

    @Resource
    private FindParkingById findParkingById;

    @Operation(summary = "Cadastrar parking")
    @PostMapping
    public ResponseEntity<ParkingDto> insert(@Valid @RequestBody ParkingDto parkingDto) throws ValidationsException {
        ParkingDto parking = insertParkingCommand.execute(parkingDto);

        return ResponseEntity.ok(parking);
    }

    @Operation(summary = "Recuperar parking com resultado paginado")
    @GetMapping
    public ResponseEntity<PagedResponse<ParkingDto>> findAll(@Parameter(description = "Default value 10", example = "10") @RequestParam(required = false) Integer pageSize,
                                                            @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllParkingCommand.execute(page));
    }

    @Operation(summary = "Atualizar parking")
    @PutMapping
    public ResponseEntity<ParkingDto> update(@Valid @RequestBody ParkingDto parkingDto) throws NotFoundException, ValidationsException {
        ParkingDto parking = updateParkingCommand.execute(parkingDto);

        return ResponseEntity.ok(parking);
    }

    @Operation(summary = "Recuperar parking por identificador")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ParkingDto> findById(@Parameter(example = "000.005.269-58") @PathVariable Integer identifier) throws NotFoundException {
        return ResponseEntity.ok(findParkingById.execute(identifier));
    }
}
