package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.VehicleDto;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
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
        dto.setId(entity.getId());
        dto.setPlate(entity.getPlate());

        return dto;
    }

    @Override
    public Vehicle convert(VehicleDto dto) throws ValidationsException {
        Vehicle entity = new Vehicle();

        entity.setColor(dto.getColor());
        entity.setPerson(personConverter.convert(dto.getPerson()));
        entity.setModel(dto.getModel());
        entity.setId(dto.getId());
        entity.setPlate(dto.getPlate());

        return entity;
    }
}
