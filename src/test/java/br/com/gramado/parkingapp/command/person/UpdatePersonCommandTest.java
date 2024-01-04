package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.ParkingAppApplication;
import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
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
class UpdatePersonCommandTest {

    @MockBean
    private PersonServiceInterface service;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UpdatePersonCommand command;

    private static final String PATH = "src/test/java/br/com/gramado/parkingapp/command/person/input.json";

    @BeforeEach
    void setUp() throws IOException {
        Person person = objectMapper.readValue(new File(PATH), Person.class);
        Person person2 = objectMapper.readValue(new File(PATH), Person.class);

        Mockito.when(service.findById(any(String.class)))
                .thenReturn(Optional.of(person));

        person2.setName("Cristina");
        Mockito.when(service.update(any(Person.class)))
                .thenReturn(person2);
    }

    @Test
    void update() throws IOException, NotFoundException {
        PersonDto dto = objectMapper.readValue(new File(PATH), PersonDto.class);
        dto.setName("Cristina");
        dto.setId("244.181.050-25");

        PersonDto update = command.execute(dto);

        Assertions.assertEquals("244.181.050-26", update.getId());
    }
}