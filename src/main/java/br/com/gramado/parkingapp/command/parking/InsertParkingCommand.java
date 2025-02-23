package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.command.notification.CreateOnRedisCommand;
import br.com.gramado.parkingapp.command.payment.InsertPaymentCommand;
import br.com.gramado.parkingapp.dto.parking.ParkingCreateDto;
import br.com.gramado.parkingapp.dto.parking.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsertParkingCommand {

    private static final String VEHICLE_NOT_FOUND_MSG = "Ve\u00EDculo n\u00E3o encontrado!";
    private static final String PRICE_TABLE_NOT_FOUND_MSG = "Tabela de pre\u00E7o n\u00E3o encontrada!";
    private static final String INACTIVE_PRICE_TABLE_MSG = "A tabela de pre\u00E7o informada est\u00E1 desabilitada!";
    private static final String INACTIVE_PERSON_MSG = "\u00C9 necess\u00E1rio que o cadastro esteja ativo para registrar estacionamento!";

    private final ParkingServiceInterface parkingService;

    private final ParkingConverter parkingConverter;

    private final VehicleServiceInterface vehicleService;

    private final PriceTableServiceInterface priceTableService;

    private final CreateOnRedisCommand createNotificationCommand;

    private final InsertPaymentCommand insertPaymentCommand;

    private final EmailServiceInterface emailServiceInterface;

    @Autowired
    public InsertParkingCommand(ParkingServiceInterface parkingService, ParkingConverter parkingConverter, VehicleServiceInterface vehicleService, PriceTableServiceInterface priceTableService, CreateOnRedisCommand createNotificationCommand, InsertPaymentCommand insertPaymentCommand, EmailServiceInterface emailServiceInterface) {
        this.parkingService = parkingService;
        this.parkingConverter = parkingConverter;
        this.vehicleService = vehicleService;
        this.priceTableService = priceTableService;
        this.createNotificationCommand = createNotificationCommand;
        this.insertPaymentCommand = insertPaymentCommand;
        this.emailServiceInterface = emailServiceInterface;
    }

    public ParkingDto execute(ParkingCreateDto parkingDto) throws ValidationsException {
        Vehicle vehicle = verifyAndGetVehicle(parkingDto.getVehicleId());
        PriceTable priceTable = verifyAndGetPriceTable(parkingDto.getPriceTableId());

        verifyPersonIsActive(vehicle.getPerson().isActive());

        TypePayment typePayment = getTypePayment(parkingDto, vehicle);

        Payment payment = insertPaymentCommand.execute(priceTable, typePayment);

        Parking parking = createParking(parkingDto, vehicle, priceTable, payment);

        if (TypeCharge.FIXED.equals(priceTable.getTypeCharge())) {
            parking.setDateTimeEnd(TimeUtils.addDurationInTime(parking.getDateTimeStart(), priceTable.getDuration()));
        }

        parking = parkingService.insert(parking);

        createNotificationCommand.createRedisExpirationEvent(parking);

        emailServiceInterface.sendEmailParkingStarted(parking.getVehicle().getPerson().getEmail(),
                parking.getPriceTable().getTypeCharge(), parking.getDateTimeEnd());

        return parkingConverter.convert(parking);
    }

    private TypePayment getTypePayment(ParkingCreateDto parkingDto, Vehicle vehicle) {
        return parkingDto.getTypePayment() != null ? parkingDto.getTypePayment() : vehicle.getPerson().getPreferentialPayment();
    }

    private Parking createParking(ParkingCreateDto parkingDto, Vehicle vehicle, PriceTable priceTable, Payment payment) {
        return new Parking(
                null,
                vehicle,
                TimeUtils.getTime(),
                null,
                parkingDto.getPlate(),
                priceTable,
                payment,
                false);
    }

    private void verifyPersonIsActive(boolean active) throws ValidationsException {
        if (!active) {
            throw new ValidationsException(INACTIVE_PERSON_MSG);
        }
    }

    private Vehicle verifyAndGetVehicle(Integer vehicleId) throws ValidationsException {
        return vehicleService.findById(vehicleId)
                .orElseThrow(() -> new ValidationsException(VEHICLE_NOT_FOUND_MSG));
    }

    private PriceTable verifyAndGetPriceTable(Integer priceTableId) throws ValidationsException {
        PriceTable priceTable = priceTableService.findById(priceTableId)
                .orElseThrow(() -> new ValidationsException(PRICE_TABLE_NOT_FOUND_MSG));

        if (!priceTable.isActive()) {
            throw new ValidationsException(INACTIVE_PRICE_TABLE_MSG);
        }

        return priceTable;
    }
}