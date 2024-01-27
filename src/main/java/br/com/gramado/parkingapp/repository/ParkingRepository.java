package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.Parking;

import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Integer> {

    Page<Parking> findParkingByIsFinished(Pageable pageable, boolean isFinished);

    Optional<Parking> findByPaymentId(Integer id);

    List<Parking> findParkingByPriceTableId(Integer id);
    List<Parking> findAllByIsFinished(boolean isFinished);

    List<Parking> findAllByIsFinishedAndPriceTableTypeChargeEquals(boolean isFinished, TypeCharge fixed);
}
