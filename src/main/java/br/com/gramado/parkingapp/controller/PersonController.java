package br.com.gramado.parkingapp.controller;

import br.com.gramado.parkingapp.command.person.FindAllPersonCommand;
import br.com.gramado.parkingapp.command.person.FindPersonById;
import br.com.gramado.parkingapp.command.person.InsertPersonCommand;
import br.com.gramado.parkingapp.command.person.UpdatePersonCommand;
import br.com.gramado.parkingapp.dto.PersonDto;
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
@RequestMapping(value = "/person")
@Tag(name = "Pessoa", description = "Métodos para Pessoa")
public class PersonController {

    @Resource
    private InsertPersonCommand insertPersonCommand;

    @Resource
    private FindAllPersonCommand findAllCommand;

    @Resource
    private UpdatePersonCommand updateCommand;

    @Resource
    private FindPersonById findPersonById;


    @Operation(summary = "Cadastrar pessoa")
    @PostMapping
    public ResponseEntity<PersonDto> insert(@Valid @RequestBody PersonDto personDto) throws ValidationsException {
        PersonDto person = insertPersonCommand.execute(personDto);

        return ResponseEntity.ok(person);
    }

    @Operation(summary = "Recuperar pessoas com resultado paginado")
    @GetMapping
    public ResponseEntity<PagedResponse<PersonDto>> findAll(@Parameter(description = "Default value 10", example = "10") @RequestParam(required = false) Integer pageSize,
                                                            @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        Pagination page = new Pagination(initialPage, pageSize);

        return ResponseEntity.ok(findAllCommand.execute(page));
    }

    @Operation(summary = "Atualizar pessoa")
    @PutMapping
    public ResponseEntity<PersonDto> update(@Valid @RequestBody PersonDto personDto) throws NotFoundException {
        PersonDto person = updateCommand.execute(personDto);

        return ResponseEntity.ok(person);
    }

    @Operation(summary = "Recuperar pessoa por identificador")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDto> findById(@Parameter(example = "000.005.269-58") @PathVariable String identifier) throws NotFoundException {
        return ResponseEntity.ok(findPersonById.execute(identifier));
    }
}
