package br.com.gramado.parkingapp.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {

    @Value("${queue.wait.for.expiration}")
    private String expirationQueue;

    @Value("${exchange.wait.for.expiration}")
    private String deadLetterExchange;

    @Value("${queue.time.to.notify}")
    private String notificationQueue;

    private static final String ROUTING_KEY = "notify";

    @Bean
    public Queue createExpirationQueue() {
        return QueueBuilder.durable(expirationQueue)
                .deadLetterExchange(deadLetterExchange)
                .deadLetterRoutingKey(ROUTING_KEY)
                .build();
    }

    @Bean
    public Exchange deadLetterExchange(){
        return new DirectExchange(deadLetterExchange,
                true,
                false);
    }

    @Bean
    public Queue createNotificationQueue() {
        return QueueBuilder.durable(notificationQueue)
                .build();
    }

    @Bean
    public Binding createQueueAssociation() {
        return BindingBuilder.bind(createNotificationQueue())
                .to(deadLetterExchange())
                .with(ROUTING_KEY)
                .noargs();
    }
}
