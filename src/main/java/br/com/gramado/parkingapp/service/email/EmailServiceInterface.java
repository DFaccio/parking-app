package br.com.gramado.parkingapp.service.email;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.util.EmailMessages;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public interface EmailServiceInterface {

    default void sendHourlyAdditionTime(LocalDateTime start, LocalDateTime end, BigDecimal hourPrice, String email) {
        BigDecimal total = hourPrice.multiply(new BigDecimal(TimeUtils.durantionBetweenDates(start, end)))
                .setScale(2, RoundingMode.HALF_EVEN);

        sendEmail(email, "Adição de período", EmailMessages.createHourlyBillingMessage(total));
    }

    default void sendPeriodClose(Parking parking) {
        sendEmail(parking.getVehicle().getPerson().getEmail(),
                "Estacionamento Encerrado",
                EmailMessages.createTerminationMessage(parking.getPayment().getPrice(),
                        parking.getDateTimeStart(),
                        parking.getDateTimeEnd(),
                        parking.getPriceTable().getValue(),
                        parking.getPriceTable().getTypeCharge())
        );
    }

    default void sendHourlyWarnMessage(String email) {
        sendEmail(email, "Acréscimo de 1 hora", EmailMessages.createHourlyBillingMessageWarn());
    }

    default void sendFixedWarnMessage(String email) {
        sendEmail(email, "Período chegando ao fim", EmailMessages.createFixedWarnMessage());
    }

    void sendEmail(String email, String subject, String message);

    default void sendEmailParkingStarted(String email, TypeCharge typeCharge, LocalDateTime dateTimeEnd) {
        sendEmail(email, "Estacionamento Iniciado", EmailMessages.parkingStarted(typeCharge, dateTimeEnd));
    }
}
