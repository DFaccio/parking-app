package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.TicketEvent;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.tickets.TicketEventServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FinishParkingCommand {

    // todo atualizar encerramento manual

    private final EmailServiceInterface emailService;

    private final ParkingServiceInterface parkingService;

    private final TicketEventServiceInterface service;

    public FinishParkingCommand(EmailServiceInterface emailService, ParkingServiceInterface parkingService, TicketEventServiceInterface service) {
        this.emailService = emailService;
        this.parkingService = parkingService;
        this.service = service;
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

        updateAndNotifyUser(parking);

        //removeParkingFromRedis(parkingId);
    }

    /**
     * Redis only finalize fixed periods
     */
    public void execute(TicketEvent event) {
        Optional<Parking> optional = getParking(event.getTicketId());

        if (optional.isEmpty()) {
            System.out.println("Event not found: " + event);
        } else {
            Parking parking = optional.get();

            if (!parking.isFinished()) {
                parking.setFinished(true);

                updateAndNotifyUser(parking);
            }
        }
    }

    private void updateAndNotifyUser(Parking parking) {
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
