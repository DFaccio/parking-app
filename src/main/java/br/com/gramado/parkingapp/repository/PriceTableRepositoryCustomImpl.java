package br.com.gramado.parkingapp.repository;

import br.com.gramado.parkingapp.entity.PriceTable;
import br.com.gramado.parkingapp.util.enums.TypeCharge;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class PriceTableRepositoryCustomImpl implements PriceTableRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PriceTable> findByParams(Integer id, String name, TypeCharge typeCharge, boolean active, Pageable pageable) {
        List<PriceTable> tables = getByParams(id, name, typeCharge, active,pageable);

        Long total = countGetByParams(id, name, typeCharge, active);

        return new PageImpl<>(tables, pageable, total);
    }

    private Long countGetByParams(Integer id, String name, TypeCharge typeCharge, boolean active) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<PriceTable> root = criteriaQuery.from(PriceTable.class);

        Predicate predicate = getPredicate(id, name, typeCharge, active,criteriaBuilder, root);

        criteriaQuery.where(predicate);

        criteriaQuery.select(criteriaBuilder.count(root));

        Query query = entityManager.createQuery(criteriaQuery);

        return (Long) query.getSingleResult();
    }

    private List<PriceTable> getByParams(Integer id, String name, TypeCharge typeCharge, boolean active, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PriceTable> criteriaQuery = criteriaBuilder.createQuery(PriceTable.class);
        Root<PriceTable> root = criteriaQuery.from(PriceTable.class);

        Predicate predicate = getPredicate(id, name, typeCharge, active, criteriaBuilder, root);

        criteriaQuery.where(predicate);

        criteriaQuery.select(root)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, criteriaBuilder));

        Query query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    private Predicate getPredicate(Integer id, String name, TypeCharge typeCharge, boolean active, CriteriaBuilder criteriaBuilder, Root<PriceTable> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(criteriaBuilder.like(root.get("id").as(String.class), "%" + id + "%"));
        }

        if (name != null && !name.trim().isEmpty()) {
            Expression<String> upperColumn = criteriaBuilder.upper(root.get("name"));
            String nameToSearch = "%" + name.toUpperCase() + "%";

            predicates.add(criteriaBuilder.like(upperColumn, nameToSearch));
        }

        if (typeCharge != null) {
            predicates.add(criteriaBuilder.equal(root.get("typeCharge"), typeCharge));
        }

        predicates.add(criteriaBuilder.equal(root.get("active"), active));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
