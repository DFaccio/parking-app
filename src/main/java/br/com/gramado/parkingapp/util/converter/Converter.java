package br.com.gramado.parkingapp.util.converter;

import br.com.gramado.parkingapp.dto.Dto;
import br.com.gramado.parkingapp.util.exception.ValidationsException;
import br.com.gramado.parkingapp.util.pagination.PagedResponse;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Converter<T extends Serializable, D extends Dto> {

    D convert(T entity);

    T convert(D dto) throws ValidationsException;

    default PagedResponse<D> convertEntities(Page<T> page) {
        PagedResponse<D> paged = new PagedResponse<>();

        paged.setPage(new Pagination(page.getNumber(), page.getSize(), page.getTotalPages()));

        List<D> dada = page.get().map(this::convert).toList();

        paged.setData(dada);

        return paged;
    }

    default List<D> convertEntity(List<T> entity) {
        if (entity == null) {
            return Collections.emptyList();
        }

        return entity.stream().map(this::convert).toList();
    }

    default List<T> convertDto(List<D> dto) {
        if (dto == null) {
            return Collections.emptyList();
        }

        List<T> entidades = new ArrayList<>();

        dto.forEach(item -> {
            try {
                entidades.add(convert(item));
            } catch (ValidationsException e) {
                throw new RuntimeException(e);
            }
        });

        return entidades;
    }
}
