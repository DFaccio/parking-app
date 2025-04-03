package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.converter.PersonConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InsertPersonCommandTest {

    @Mock
    private PersonServiceInterface service;

    @Mock
    private PersonConverter converter;

    @InjectMocks
    private InsertPersonCommand insertPersonCommand;

    private PersonDto personDto;

    private Person person;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personDto = new PersonDto();
        personDto.setDocument("123456789");

        person = new Person();
        person.setDocument("123456789");
    }

    @Test
    void testExecute_Success() throws ValidationsException {
        when(converter.convert(personDto))
                .thenReturn(person);

        when(service.findByDocument(person.getDocument()))
                .thenReturn(Optional.empty());

        when(service.insert(person))
                .thenReturn(person);

        when(converter.convert(person))
                .thenReturn(personDto);

        PersonDto result = insertPersonCommand.execute(personDto);

        assertEquals(personDto, result);
        verify(service).insert(person);
    }

    @Test
    void testExecute_PersonAlreadyExists() throws ValidationsException {
        when(converter.convert(personDto))
                .thenReturn(person);

        when(service.findByDocument(person.getDocument()))
                .thenReturn(Optional.of(person));

        ValidationsException exception = assertThrows(ValidationsException.class, () ->
                insertPersonCommand.execute(personDto));

        assertEquals("Pessoa já cadastrada", exception.getMessage());
    }
}