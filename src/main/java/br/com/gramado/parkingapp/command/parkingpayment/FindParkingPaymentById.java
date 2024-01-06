package br.com.gramado.parkingapp.command.parkingpayment;

import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.service.parkingpayment.ParkingPaymentServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingPaymentConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindParkingPaymentById {
    @Resource
    private ParkingPaymentServiceInterface parkingPaymentService;
    @Resource
    private ParkingPaymentConverter parkingPaymentConverter;

    public ParkingPaymentDto execute(Integer identifier) throws NotFoundException {
        Optional<ParkingPayment> parkingPayment = parkingPaymentService.findById(identifier);

        if (parkingPayment.isEmpty()) {
            throw new NotFoundException(identifier, "Parking");
        }

        return parkingPaymentConverter.convert(parkingPayment.get());
    }

}
