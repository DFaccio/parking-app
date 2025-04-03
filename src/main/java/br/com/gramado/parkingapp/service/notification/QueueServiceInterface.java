package br.com.gramado.parkingapp.service.notification;

public interface QueueServiceInterface {

    void sendMessageToQueue(String bodyMessage, String queueName);
}
