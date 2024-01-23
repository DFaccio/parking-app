package br.com.gramado.parkingapp.service.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ParkingServiceInterface {

    Parking insert(Parking parking);

    Optional<Parking> findById(Integer identifier);

    Page<Parking> findAll(Pagination pagination, boolean isFinished);

    Parking update(Parking parking);

    Optional<Parking> findByPaymentId(Integer id);

    List<Parking> findAllByPriceTableId(Integer id);

    List<Parking> findAllByIsFinished(boolean isFinished);

    List<Parking> findAllByIsFinishedAndPriceTableEqualsFixed(boolean isFinished);

    void update(List<Parking> toUpdate);
}