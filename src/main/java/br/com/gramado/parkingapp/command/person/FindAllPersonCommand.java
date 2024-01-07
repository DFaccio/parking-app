package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.converter.PersonConverter;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FindAllPersonCommand {

    @Resource
    private PersonServiceInterface personService;

    @Resource
    private PersonConverter personConverter;

    public PagedResponse<PersonDto> execute(Pagination page) {
        Page<Person> result = personService.findAll(page);

        return personConverter.convertEntities(result);
    }
}
