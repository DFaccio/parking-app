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
public class UpdatePersonCommand {

    @Resource
    private PersonServiceInterface personServiceInterface;

    @Resource
    private PersonConverter converter;

    public PersonDto execute(PersonDto personDto) throws NotFoundException {
        Optional<Person> savedOptional = personServiceInterface.findById(personDto.getId());

        if (savedOptional.isEmpty()) {
            throw new NotFoundException(personDto.getId(), "Person");
        }

        Person toUpdate = converter.convert(personDto);

        toUpdate.setId(savedOptional.get().getId());

        toUpdate = personServiceInterface.update(toUpdate);

        return converter.convert(toUpdate);
    }
}
