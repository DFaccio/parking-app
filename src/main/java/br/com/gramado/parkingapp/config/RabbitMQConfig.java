package br.com.gramado.parkingapp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${queue.name.expired}")
    private String expiredTicketQueue;

    @Bean
    public Queue createExpiredTicketQueue() {
        return QueueBuilder.durable(expiredTicketQueue)
                .build();
    }
}
