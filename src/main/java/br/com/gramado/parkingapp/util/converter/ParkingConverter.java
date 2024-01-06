package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import org.springframework.stereotype.Component;

@Component
public class ParkingConverter implements Converter<Parking, ParkingDto>{

    private VehicleConverter vehicleConverter;
    @Override
    public ParkingDto convert(Parking entity) {
        ParkingDto dto = new ParkingDto();

        dto.setTypeParking(entity.getTypeParking());
        dto.setDateTimeStart(entity.getDateTimeStart());
        dto.setDateTimeEnd(entity.getDateTimeEnd());
        dto.setPlate(entity.getPlate());
        dto.setVehicleDto(vehicleConverter.convert(entity.getVehicule()));

        return dto;
    }

    @Override
    public Parking convert(ParkingDto dto) {
        Parking entity = new Parking();

        entity.setTypeParking(dto.getTypeParking());
        entity.setDateTimeStart(dto.getDateTimeStart());
        entity.setDateTimeEnd(dto.getDateTimeEnd());
        entity.setPlate(dto.getPlate());
        entity.setVehicule(vehicleConverter.convert(dto.getVehicleDto()));

        return entity;
    }

}
