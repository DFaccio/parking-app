package br.com.gramado.parkingapp.command.pricetable;

import br.com.gramado.parkingapp.dto.pricetable.PriceTableDto;
import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.service.pricetable.PriceTableServiceInterface;
import br.com.gramado.parkingapp.util.converter.PriceTableConverter;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FindPriceTableByParamsCommand {

    @Resource
    private PriceTableServiceInterface service;

    @Resource
    private PriceTableConverter converter;

    public PagedResponse<PriceTableDto> execute(Integer id, String name, TypeCharge typeCharge, boolean active, Pagination pagination) {
        Page<PriceTable> page = service.findByParams(id, name, typeCharge, active,pagination);

        return converter.convertEntities(page);
    }
}
