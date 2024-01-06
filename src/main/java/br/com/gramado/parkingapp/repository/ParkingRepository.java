package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, String>{
    Optional<Parking> findById (Integer identifier);
}
