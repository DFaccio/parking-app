package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindParkingById {

    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private ParkingConverter parkingConverter;

    public ParkingDto execute(Integer identifier) throws NotFoundException {
        Optional<Parking> parking = parkingService.findById(identifier);

        if (parking.isEmpty()) {
            throw new NotFoundException(identifier, "Parking");
        }

        return parkingConverter.convert(parking.get());
    }

}