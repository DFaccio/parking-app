package br.com.gramado.parkingapp.service.parkingpayment;

import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.repository.PaymentRepository;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class PaymentService implements PaymentServiceInterface {

    private static final String SORT = "id";

    @Resource
    private PaymentRepository repository;

    @Override
    public Payment insert(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Integer identifier) {
        return repository.findById(identifier);
    }

    @Override
    public Payment update(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public Page<Payment> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findAll(pageable);
    }
}
