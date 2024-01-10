package br.com.gramado.parkingapp.command.pricetable;

import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.util.converter.PriceTableConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class InsertPriceTableCommand {

    @Resource
    private PriceTableServiceInterface service;

    @Resource
    private PriceTableConverter converter;

    public PriceTableDto execute(PriceTableDto dto) throws ValidationsException {
        validateTypeCharge(dto.getTypeCharge(), dto.getDuration());

        PriceTable priceTable = converter.convert(dto);

        correctDuration(priceTable);

        priceTable = service.insert(priceTable);

        return converter.convert(priceTable);
    }

    private void validateTypeCharge(TypeCharge typeCharge, String duration) throws ValidationsException {
        if (TypeCharge.FIXED.equals(typeCharge) && (duration == null || duration.trim().isEmpty())) {
            throw new ValidationsException("Para per\u00EDodos fixos a dura\u00E7\u00E3o \u00E9 obrigat\u00F3ria!");
        }
    }

    private void correctDuration(PriceTable priceTable) {
        if (TypeCharge.HOUR.equals(priceTable.getTypeCharge())) {
            priceTable.setDuration(LocalTime.of(1, 0, 0));
        }
    }
}
