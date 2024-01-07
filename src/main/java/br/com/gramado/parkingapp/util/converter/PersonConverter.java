package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.util.DocumentType;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.stereotype.Component;

@Component
public class PersonConverter implements Converter<Person, PersonDto> {

    private static final String CPF_REGEX = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";

    private static final String CNPJ_REGEX = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}";

    private static final String PASSPORT_REGEX = "[A-Z]{2}\\d{6}";

    private static final String RNE_REGEX = "[A-Z]\\d{6}-[A-Z]";

    @Override
    public PersonDto convert(Person entity) {
        PersonDto dto = new PersonDto();

        dto.setId(entity.getId());
        dto.setDocument(entity.getDocument());
        dto.setActive(entity.isActive());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setPreferentialPayment(entity.getPreferentialPayment());
        dto.setDocumentType(entity.getDocumentType());

        return dto;
    }

    @Override
    public Person convert(PersonDto dto) throws ValidationsException {
        validateDocumentType(dto.getDocument(), dto.getDocumentType());

        Person person = new Person();

        person.setId(dto.getId());
        person.setDocument(dto.getDocument());
        person.setActive(dto.isActive());
        person.setName(dto.getName());
        person.setEmail(dto.getEmail());
        person.setPhone(dto.getPhone());
        person.setPreferentialPayment(dto.getPreferentialPayment());
        person.setDocumentType(dto.getDocumentType());

        return person;
    }

    private void validateDocumentType(String document, DocumentType type) throws ValidationsException {

        if (DocumentType.CPF.equals(type) && !document.matches(CPF_REGEX)) {
            throw new ValidationsException("Documento não está no padrão esperado para CPF");
        }

        if (DocumentType.CNPJ.equals(type) && !document.matches(CNPJ_REGEX)) {
            throw new ValidationsException("Documento não está no padrão esperado para CNPJ");
        }

        if (DocumentType.PASSPORT.equals(type) && !document.matches(PASSPORT_REGEX)) {
            throw new ValidationsException("Documento não está no padrão esperado para o passaporte");
        }

        if (DocumentType.RNE.equals(type) && !document.matches(RNE_REGEX)) {
            throw new ValidationsException("Documento não está no padrão esperado para o RNE");
        }
    }
}
