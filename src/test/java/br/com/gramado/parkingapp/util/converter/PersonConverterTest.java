package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.PersonDto;
import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.util.enums.DocumentType;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonConverterTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String PATH = "src/test/java/br/com/gramado/parkingapp/util/converter";

    private final PersonConverter converter = new PersonConverter();

    private PersonDto personDto;

    private Person person;

    @BeforeEach
    void setUp() {
        personDto = new PersonDto();
        personDto.setId(1);
        personDto.setDocument("123.456.789-00");
        personDto.setActive(true);
        personDto.setName("John Doe");
        personDto.setEmail("john@example.com");
        personDto.setPhone("123456789");
        personDto.setPreferentialPayment(TypePayment.CREDIT);
        personDto.setDocumentType(DocumentType.CPF);

        person = new Person();
        person.setId(1);
        person.setDocument("123.456.789-00");
        person.setActive(true);
        person.setName("John Doe");
        person.setEmail("john@example.com");
        person.setPhone("123456789");
        person.setPreferentialPayment(TypePayment.CREDIT);
        person.setDocumentType(DocumentType.CPF);
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

    private void testTypeDocument(String filename, String message) throws IOException {
        PersonDto dto = objectMapper.readValue(new File(PATH + "/" + filename), PersonDto.class);

        ValidationsException exception = assertThrows(ValidationsException.class, () -> converter.convert(dto));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConvertToDto() {
        PersonDto dto = converter.convert(person);

        assertEquals(personDto.getId(), dto.getId());
        assertEquals(personDto.getDocument(), dto.getDocument());
        assertEquals(personDto.isActive(), dto.isActive());
        assertEquals(personDto.getName(), dto.getName());
        assertEquals(personDto.getEmail(), dto.getEmail());
        assertEquals(personDto.getPhone(), dto.getPhone());
        assertEquals(personDto.getPreferentialPayment(), dto.getPreferentialPayment());
        assertEquals(personDto.getDocumentType(), dto.getDocumentType());
    }

    @Test
    void testConvertToEntity() throws ValidationsException {
        Person entity = converter.convert(personDto);

        assertEquals(person.getId(), entity.getId());
        assertEquals(person.getDocument(), entity.getDocument());
        assertEquals(person.isActive(), entity.isActive());
        assertEquals(person.getName(), entity.getName());
        assertEquals(person.getEmail(), entity.getEmail());
        assertEquals(person.getPhone(), entity.getPhone());
        assertEquals(person.getPreferentialPayment(), entity.getPreferentialPayment());
        assertEquals(person.getDocumentType(), entity.getDocumentType());
    }
}