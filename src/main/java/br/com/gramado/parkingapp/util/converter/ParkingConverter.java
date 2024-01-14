package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ParkingConverter implements Converter<Parking, ParkingDto> {

    @Resource
    private VehicleConverter vehicleConverter;

    @Override
    public ParkingDto convert(Parking entity) {
        ParkingDto dto = new ParkingDto();

        dto.setId(entity.getId());
        dto.setDateTimeStart(entity.getDateTimeStart());
        dto.setDateTimeEnd(entity.getDateTimeEnd());
        dto.setPlate(entity.getPlate());
        dto.setVehicle(vehicleConverter.convert(entity.getVehicule()));

        return dto;
    }

    @Override
    public Parking convert(ParkingDto dto) throws ValidationsException {
        Parking entity = new Parking();

        entity.setId(dto.getId());
        entity.setDateTimeStart(dto.getDateTimeStart());
        entity.setDateTimeEnd(dto.getDateTimeEnd());
        entity.setPlate(dto.getPlate());
        entity.setVehicule(vehicleConverter.convert(dto.getVehicle()));

        return entity;
    }

}
