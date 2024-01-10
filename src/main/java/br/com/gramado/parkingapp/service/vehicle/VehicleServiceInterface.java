package br.com.gramado.parkingapp.service.vehicle;

import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface VehicleServiceInterface {

    Page<Vehicle> findAll(Pagination pagination);

    Vehicle insert(Vehicle vehicle);

    Vehicle update(Vehicle vehicle);

    Optional<Vehicle> findByPersonDocumentAndPlate(String personId, String plate);

    Optional<Vehicle> findById(Integer vehicleId);
}
