package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.converter.PersonConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdatePersonCommand {

    @Resource
    private PersonServiceInterface personServiceInterface;

    @Resource
    private PersonConverter converter;

    public PersonDto execute(PersonDto personDto) throws NotFoundException, ValidationsException {
        Optional<Person> savedOptional = personServiceInterface.findByDocument(personDto.getDocument());

        if (savedOptional.isEmpty()) {
            throw new NotFoundException(personDto.getId(), "Person");
        }

        Person toUpdate = converter.convert(personDto);

        toUpdate = personServiceInterface.update(toUpdate);

        return converter.convert(toUpdate);
    }
}
