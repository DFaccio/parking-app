package br.com.gramado.parkingapp.command.notification;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.email.EmailServiceInterface;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.Messages;
import br.com.gramado.parkingapp.util.TimeUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class FinalizeFixedPeriodCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinalizeFixedPeriodCommand.class);

    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private EmailServiceInterface emailService;

    private static final Integer MINUTES_IN_SECOND_TO_END_PARKING = -300;

    private static final Integer HOURS_IN_SECONDS = 3600;

    @Scheduled(fixedRateString = "${time.to.run.end.parking.fixed.schedule}", timeUnit = TimeUnit.MINUTES)
    public void checksEndFixedPeriod() {
        LOGGER.info("Iniciando processo finalização de período fixo");

        List<Parking> parkings = parkingService.findAllByIsFinishedAndPriceTableEqualsFixed(false);

        List<Parking> toUpdate = new ArrayList<>();

        parkings.forEach(parking -> {
            if (!parking.isFinished() && getDuration(parking.getDateTimeEnd()) < MINUTES_IN_SECOND_TO_END_PARKING) {
                sendNotification(parking);
                parking.setFinished(true);

                toUpdate.add(parking);
            }
        });

        if (!toUpdate.isEmpty()) {
            LOGGER.info("Iniciando processo de atualização em lote");

            parkingService.update(toUpdate);

            LOGGER.info("Processo de atualização em lote finalizado");
        }

        LOGGER.info("Verificação de finalização de perído fixo terminado");
    }

    private void sendNotification(Parking parking) {
        String message = Messages.createTerminationMessage(
                parking.getPayment().getPrice().setScale(2, RoundingMode.HALF_EVEN),
                parking.getDateTimeStart(),
                parking.getDateTimeEnd(),
                parking.getPriceTable().getValue().setScale(2, RoundingMode.HALF_EVEN),
                parking.getPriceTable().getTypeCharge()
        );

        emailService.sendEmail(
                parking.getVehicle().getPerson().getEmail(),
                "Estacionamento Encerrado",
                message
        );
    }

    private int getDuration(LocalDateTime timeEnd) {
        long duration = TimeUtils.getDurationBetweenInSeconds(TimeUtils.getTime(), timeEnd);

        return (int) (duration % HOURS_IN_SECONDS);
    }
}
