package br.com.gramado.parkingapp.command.person;

import br.com.gramado.parkingapp.ParkingAppApplication;
import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ParkingAppApplication.class)
class InsertPersonCommandTest {
    @MockBean
    private PersonServiceInterface service;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private InsertPersonCommand command;

    private static final String PATH = "src/test/java/br/com/gramado/parkingapp/command/person";

    @Test
    void insert() throws ValidationsException, IOException {
        String filename = PATH + "/input.json";

        Person person = objectMapper.readValue(new File(PATH + "/input.json"), Person.class);

        Mockito.when(service.insert(any(Person.class)))
                .thenReturn(person);

        Mockito.when(service.findByDocument(any(String.class)))
                .thenReturn(Optional.empty());

        PersonDto dto = objectMapper.readValue(new File(filename), PersonDto.class);

        PersonDto saved = command.execute(dto);

        Assertions.assertEquals(dto.getDocument(), saved.getDocument());
        Assertions.assertEquals(dto.getPreferentialPayment(), saved.getPreferentialPayment());
        Assertions.assertEquals(dto.getName(), saved.getName());
    }

    @Test
    void insertAlreadySaved() throws IOException {
        String filename = PATH + "/input.json";

        Person person = objectMapper.readValue(new File(filename), Person.class);

        Mockito.when(service.findByDocument(any(String.class)))
                .thenReturn(Optional.of(person));

        PersonDto dto = objectMapper.readValue(new File(filename), PersonDto.class);

        Assertions.assertThrows(ValidationsException.class, () -> command.execute(dto));
    }

    void testTypeDocument(String filename, String message) throws IOException {
        PersonDto dto = objectMapper.readValue(new File(PATH + "/" + filename), PersonDto.class);

        try {
            command.execute(dto);
        } catch (ValidationsException e) {
            assertEquals(e.getMessage(), message);
        }
    }

    @Test
    void insertVehicleWrongDocument_CPF() throws IOException {
        testTypeDocument("input_cpf.json", "Documento não está no padrão esperado para CPF");
    }

    @Test
    void insertVehicleWrongDocument_CNPJ() throws IOException {
        testTypeDocument("input_cnpj.json", "Documento não está no padrão esperado para CNPJ");
    }

    @Test
    void insertVehicleWrongDocument_Passport() throws IOException {
        testTypeDocument("input_passport.json", "Documento não está no padrão esperado para o passaporte");
    }

    @Test
    void insertVehicleWrongDocument_rne() throws IOException {
        testTypeDocument("input_rne.json", "Documento não está no padrão esperado para o RNE");
    }
}