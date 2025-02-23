package br.com.gramado.parkingapp.util;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailMessagesTest {

    @Test
    void testCreateTerminationMessageHourly() {
        LocalDateTime start = LocalDateTime.of(2025, 2, 23, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 23, 13, 30);
        BigDecimal price = new BigDecimal("40.00");
        BigDecimal value = new BigDecimal("10.00");

        String expected = """
                ------- Estacionamento Encerrado -------
                Valor Total: R$ 40.00
                Valor por hora: R$ 10.00
                Início: 23-02-2025 10:00
                Fim: 23-02-2025 13:30
                Duração: 04:00.""";

        String result = EmailMessages.createTerminationMessage(price, start, end, value, TypeCharge.HOUR);
        assertEquals(expected, result);
    }

    @Test
    void testCreateTerminationMessageFixed() {
        LocalDateTime start = LocalDateTime.of(2025, 2, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 1, 13, 0);
        BigDecimal price = new BigDecimal("50.00");
        BigDecimal value = new BigDecimal("50.00");

        String expected = """
                ------- Estacionamento Encerrado -------
                Valor Total: R$ 50.00
                Valor por período: R$ 50.00
                Início: 01-02-2025 10:00
                Fim: 01-02-2025 13:00
                Duração: 03:00.""";

        String result = EmailMessages.createTerminationMessage(price, start, end, value, TypeCharge.FIXED);
        assertEquals(expected, result);
    }

    @Test
    void testCreateHourlyBillingMessageWarn() {
        String expected = """
                Dentro de 10 minutos, o período de estacionamento será estendido em 1 hora.

                Caso não deseje a adição, encerre o uso do estacionamento!
                """;
        assertEquals(expected, EmailMessages.createHourlyBillingMessageWarn());
    }

    @Test
    void testCreateHourlyBillingMessage() {
        BigDecimal price = new BigDecimal("20.00");
        String expected = "Período de estacionamento acrescido de 1 hora.Total a ser pago R$ 20.00.";
        assertEquals(expected, EmailMessages.createHourlyBillingMessage(price));
    }

    @Test
    void testCreateFixedWarnMessage() {
        String expected = "Dentro de 5 minutos, o seu período de estacionamento chegará ao final.\n";
        assertEquals(expected, EmailMessages.createFixedWarnMessage());
    }

    @Test
    void testParkingStartedFixed() {
        LocalDateTime dateTimeEnd = LocalDateTime.of(2023, 2, 10, 15, 30);
        String message = EmailMessages.parkingStarted(TypeCharge.FIXED, dateTimeEnd);
        String expectedMessage = """
                Seu ticket de estacionamento foi iniciado.
                O período será encerrado em 10-02-2023 15:30.
                """;
        assertEquals(expectedMessage, message);
    }

    @Test
    void testParkingStartedHourly() {
        String message = EmailMessages.parkingStarted(TypeCharge.HOUR, null);
        String expectedMessage = """
                Seu ticket de estacionamento foi iniciado.
                Seu período é atualizado a cada hora.
                """;
        assertEquals(expectedMessage, message);
    }
}
