package br.com.gramado.parkingapp.service.email;

import br.com.gramado.parkingapp.util.Messages;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface EmailServiceInterface {

    default void sendEmailAdditionTime(Integer minutesLeft, BigDecimal total, String email) {
        total = total.setScale(2, RoundingMode.HALF_EVEN);

        sendEmail(email, "Adição de período", Messages.createHourlyBillingMessage(minutesLeft, total));
    }

    void sendEmail(String email, String subject, String message);

    default void sendEmailEndFixedPeriod(Integer minutesLeft, String email) {
        sendEmail(email, "Fim do perído", Messages.createFixedBillingMessage(minutesLeft));
    }
}
