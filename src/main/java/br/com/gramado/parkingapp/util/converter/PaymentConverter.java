package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.payment.PaymentDto;
import br.com.gramado.parkingapp.entity.Payment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.stereotype.Component;

@Component
public class PaymentConverter implements Converter<Payment, PaymentDto> {

    @Override
    public PaymentDto convert(br.com.gramado.parkingapp.entity.Payment entity) {
        PaymentDto dto = new PaymentDto();

        dto.setId(entity.getId());
        dto.setPaid(entity.isPaid());
        dto.setDateTimePayment(entity.getDateTimePayment());
        dto.setPrice(entity.getPrice());
        dto.setTypePayment(entity.getPaymentType());

        return dto;
    }

    @Override
    public br.com.gramado.parkingapp.entity.Payment convert(PaymentDto dto) throws ValidationsException {
        Payment entity = new Payment();

        entity.setId(dto.getId());
        entity.setPaid(dto.isPaid());
        entity.setDateTimePayment(dto.getDateTimePayment());
        entity.setPrice(dto.getPrice());
        entity.setPaymentType(dto.getTypePayment());

        return entity;
    }
}
