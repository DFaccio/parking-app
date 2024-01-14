package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.ParkingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingPaymentRepository extends JpaRepository<ParkingPayment, String> {
    Optional<ParkingPayment> findById(Integer identifier);
}
