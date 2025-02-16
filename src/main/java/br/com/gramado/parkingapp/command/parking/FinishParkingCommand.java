package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
public class FinishParkingCommand {

    private final EmailServiceInterface emailService;

    private final ParkingServiceInterface parkingService;

    public FinishParkingCommand(EmailServiceInterface emailService, ParkingServiceInterface parkingService) {
        this.emailService = emailService;
        this.parkingService = parkingService;
    }

    public void execute(TicketEvent event) {
        Optional<Parking> optional = parkingService.findById(event.getTicketId());

        if (optional.isPresent()) {
            Parking parking = optional.get();

            if (!parking.isFinished()) {
                if (TypeCharge.HOUR.equals(parking.getPriceTable().getTypeCharge())) {
                    parking.getPayment().setPrice(parking.getPriceTable().getValue().multiply(
                                    new BigDecimal(TimeUtils.durationBetweenDates(event.getStartDate(), event.getExpirationTime()))
                            )
                            .setScale(2, RoundingMode.HALF_EVEN));

                    parking.setDateTimeEnd(event.getExpirationTime());
                }

                parking.setFinished(true);

                parking = parkingService.update(parking);

                emailService.sendPeriodClose(parking);
            }
        }
    }
}
