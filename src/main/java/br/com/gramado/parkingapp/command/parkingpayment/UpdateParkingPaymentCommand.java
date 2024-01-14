package br.com.gramado.parkingapp.command.parkingpayment;

import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.service.parkingpayment.ParkingPaymentServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingPaymentConverter;
import br.com.gramado.parkingapp.util.exception.NotFoundException;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateParkingPaymentCommand {
    @Resource
    private ParkingPaymentServiceInterface parkingPaymentService;
    @Resource
    private ParkingPaymentConverter parkingPaymentConverter;

    public ParkingPaymentDto execute(ParkingPaymentDto parkingPaymentDto) throws ValidationsException, NotFoundException {
        Optional<ParkingPayment> savedOptional = parkingPaymentService.findById(parkingPaymentDto.getId());

        if (savedOptional.isEmpty()) {
            throw new NotFoundException(parkingPaymentDto.getId(), "Parking");
        }

        ParkingPayment toUpdate = parkingPaymentConverter.convert(parkingPaymentDto);
        toUpdate.setId(savedOptional.get().getId());
        toUpdate = parkingPaymentService.update(toUpdate);

        return parkingPaymentConverter.convert(toUpdate);
    }
}