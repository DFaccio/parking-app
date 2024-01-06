package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import org.springframework.stereotype.Component;

@Component
public class ParkingPaymentConverter implements Converter<ParkingPayment, ParkingPaymentDto> {
    private ParkingConverter parkingConverter;


    @Override
    public ParkingPaymentDto convert(ParkingPayment entity) {
        ParkingPaymentDto dto = new ParkingPaymentDto();

        dto.setPaid(entity.isPaid());
        dto.setDateTimePayment(entity.getDateTimePayment());
        dto.setPaymentType(entity.getPaymentType());
        dto.setPrice(entity.getPrice());
        dto.setParkingDto(parkingConverter.convert(entity.getParking()));

        return dto;
    }

    @Override
    public ParkingPayment convert(ParkingPaymentDto dto) {
        ParkingPayment entity = new ParkingPayment();

        entity.setPaid(dto.isPaid());
        entity.setDateTimePayment(dto.getDateTimePayment());
        entity.setPaymentType(dto.getPaymentType());
        entity.setPrice(dto.getPrice());
        entity.setParking(parkingConverter.convert(dto.getParkingDto()));

        return entity;
    }
}
