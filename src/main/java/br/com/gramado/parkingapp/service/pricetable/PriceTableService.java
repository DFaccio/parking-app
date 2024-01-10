package br.com.gramado.parkingapp.service.pricetable;

import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.repository.PriceTableRepository;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class PriceTableService implements PriceTableServiceInterface {

    private static final String SORT = "id";

    @Resource
    private PriceTableRepository repository;

    @Override
    public PriceTable insert(PriceTable priceTable) {
        return repository.save(priceTable);
    }

    @Override
    public PriceTable update(PriceTable priceTable) {
        return repository.save(priceTable);
    }

    @Override
    public Optional<PriceTable> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Page<PriceTable> findByParams(Integer id, String name, TypeCharge typeCharge, boolean active, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findByParams(id, name, typeCharge, active,pageable);
    }
}
