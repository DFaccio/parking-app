package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Optional<Vehicle> findVehicleByPlateEqualsAndPerson_Document(String plate, String document);
}
