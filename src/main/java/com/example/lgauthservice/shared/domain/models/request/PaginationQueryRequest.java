package com.example.lgauthservice.shared.domain.models.request;

import com.example.lgauthservice.shared.constants.Constants;
import com.example.lgauthservice.shared.utils.Utilities;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationQueryRequest {
    @Min(value = 0, message = "Page >= 0")
    private int page = 0;
    @Min(value = 1, message = "limit >= 1")
    private int limit = Constants.PAGE_SIZE_DEFAULT;
    private String keyword;
    private String filter;
    private String sort;

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return (limit <= Constants.MAX_PAGE_SIZE_DEFAULT) ? limit : Constants.PAGE_SIZE_DEFAULT;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getFilter() {
        return filter;
    }

    public String getSort() {
        return sort;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getSkip() {
        return page * getLimit();
    }

    public Map<String, String> getFilterMap() {
        List<FilterQuery> filterQueries = Utilities.convertStringToList(filter, FilterQuery.class);
        if (CollectionUtils.isEmpty(filterQueries)) {
            return new HashMap<>();
        }
        return filterQueries.stream()
                .filter(f -> StringUtils.isNoneBlank(f.getColumn(), f.getText()))
                .collect(Collectors.toMap(
                        FilterQuery::getColumn,
                        FilterQuery::getText,
                        (oldVal, newVal) -> newVal
                ));
    }

    public Sort getSortPageable() {
        List<SortQuery> sortQueries = Utilities.convertStringToList(sort, SortQuery.class);
        sortQueries = sortQueries.stream()
                .filter(sq -> StringUtils.isNoneBlank(sq.getColumn(), sq.getOrder()))
                .toList();
        if (CollectionUtils.isEmpty(sortQueries)) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        List<Sort.Order> orders = new ArrayList<>();
        sortQueries.forEach(o -> {
            Sort.Direction direction = Sort.Direction.valueOf(o.getOrder().toUpperCase());
            orders.add(new Sort.Order(Utilities.getOrDefault(direction, Sort.Direction.DESC), o.getColumn()));
        });
        return Sort.by(orders);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortQuery {
        private String column;
        private String order;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterQuery {
        private String column;
        private String text;
    }
}
