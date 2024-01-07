package br.com.gramado.parkingapp.util.pagination;

import br.com.gramado.parkingapp.dto.Dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponse<T extends Dto> {

    private Pagination page;

    private List<T> data;
}
