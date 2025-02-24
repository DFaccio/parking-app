package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.command.notification.CreateOnRedisCommand;
import br.com.gramado.parkingapp.command.payment.InsertPaymentCommand;
import br.com.gramado.parkingapp.dto.parking.ParkingCreateDto;
import br.com.gramado.parkingapp.dto.parking.ParkingDto;
import br.com.gramado.parkingapp.entity.*;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsertParkingCommandTest {

    @Mock
    private ParkingServiceInterface parkingService;

    @Mock
    private ParkingConverter parkingConverter;

    @Mock
    private VehicleServiceInterface vehicleService;

    @Mock
    private PriceTableServiceInterface priceTableService;

    @Mock
    private CreateOnRedisCommand createNotificationCommand;

    @Mock
    private InsertPaymentCommand insertPaymentCommand;

    @Mock
    private EmailServiceInterface emailServiceInterface;

    @InjectMocks
    private InsertParkingCommand insertParkingCommand;

    private ParkingCreateDto validParkingCreateDto;
    private Vehicle vehicle;
    private PriceTable priceTable;
    private Person person;

    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        person = new Person();
        person.setName("John Doe");
        person.setEmail("johndoe@example.com");
        person.setActive(true);
        vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setPerson(person);

        priceTable = new PriceTable();
        priceTable.setId(1);
        priceTable.setActive(true);
        priceTable.setTypeCharge(TypeCharge.FIXED);
        priceTable.setDuration(LocalTime.of(2, 0, 0));

        validParkingCreateDto = new ParkingCreateDto(1, 1, "ABC-1234", TypePayment.CREDIT);

        fixedClock = Clock.fixed(Instant.parse("2025-02-16T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
    }

    @Test
    void shouldCreateParkingSuccessfully() throws ValidationsException {
        MockedStatic<TimeUtils> mockedStatic = Mockito.mockStatic(TimeUtils.class);

        when(TimeUtils.getTime()).thenReturn(LocalDateTime.now(fixedClock));
        when(TimeUtils.addDurationInTime(any(), any()))
                .thenReturn(LocalDateTime.of(2025, 2, 16, 14, 0, 0, 0));

        when(vehicleService.findById(1)).thenReturn(Optional.of(vehicle));

        when(priceTableService.findById(1)).thenReturn(Optional.of(priceTable));

        when(parkingService.insert(any(Parking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(insertPaymentCommand.execute(any(PriceTable.class), any(TypePayment.class)))
                .thenReturn(new Payment());

        when(parkingConverter.convert(any(Parking.class))).thenReturn(new ParkingDto());

        ParkingDto result = insertParkingCommand.execute(validParkingCreateDto);

        assertNotNull(result);
        verify(parkingService).insert(any(Parking.class));
        verify(createNotificationCommand).createRedisExpirationEvent(any(Parking.class));
        verify(emailServiceInterface).sendEmailParkingStarted(
                eq(person.getEmail()),
                eq(priceTable.getTypeCharge()),
                eq(LocalDateTime.of(2025, 2, 16, 14, 0, 0, 0))
        );

        mockedStatic.close();
    }

    @Test
    void shouldThrowValidationExceptionWhenVehicleNotFound() {
        when(vehicleService.findById(1)).thenReturn(Optional.empty());

        ValidationsException exception = assertThrows(ValidationsException.class, () -> insertParkingCommand.execute(validParkingCreateDto));

        assertEquals("Veículo não encontrado!", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenPriceTableNotFound() {
        when(vehicleService.findById(1)).thenReturn(Optional.of(vehicle));
        when(priceTableService.findById(1)).thenReturn(Optional.empty());

        ValidationsException exception = assertThrows(ValidationsException.class, () -> insertParkingCommand.execute(validParkingCreateDto));

        assertEquals("Tabela de preço não encontrada!", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenPriceTableIsInactive() {
        priceTable.setActive(false);
        when(vehicleService.findById(1)).thenReturn(Optional.of(vehicle));
        when(priceTableService.findById(1)).thenReturn(Optional.of(priceTable));

        ValidationsException exception = assertThrows(ValidationsException.class, () -> insertParkingCommand.execute(validParkingCreateDto));

        assertEquals("A tabela de preço informada está desabilitada!", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenPersonIsInactive() {
        person.setActive(false);
        when(vehicleService.findById(1)).thenReturn(Optional.of(vehicle));
        when(priceTableService.findById(1)).thenReturn(Optional.of(priceTable));

        ValidationsException exception = assertThrows(ValidationsException.class, () -> {
            insertParkingCommand.execute(validParkingCreateDto);
        });

        assertEquals("É necessário que o cadastro esteja ativo para registrar estacionamento!", exception.getMessage());
    }
}
