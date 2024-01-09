package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.converter.PersonConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindPersonById {

    @Resource
    private PersonServiceInterface personService;

    @Resource
    private PersonConverter personConverter;

    public PersonDto execute(String identifier) throws NotFoundException {
        Optional<Person> person = personService.findByDocument(identifier);

        if (person.isEmpty()) {
            throw new NotFoundException(identifier, "Person");
        }

        return personConverter.convert(person.get());
    }
}
