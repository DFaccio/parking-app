package br.com.gramado.parkingapp.controller;

import br.com.gramado.parkingapp.command.pricetable.*;
import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.dto.pricetable.PriceTableUpdateDto;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
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
@RequestMapping(value = "/price-table")
@Tag(name = "Tabela de Preço", description = "Métodos para Tabela de Preço")
public class PriceTableController {

    @Resource
    private InsertPriceTableCommand insertCommand;

    @Resource
    private FindPriceTableByParamsCommand findAllCommand;

    @Resource
    private UpdateNamePriceTableCommand updateNameCommand;

    @Resource
    private DisableTablePriceCommand disableCommand;

    @Resource
    private FindPriceTableById findById;

    @Resource
    private UpdateChargeInfoCommand updateChargeInfoCommand;

    @Operation(summary = "Cadastrar tabela de preço")
    @PostMapping
    public ResponseEntity<PriceTableDto> insert(@Valid @RequestBody PriceTableDto priceTableDto) throws ValidationsException {
        PriceTableDto dto = insertCommand.execute(priceTableDto);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar nome da tabela de preço")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PriceTableDto> update(@RequestParam String name, @PathVariable Integer id) throws NotFoundException {
        PriceTableDto person = updateNameCommand.execute(name, id);

        return ResponseEntity.ok(person);
    }

    @Operation(summary = "Recuperar tabela de preço por identificador")
    @GetMapping(value = "/find/{id}")
    public ResponseEntity<PriceTableDto> findById(@Parameter(example = "2") @PathVariable Integer id) throws NotFoundException {
        return ResponseEntity.ok(findById.execute(id));
    }

    @Operation(summary = "Desabilita tabela de preço")
    @PutMapping(value = "/disable/{id}")
    public ResponseEntity<PriceTableDto> disable(@PathVariable Integer id) throws NotFoundException {
        disableCommand.execute(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualiza informações de cobrança da tabela de preço")
    @PutMapping
    public ResponseEntity<PriceTableDto> updateChargeInfo(@Valid @RequestBody PriceTableUpdateDto priceTable) throws NotFoundException, ValidationsException {
        return ResponseEntity.ok(updateChargeInfoCommand.execute(priceTable));
    }

    @Operation(summary = "Recuperar tabela de preço por parâmetros com resultado paginado")
    @GetMapping
    public ResponseEntity<PagedResponse<PriceTableDto>> findAll(@Parameter(description = "Default value 10", example = "10") @RequestParam(required = false) Integer pageSize,
                                                                @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage,
                                                                @Parameter(example = "15") @RequestParam(required = false) Integer id,
                                                                @Parameter(example = "Happy Hour") @RequestParam(required = false) String name,
                                                                @Parameter(example = "HOUR") @RequestParam(required = false) TypeCharge typeCharge,
                                                                @Parameter(example = "false") boolean active) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllCommand.execute(id, name, typeCharge, active, page));
    }
}
