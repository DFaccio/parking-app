package br.com.gramado.parkingapp.service.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.repository.ParkingRepository;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class ParkingService implements ParkingServiceInterface {

    private static final String SORT = "id";

    @Resource
    private ParkingRepository repository;

    @Override
    public Parking insert(Parking parking) {
        return repository.save(parking);
    }

    @Override
    public Parking update(Parking parking) {
        return repository.save(parking);
    }

    @Override
    public Optional<Parking> findByPaymentId(Integer id) {
        return repository.findByPaymentId(id);
    }

    @Override
    public List<Parking> findAllByPriceTableId(Integer id) {
        return repository.findParkingByPriceTableId(id);
    }

    @Override
    public Optional<Parking> findById(Integer identifier) {
        return repository.findById(identifier);
    }

    @Override
    public Page<Parking> findAll(Pagination pagination, boolean isFinished) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findParkingByIsFinished(pageable, isFinished);
    }

    @Override
    public List<Parking> findAllByIsFinished(boolean isFinished) {
        return repository.findAllByIsFinished(isFinished);
    }

    @Override
    public List<Parking> findAllByIsFinishedAndPriceTableEqualsFixed(boolean isFinished) {
        return repository.findAllByIsFinishedAndPriceTableTypeChargeEquals(isFinished, TypeCharge.FIXED);
    }

    @Override
    public void update(List<Parking> parkingLots) {
        repository.saveAllAndFlush(parkingLots);
    }
}
