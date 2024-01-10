package br.com.gramado.parkingapp.service.pricetable;

import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PriceTableServiceInterface {

    PriceTable insert(PriceTable priceTable);

    PriceTable update(PriceTable priceTable);

    Optional<PriceTable> findById(Integer id);

    Page<PriceTable> findByParams(Integer id, String name, TypeCharge typeCharge, boolean active, Pagination pagination);
}
