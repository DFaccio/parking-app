package br.com.gramado.parkingapp.service.parkingpayment;

import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PaymentServiceInterface {

    Payment insert(Payment payment);

    Optional<Payment> findById(Integer identifier);

    Page<Payment> findAll(Pagination pagination);

    Payment update(Payment payment);

}
