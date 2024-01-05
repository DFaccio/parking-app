package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.VehicleDto;
import br.com.gramado.parkingapp.entity.Vehicle;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter implements Converter<Vehicle, VehicleDto> {

    @Resource
    private PersonConverter personConverter;

    @Override
    public VehicleDto convert(Vehicle entity) {
        VehicleDto dto = new VehicleDto();

        dto.setColor(entity.getColor());
        dto.setPerson(personConverter.convert(entity.getPerson()));
        dto.setModel(entity.getModel());
        dto.setIdentifier(entity.getId());
        dto.setPlate(entity.getPlate());

        return dto;
    }

    @Override
    public Vehicle convert(VehicleDto dto) {
        Vehicle entity = new Vehicle();

        entity.setColor(dto.getColor());
        entity.setPerson(personConverter.convert(dto.getPerson()));
        entity.setModel(dto.getModel());
        entity.setId(dto.getIdentifier());
        entity.setPlate(dto.getPlate());

        return entity;
    }
}
