package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.util.TimeUtils;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class PriceTableConverter implements Converter<PriceTable, PriceTableDto> {

    @Override
    public PriceTableDto convert(PriceTable entity) {
        PriceTableDto dto = new PriceTableDto();

        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setActive(entity.isActive());
        dto.setTypeCharge(entity.getTypeCharge());
        dto.setName(entity.getName());
        dto.setDuration(entity.getDuration().format(DateTimeFormatter.ISO_LOCAL_TIME));

        return dto;
    }

    @Override
    public PriceTable convert(PriceTableDto dto) {
        PriceTable entity = new PriceTable();

        entity.setId(dto.getId());
        entity.setValue(dto.getValue());
        entity.setActive(dto.isActive());
        entity.setTypeCharge(dto.getTypeCharge());
        entity.setName(dto.getName());
        entity.setDuration(TimeUtils.convertStringIntoTime(dto.getDuration()));

        return entity;
    }
}
