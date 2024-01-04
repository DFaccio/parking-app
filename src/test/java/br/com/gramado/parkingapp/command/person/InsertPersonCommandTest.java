package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.ParkingAppApplication;
import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ParkingAppApplication.class)
class InsertPersonCommandTest {
    @MockBean
    private PersonServiceInterface service;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private InsertPersonCommand command;

    private Person person;

    private static final String PATH = "src/test/java/br/com/gramado/parkingapp/command/person/input.json";

    @BeforeEach
    void setUp() throws IOException {
        person = objectMapper.readValue(new File(PATH), Person.class);

        Mockito.when(service.insert(any(Person.class)))
                .thenReturn(person);
    }

    @Test
    void insert() throws ValidationsException, IOException {
        Mockito.when(service.findById(any(String.class)))
                .thenReturn(Optional.empty());

        PersonDto dto = objectMapper.readValue(new File(PATH), PersonDto.class);

        PersonDto saved = command.execute(dto);

        Assertions.assertEquals(dto.getId(), saved.getId());
        Assertions.assertEquals(dto.getPreferentialPayment(), saved.getPreferentialPayment());
        Assertions.assertEquals(dto.getName(), saved.getName());
    }

    @Test
    void insertAlreadySaved() throws IOException {
        Mockito.when(service.findById(any(String.class)))
                .thenReturn(Optional.of(person));

        PersonDto dto = objectMapper.readValue(new File(PATH), PersonDto.class);

        Assertions.assertThrows(ValidationsException.class, () -> command.execute(dto));
    }
}