package br.com.gramado.parkingapp.controller;

import br.com.gramado.parkingapp.command.vehicle.FindAllVehicleCommand;
import br.com.gramado.parkingapp.command.vehicle.FindVehicleCommand;
import br.com.gramado.parkingapp.command.vehicle.InsertVehicleCommand;
import br.com.gramado.parkingapp.dto.VehicleDto;
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
@RequestMapping(value = "/vehicle")
@Tag(name = "Veículo", description = "Métodos para Veículo")
public class VehicleController {

    @Resource
    private FindAllVehicleCommand findAllCommand;

    @Resource
    private InsertVehicleCommand insertVehicleCommand;

    @Resource
    private FindVehicleCommand findVehicleCommand;

    @GetMapping
    @Operation(summary = "Recuperar veículos com resultado paginado")
    public ResponseEntity<PagedResponse<VehicleDto>> findAll(@Parameter(description = "Default value 10. Max value 1000", example = "10") @RequestParam(required = false) Integer pageSize,
                                                             @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllCommand.execute(page));
    }

    @Operation(summary = "Cadastrar veículo")
    @PostMapping
    public ResponseEntity<VehicleDto> insert(@Valid @RequestBody VehicleDto vehicleDto) throws ValidationsException {
        VehicleDto vehicle = insertVehicleCommand.execute(vehicleDto);

        return ResponseEntity.ok(vehicle);
    }

    @Operation(summary = "Recuperar veículo por identificador da pessoa e a placa")
    @GetMapping(value = "/{plate}")
    public ResponseEntity<VehicleDto> findById(@Parameter(example = "RDQ9690") @PathVariable String plate, @Parameter(example = "05.963.859-89") @RequestParam String person) throws ValidationsException {
        return ResponseEntity.ok(findVehicleCommand.execute(plate, person));
    }
}
