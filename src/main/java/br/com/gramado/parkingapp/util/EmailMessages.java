package br.com.gramado.parkingapp.util;

import br.com.gramado.parkingapp.util.enums.TypeCharge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface EmailMessages {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    static String createTerminationMessage(BigDecimal price, LocalDateTime start, LocalDateTime end, BigDecimal value, TypeCharge typeCharge) {
        int duration = TimeUtils.getDurationInHoursRoundedUp(start, end).intValue();

        StringBuilder message = new StringBuilder()
                .append("------- Estacionamento Encerrado -------")
                .append("\nValor Total: R$ ")
                .append(price);

        if (TypeCharge.HOUR.equals(typeCharge))
            message.append("\nValor por hora: R$ ");
        else
            message.append("\nValor por período: R$ ");

        message.append(value)
                .append("\nInício: ")
                .append(formatter.format(start))
                .append("\nFim: ")
                .append(formatter.format(end))
                .append("\nDuração: ")
                .append(duration < 9 ? "0" + duration : Integer.toString(duration))
                .append(":00.");

        return message.toString();
    }

    static String createHourlyBillingMessageWarn() {
        return """
                Dentro de 10 minutos, o período de estacionamento será estendido em 1 hora.
                        
                Caso não deseje a adição, encerre o uso do estacionamento!
                """;
    }

    static String createHourlyBillingMessage(BigDecimal price) {
        return "Período de estacionamento acrescido de 1 hora." +
               "Total a ser pago R$ " +
               price +
               ".";
    }

    static String createFixedWarnMessage() {
        return """
                Dentro de 5 minutos, o seu período de estacionamento chegará ao final.
                """;
    }

    static String parkingStarted(TypeCharge typeCharge, LocalDateTime dateTimeEnd) {
        StringBuilder message = new StringBuilder("Seu ticket de estacionamento foi iniciado.\n");

        if (TypeCharge.FIXED.equals(typeCharge)) {
            message.append("O período será encerrado em ")
                    .append(formatter.format(dateTimeEnd))
                    .append(".\n");
        } else {
            message.append("Seu período é atualizado a cada hora.\n");
        }

        return message.toString();
    }
}
