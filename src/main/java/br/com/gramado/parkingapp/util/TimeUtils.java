package br.com.gramado.parkingapp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface TimeUtils {

    static LocalDateTime getTime() {
        return LocalDateTime.now()
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toLocalDateTime();
    }

    static LocalDateTime addDurationInTime(LocalDateTime timeToStart, LocalTime duration) {
        return timeToStart.plus(Duration.between(LocalTime.MIN, duration));
    }

    static BigDecimal getDurationInHoursRoundedUp(LocalDateTime start, LocalDateTime end) {
        long seconds = Duration.between(start, end).toSeconds();

        return new BigDecimal(seconds)
                .divide(new BigDecimal(3600), 0, RoundingMode.CEILING);
    }

    static LocalTime convertStringIntoTime(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            return null;
        }

        return LocalTime.parse(duration, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    static long durationBetweenDate(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toSeconds();
    }
}
