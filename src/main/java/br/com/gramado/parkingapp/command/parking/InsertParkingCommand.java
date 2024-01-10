package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.command.payment.InsertPaymentCommand;
import br.com.gramado.parkingapp.dto.ParkingCreateDto;
import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.parkingpayment.PaymentServiceInterface;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InsertParkingCommand {

    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private ParkingConverter parkingConverter;

    @Resource
    private VehicleServiceInterface vehicleService;

    @Resource
    private PriceTableServiceInterface priceTableService;

    @Resource
    private PaymentServiceInterface parkingPaymentService;

    @Resource
    private InsertPaymentCommand insertPaymentCommand;

    public ParkingDto execute(ParkingCreateDto parkingDto) throws ValidationsException {
        Vehicle vehicle = verifyAndGetVehicle(parkingDto.getVehicleId());
        PriceTable priceTable = verifyAndGetPriceTable(parkingDto.getPriceTableId());

        TypePayment typePayment = parkingDto.getTypePayment() == null ?
                vehicle.getPerson().getPreferentialPayment() :
                parkingDto.getTypePayment();

        Payment payment = insertPaymentCommand.execute(priceTable, typePayment);

        Parking parking = new Parking();
        parking.setPayment(payment);
        parking.setVehicle(vehicle);
        parking.setPriceTable(priceTable);
        parking.setPlate(parkingDto.getPlate());
        parking.setFinished(false);
        parking.setDateTimeStart(TimeUtils.getTime());

        if (TypeCharge.FIXED.equals(priceTable.getTypeCharge())) {
            parking.setDateTimeEnd(TimeUtils.addDurationInTime(parking.getDateTimeStart(), priceTable.getDuration()));
        }

        parking = parkingService.insert(parking);

        return parkingConverter.convert(parking);
    }

    private Vehicle verifyAndGetVehicle(Integer vehicleId) throws ValidationsException {
        Optional<Vehicle> optionalVehicle = vehicleService.findById(vehicleId);

        if (optionalVehicle.isEmpty()) {
            throw new ValidationsException("Ve\u00EDculo n\u00E3o encontrado!");
        }

        return optionalVehicle.get();
    }

    private PriceTable verifyAndGetPriceTable(Integer priceTableId) throws ValidationsException {
        Optional<PriceTable> optionalPriceTable = priceTableService.findById(priceTableId);

        if (optionalPriceTable.isEmpty()) {
            throw new ValidationsException("Tabela de pre\u00E7o n\u00E3o encontrada!");
        }

        PriceTable priceTable = optionalPriceTable.get();

        if (!priceTable.isActive()) {
            throw new ValidationsException("A tabela de pre\u00E7o informada est\u00E1 desabilitada!");
        }

        return priceTable;
    }
}