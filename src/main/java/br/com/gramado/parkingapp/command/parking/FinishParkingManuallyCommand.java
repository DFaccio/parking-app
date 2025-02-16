package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.EmailMessages;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FinishParkingManuallyCommand {

    @Resource
    private ParkingServiceInterface service;

    // TODO ON ENCERRAR MANUAL TEM QUE REMOVER DO REDIS/FILA

    public String execute(Integer parkingId) throws ValidationsException {
        Optional<Parking> optional = service.findById(parkingId);

        if (optional.isEmpty()) {
            throw new ValidationsException("Estacionamento n\u00E3o encontrado!");
        }

        Parking parking = optional.get();

        if (parking.isFinished()) {
            throw new ValidationsException("Estacionamento j\u00E1 finalizado!");
        }

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

        service.update(parking);

        return EmailMessages.createTerminationMessage(
                parking.getPayment().getPrice().setScale(2, RoundingMode.HALF_EVEN),
                parking.getDateTimeStart(),
                parking.getDateTimeEnd(),
                parking.getPriceTable().getValue().setScale(2, RoundingMode.HALF_EVEN),
                parking.getPriceTable().getTypeCharge());
    }

    private BigDecimal calculateTotalValue(LocalDateTime start, LocalDateTime end, BigDecimal value) {
        long seconds = TimeUtils.getDurationBetweenInSeconds(start, end);

        BigDecimal totalHours = new BigDecimal(seconds)
                .divide(new BigDecimal(3600), RoundingMode.CEILING);

        return value.multiply(totalHours);
    }
}
