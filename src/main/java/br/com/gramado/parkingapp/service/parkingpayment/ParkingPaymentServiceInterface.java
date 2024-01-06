package br.com.gramado.parkingapp.service.parkingpayment;

import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.Optional;
public interface ParkingPaymentServiceInterface {

    ParkingPayment insert(ParkingPayment parkingPayment);

    Optional<ParkingPayment> findById(Integer identifier);

    Page<ParkingPayment> findAll(Pagination pagination);

    ParkingPayment update(ParkingPayment parkingPaymentg);

}
