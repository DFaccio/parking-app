package br.com.gramado.parkingapp.command.parking;

import br.com.gramado.parkingapp.dto.ParkingDto;
import br.com.gramado.parkingapp.entity.Parking;
import br.com.gramado.parkingapp.service.parking.ParkingServiceInterface;
import br.com.gramado.parkingapp.util.converter.ParkingConverter;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FindAllParkingCommand {
    @Resource
    private ParkingServiceInterface parkingService;

    @Resource
    private ParkingConverter parkingConverter;

    public PagedResponse<ParkingDto> execute(Pagination page) {
        Page<Parking> result = parkingService.findAll(page);

        return parkingConverter.convertEntities(result);
    }
}