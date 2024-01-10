package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.PriceTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceTableRepository extends JpaRepository<PriceTable, Integer>, PriceTableRepositoryCustom {

}
