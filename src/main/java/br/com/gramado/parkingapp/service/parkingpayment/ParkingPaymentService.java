package br.com.gramado.parkingapp.service.parkingpayment;

import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.repository.ParkingPaymentRepository;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingPaymentService implements ParkingPaymentServiceInterface {

    private static final String SORT = "id";

    @Resource
    private ParkingPaymentRepository repository;

    @Override
    public ParkingPayment insert(ParkingPayment parkingPayment) {
        return repository.save(parkingPayment);
    }

    @Override
    public Optional<ParkingPayment> findById(Integer identifier) {
        return repository.findById(identifier);
    }

    @Override
    public ParkingPayment update(ParkingPayment parkingPayment) {
        return repository.save(parkingPayment);
    }

    @Override
    public Page<ParkingPayment> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findAll(pageable);
    }

}
