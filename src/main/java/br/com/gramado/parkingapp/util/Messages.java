package br.com.gramado.parkingapp.util;

import br.com.gramado.parkingapp.util.enums.TypeCharge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Messages {

    static String createTerminationMessage(BigDecimal price, LocalDateTime start, LocalDateTime end, BigDecimal value, TypeCharge typeCharge) {
        StringBuilder message = new StringBuilder()
                .append("------- Estacionamento Encerrado -------")
                .append("\nValor Total: R$ ")
                .append(price);

        if (TypeCharge.HOUR.equals(typeCharge))
            message.append("\nValor por hora R$ ");
        else
            message.append("\nValor por período R$ ");

        message.append(value)
                .append("\nInício: ")
                .append(start)
                .append("\nFim: ")
                .append(end)
                .append("\nDuração: ")
                .append(TimeUtils.getDuration(start, end));

        return message.toString();
    }

    static String createFixedBillingMessage(Integer minutes) {
        return new StringBuilder()
                .append("Seu perído de estacionamento será encerrado em ")
                .append(minutes)
                .append(" minutos!")
                .toString();
    }

    static String createHourlyBillingMessage(Integer minutes, BigDecimal price) {
        return new StringBuilder()
                .append("Dentro de ")
                .append(minutes)
                .append(" minutos seu perído de estacionamento será extendido!\n")
                .append("No momento, a cobrança está no valor de R$ ")
                .append(price)
                .toString();
    }
}
