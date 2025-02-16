package br.com.gramado.parkingapp.util;

import br.com.gramado.parkingapp.util.enums.TypeCharge;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface EmailMessages {

    static String createTerminationMessage(BigDecimal price, LocalDateTime start, LocalDateTime end, BigDecimal value, TypeCharge typeCharge) {
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
                .append(start)
                .append("\nFim: ")
                .append(end)
                .append("\nDuração: ")
                .append(TimeUtils.durationBetweenDates(start, end));

        return message.toString();
    }

    static String createHourlyBillingMessageWarn() {
        return """
                Dentro de 10 minutos, o período de estacionamento será estendido em 1 hora.
                        
                Caso não deseje a adição, encerre o uso do estacionamento!
                """;
    }

    static String createHourlyBillingMessage(BigDecimal price) {
        return new StringBuilder()
                .append("Período de estacionamento acrescido de 1 hora.")
                .append("No momento, a cobrança está no valor de R$ ")
                .append(price)
                .append(".")
                .toString();
    }

    static String createFixedWarnMessage() {
        return """
                 Dentro de 5 minutos, o seu período de estacionamento chegará ao final.
                """;
    }

    static String parkingStarted(TypeCharge typeCharge, LocalDateTime dateTimeEnd) {
        StringBuilder message = new StringBuilder("Seu ticket de estacionamento foi iniciado.");

        if (TypeCharge.FIXED.equals(typeCharge)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            message.append("\nO período será encerrado às ").append(dateTimeEnd.format(formatter));
        } else {
            message.append("\nSeu período é atualizado a cada hora.");
        }

        return message.toString();
    }
}
