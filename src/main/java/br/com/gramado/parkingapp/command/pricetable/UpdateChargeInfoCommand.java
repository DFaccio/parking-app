package br.com.gramado.parkingapp.command.pricetable;

import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.dto.pricetable.PriceTableUpdateDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.converter.PriceTableConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UpdateChargeInfoCommand {

    @Resource
    private PriceTableServiceInterface service;

    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private PriceTableConverter converter;

    public PriceTableDto execute(PriceTableUpdateDto priceTableDto) throws NotFoundException, ValidationsException {
        Optional<PriceTable> optional = service.findById(priceTableDto.getId());

        if (optional.isEmpty()) {
            throw new NotFoundException(priceTableDto.getId(), "Price Table");
        }

        PriceTable priceTable = optional.get();

        List<Parking> parkingList = parkingService.findAllByPriceTableId(priceTableDto.getId());

        if (!parkingList.isEmpty()) {
            throw new ValidationsException("Informa\u00E7\u00F5es de cobran\u00E7a n\u00E3o podem ser alteradas se a tabela j\u00E1 est\u00E1 em uso!");
        }

        if (TypeCharge.FIXED.equals(priceTable.getTypeCharge())) {
            priceTable.setDuration(TimeUtils.convertStringIntoTime(priceTableDto.getDuration()));
        } else {
            priceTable.setDuration(TimeUtils.convertStringIntoTime("01:00:00"));
        }

        priceTable.setValue(priceTableDto.getValue());

        priceTable = service.update(priceTable);

        return converter.convert(priceTable);
    }
}
