package br.com.gramado.parkingapp.command.payment;

import br.com.gramado.parkingapp.dto.payment.PaymentDto;
import br.com.gramado.parkingapp.dto.payment.PaymentUpdateDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.service.parkingpayment.PaymentServiceInterface;
import br.com.gramado.parkingapp.util.TimeUtils;
import br.com.gramado.parkingapp.util.converter.PaymentConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.enums.TypePayment;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdatePaymentCommand {

    @Resource
    private PaymentServiceInterface paymentService;

    @Resource
    private PaymentConverter paymentConverter;

    @Resource
    private ParkingServiceInterface parkingService;

    public PaymentDto execute(PaymentUpdateDto paymentDto) throws ValidationsException, NotFoundException {
        Optional<Payment> savedOptional = paymentService.findById(paymentDto.getId());

        if (savedOptional.isEmpty()) {
            throw new NotFoundException(paymentDto.getId(), "Parking");
        }

        Payment payment = savedOptional.get();

        if (payment.isPaid()) {
            throw new ValidationsException("Pagamento j\u00E1 realizado!");
        }

        payment.setPaid(paymentDto.isPaid());
        payment.setDateTimePayment(TimeUtils.getTime());

        if (!payment.getPaymentType().equals(paymentDto.getTypePayment())) {
            Parking parking = parkingService.findByPaymentId(payment.getId()).get();

            if (paymentDto.getTypePayment().equals(TypePayment.PIX) && parking.getPriceTable().getTypeCharge().equals(TypeCharge.HOUR)) {
                throw new ValidationsException("Pagamento por PIX \u00E9 aceito apenas em per\u00EDodos fixos!");
            }

            payment.setPaymentType(paymentDto.getTypePayment());
        }

        payment = paymentService.update(payment);

        return paymentConverter.convert(payment);
    }
}