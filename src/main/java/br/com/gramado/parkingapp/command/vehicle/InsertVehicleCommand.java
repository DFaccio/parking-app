package br.com.gramado.parkingapp.command.vehicle;

import br.com.gramado.parkingapp.dto.VehicleDto;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.converter.VehicleConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InsertVehicleCommand {

    @Resource
    private VehicleServiceInterface service;

    @Resource
    private VehicleConverter converter;

    @Resource
    private PersonServiceInterface personService;

    public VehicleDto execute(VehicleDto vehicleDto) throws ValidationsException {
        Vehicle vehicle = converter.convert(vehicleDto);

        verifyVehiculeAlreadySaved(vehicle.getPlate(), vehicle.getPerson().getId());

        vehicle = service.insert(vehicle);

        return converter.convert(vehicle);
    }

    private void verifyVehiculeAlreadySaved(String plate, String personId) throws ValidationsException {
        Optional<Vehicle> optional = service.findByPersonIdAndPlate(personId, plate);

        if (optional.isPresent()) {
            throw new ValidationsException("Esta pessoa já possui esse veículo cadastrado");
        }
    }
}
