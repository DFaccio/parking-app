package br.com.gramado.parkingapp.command.vehicle;

import br.com.gramado.parkingapp.dto.VehicleDto;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.service.vehicle.VehicleServiceInterface;
import br.com.gramado.parkingapp.util.converter.VehicleConverter;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FindAllVehicleCommand {

    @Resource
    private VehicleServiceInterface service;

    @Resource
    private VehicleConverter converter;

    public PagedResponse<VehicleDto> execute(Pagination page) {
        Page<Vehicle> result = service.findAll(page);

        return converter.convertEntities(result);
    }
}
