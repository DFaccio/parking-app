package br.com.gramado.parkingapp.service.parking;

import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.repository.ParkingRepository;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingService implements ParkingServiceInterface{

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
    public Optional<Parking> findById(Integer identifier) {
        return repository.findById(identifier);
    }

    @Override
    public Page<Parking> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findAll(pageable);
    }
}
