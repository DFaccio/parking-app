package br.com.gramado.parkingapp.service.tickets;

import br.com.gramado.parkingapp.dto.TicketEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface TicketEventServiceInterface {

    void create(TicketEvent event) throws JsonProcessingException;

    void update(TicketEvent event) throws JsonProcessingException;
}
