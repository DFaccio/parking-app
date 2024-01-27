package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class PeriodNotificationCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeriodNotificationCommand.class);

    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private EmailServiceInterface emailService;

    private static final Integer TEN_MINUTES_IN_SECONDS = 600;

    private static final Integer HOURS_IN_SECONDS = 3600;

    @Scheduled(fixedRateString = "${time.to.run.notification.schedule}", timeUnit = TimeUnit.MINUTES)
    public void checkParkingTime() {
        LOGGER.info("Iniciando processo de notificação para adição de período ou encerramento");

        List<Parking> parkings = parkingService.findAllByIsFinished(false);

        parkings.forEach(parking -> {
            if (TypeCharge.FIXED.equals(parking.getPriceTable().getTypeCharge())) {
                checkNeedNotifyEndFixedPeriod(
                        parking.getDateTimeEnd(),
                        parking.getVehicle().getPerson().getEmail());
            } else {
                checkNeedNotifyAdditionTime(parking.getDateTimeStart(),
                        parking.getVehicle().getPerson().getEmail(),
                        parking.getPriceTable().getValue());
            }
        });

        LOGGER.info("Processo de notificação finalizado");
    }

    private void checkNeedNotifyAdditionTime(LocalDateTime dateTimeStart, String email, BigDecimal value) {
        long duration = TimeUtils.getDurationBetweenInSeconds(dateTimeStart, TimeUtils.getTime());

        int durationWithoutHours = (int) (HOURS_IN_SECONDS - duration % HOURS_IN_SECONDS);

        if (durationWithoutHours <= TEN_MINUTES_IN_SECONDS) {
            BigDecimal price = value
                    .multiply(new BigDecimal(duration).divide(new BigDecimal(HOURS_IN_SECONDS), RoundingMode.CEILING));

            emailService.sendEmailAdditionTime(durationWithoutHours / 60, price, email);
        }
    }

    private void checkNeedNotifyEndFixedPeriod(LocalDateTime timeEnd, String email) {
        long duration = TimeUtils.getDurationBetweenInSeconds(TimeUtils.getTime(), timeEnd);

        int durationWithoutHours = (int) (duration % HOURS_IN_SECONDS);

        // duration < 0 já passou o tempo de finalização.
        if (duration > 0 && durationWithoutHours <= TEN_MINUTES_IN_SECONDS) {
            emailService.sendEmailEndFixedPeriod(durationWithoutHours / 60, email);
        }
    }
}
