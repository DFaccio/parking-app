package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter implements Converter<Person, PersonDto> {

    @Override
    public PersonDto convert(Person entity) {
        PersonDto dto = new PersonDto();

        dto.setId(entity.getId());
        dto.setActive(entity.isActive());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPreferentialPayment(entity.getPreferentialPayment());

        return dto;
    }

    @Override
    public Person convert(PersonDto dto) {
        Person person = new Person();

        person.setId(dto.getId());
        person.setActive(dto.isActive());
        person.setName(dto.getName());
        person.setEmail(dto.getEmail());
        person.setPhone(dto.getPhone());
        person.setPreferentialPayment(dto.getPreferentialPayment());

        return person;
    }
}
