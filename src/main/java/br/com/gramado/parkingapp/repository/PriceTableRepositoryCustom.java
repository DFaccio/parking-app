package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceTableRepositoryCustom {

    Page<PriceTable> findByParams(Integer id, String name, TypeCharge typeCharge, boolean active, Pageable pageable);
}
