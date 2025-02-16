package br.com.gramado.parkingapp.util;

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
        return timeToStart.plusHours(duration.getHour())
                .plusMinutes(duration.getMinute())
                .plusSeconds(duration.getSecond())
                .plusNanos(duration.getNano());
    }

    static long getDurationBetweenInSeconds(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toSeconds();
    }

    static LocalTime convertStringIntoTime(String duration) {
        if (duration == null || duration.trim().isEmpty()) {
            return null;
        }

        return LocalTime.parse(duration, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    static long durationBetweenDates(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours();
    }
}
