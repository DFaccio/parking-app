package br.com.gramado.parkingapp.command.parkingpayment;

import br.com.gramado.parkingapp.dto.ParkingPaymentDto;
import br.com.gramado.parkingapp.entity.ParkingPayment;
import br.com.gramado.parkingapp.service.parkingpayment.ParkingPaymentServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingPaymentConverter;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FindAllParkingPaymentCommand {
    @Resource
    private ParkingPaymentServiceInterface parkingPaymentService;

    @Resource
    private ParkingPaymentConverter parkingPaymentConverter;

    public PagedResponse<ParkingPaymentDto> execute(Pagination page) {
        Page<ParkingPayment> result = parkingPaymentService.findAll(page);

        return parkingPaymentConverter.convertEntities(result);
    }
}