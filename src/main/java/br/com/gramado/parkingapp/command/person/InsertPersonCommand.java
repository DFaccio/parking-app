package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.converter.PersonConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InsertPersonCommand {

    @Resource
    private PersonServiceInterface service;

    @Resource
    private PersonConverter converter;

    public PersonDto execute(PersonDto personDto) throws ValidationsException {
        Person person = converter.convert(personDto);

        Optional<Person> saved = service.findById(person.getId());

        if (saved.isPresent()) {
            throw new ValidationsException("Pessoa j\u00E1 cadastrada");
        }

        person = service.insert(person);

        return converter.convert(person);
    }
}
