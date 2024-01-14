package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateParkingCommand {
    @Resource
    private ParkingServiceInterface parkingService;
    @Resource
    private ParkingConverter parkingConverter;

    public ParkingDto execute(ParkingDto parkingDto) throws ValidationsException, NotFoundException {
        // TODO Esse método permite atualizar pessoa e veículo. Não acho que deveria.
        /* Esse tivo de atualização deveria ser feita pelo controller de pessoa e veículo */

        Optional<Parking> savedOptional = parkingService.findById(parkingDto.getId());

        if (savedOptional.isEmpty()) {
            throw new NotFoundException(parkingDto.getId(), "Parking");
        }

        Parking toUpdate = parkingConverter.convert(parkingDto);
        toUpdate.setId(savedOptional.get().getId());
        toUpdate = parkingService.update(toUpdate);

        return parkingConverter.convert(toUpdate);
    }
}