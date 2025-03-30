package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FinishParkingCommand {

    private final EmailServiceInterface emailService;

    private final ParkingServiceInterface parkingService;

    public FinishParkingCommand(EmailServiceInterface emailService, ParkingServiceInterface parkingService) {
        this.emailService = emailService;
        this.parkingService = parkingService;
    }

    public void execute(Integer parkingId) throws ValidationsException {
        Optional<Parking> optional = getParking(parkingId);

        Parking parking = validateParking(optional);

        parking.setFinished(true);
        parking.setDateTimeEnd(TimeUtils.getTime());

        if (TypeCharge.HOUR.equals(parking.getPriceTable().getTypeCharge())) {
            parking.getPayment().setPrice(
                    calculateTotalValue(parking.getDateTimeStart(),
                            parking.getDateTimeEnd(),
                            parking.getPriceTable().getValue()
                    )
            );
        }

        parkingService.update(parking);

        emailService.sendPeriodClose(parking);
    }

    private Optional<Parking> getParking(Integer id) {
        return parkingService.findById(id);
    }

    private Parking validateParking(Optional<Parking> optional) throws ValidationsException {
        if (optional.isEmpty()) {
            throw new ValidationsException("Estacionamento n\u00E3o encontrado!");
        }

        Parking parking = optional.get();

        if (parking.isFinished()) {
            throw new ValidationsException("Estacionamento j\u00E1 finalizado!");
        }

        return parking;
    }

    private BigDecimal calculateTotalValue(LocalDateTime start, LocalDateTime end, BigDecimal value) {
        BigDecimal hours = TimeUtils.getDurationInHoursRoundedUp(start, end);

        return value.multiply(hours);
    }
}
