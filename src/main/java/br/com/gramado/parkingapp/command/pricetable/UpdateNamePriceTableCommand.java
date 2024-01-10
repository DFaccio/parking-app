package br.com.gramado.parkingapp.command.pricetable;

import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.util.converter.PriceTableConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateNamePriceTableCommand {

    @Resource
    private PriceTableServiceInterface service;

    @Resource
    private PriceTableConverter converter;

    public PriceTableDto execute(String name, Integer id) throws NotFoundException {
        Optional<PriceTable> optional = service.findById(id);

        if (optional.isEmpty()) {
            throw new NotFoundException(id, "Price Table");
        }

        PriceTable priceTable = optional.get();
        priceTable.setName(name);

        priceTable = service.update(priceTable);

        return converter.convert(priceTable);
    }
}
