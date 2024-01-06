package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InsertParkingCommand {
    @Resource
    private ParkingServiceInterface parkingService;
    @Resource
    private ParkingConverter parkingConverter;

    public ParkingDto execute(ParkingDto parkingDto) throws ValidationsException{
        Parking parking = parkingConverter.convert(parkingDto);

        parking = parkingService.insert(parking);

        return parkingConverter.convert(parking);
    }
}
