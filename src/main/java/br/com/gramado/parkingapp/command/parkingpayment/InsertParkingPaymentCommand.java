package br.com.gramado.parkingapp.command.parkingpayment;

import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.service.parkingpayment.ParkingPaymentServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingPaymentConverter;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class InsertParkingPaymentCommand {
    @Resource
    private ParkingPaymentServiceInterface parkingPaymentService;
    @Resource
    private ParkingPaymentConverter parkingPaymentConverter;

    public ParkingPaymentDto execute(ParkingPaymentDto parkingPaymentDto) throws ValidationsException {
        ParkingPayment parkingPayment = parkingPaymentConverter.convert(parkingPaymentDto);

        parkingPayment = parkingPaymentService.insert(parkingPayment);

        return parkingPaymentConverter.convert(parkingPayment);
    }
}