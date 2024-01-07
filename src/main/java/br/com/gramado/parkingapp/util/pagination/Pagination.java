package br.com.gramado.parkingapp.util.pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    @Schema(example = "0")
    private Integer page;

    @Schema(example = "10")
    private Integer pageSize;

    @Schema(example = "1")
    private Integer totalPages;

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private static final Integer DEFAULT_INITIAL_PAGE = 0;

    public Pagination(Integer page, Integer pageSize) {
        setPage(page);
        setPageSize(pageSize);
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }

        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_INITIAL_PAGE;
        }

        return page;
    }

    public void setPage(Integer page) {
        if (page == null) {
            this.page = DEFAULT_INITIAL_PAGE;
        } else {
            this.page = page;
        }
    }
}