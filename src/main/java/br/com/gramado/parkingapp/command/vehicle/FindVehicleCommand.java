package br.com.gramado.parkingapp.command.vehicle;

import br.com.gramado.parkingapp.dto.VehicleDto;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.converter.VehicleConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindVehicleCommand {

    @Resource
    private VehicleConverter converter;

    @Resource
    private VehicleServiceInterface service;

    public VehicleDto execute(String plate, String person) throws ValidationsException {
        Optional<Vehicle> optional = service.findByPersonIdAndPlate(person, plate);

        if (optional.isEmpty()) {
            throw new ValidationsException("Veículo não encontrado");
        }

        return converter.convert(optional.get());
    }
}
