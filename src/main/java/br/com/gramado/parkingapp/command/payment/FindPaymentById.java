package br.com.gramado.parkingapp.command.payment;

import br.com.gramado.parkingapp.dto.payment.PaymentDto;
import br.com.gramado.parkingapp.service.parkingpayment.PaymentServiceInterface;
import br.com.gramado.parkingapp.util.converter.PaymentConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindPaymentById {

    @Resource
    private PaymentServiceInterface parkingPaymentService;

    @Resource
    private PaymentConverter paymentConverter;

    public PaymentDto execute(Integer identifier) throws NotFoundException {
        Optional<br.com.gramado.parkingapp.entity.Payment> parkingPayment = parkingPaymentService.findById(identifier);

        if (parkingPayment.isEmpty()) {
            throw new NotFoundException(identifier, "Parking");
        }

        return paymentConverter.convert(parkingPayment.get());
    }

}