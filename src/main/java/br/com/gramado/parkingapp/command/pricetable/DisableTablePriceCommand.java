package br.com.gramado.parkingapp.command.pricetable;

import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DisableTablePriceCommand {

    @Resource
    private PriceTableServiceInterface service;

    public void execute(Integer id) throws NotFoundException {
        Optional<PriceTable> tableSaved = service.findById(id);

        if (tableSaved.isEmpty()) {
            throw new NotFoundException(id, "Price Table");
        }

        PriceTable table = tableSaved.get();
        table.setActive(false);

        service.update(table);
    }
}
