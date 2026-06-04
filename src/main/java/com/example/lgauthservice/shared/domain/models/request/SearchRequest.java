package com.example.lgauthservice.shared.domain.models.request;

import com.example.lgauthservice.shared.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.example.lgauthservice.shared.constants.Constants.*;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor()
@NoArgsConstructor()
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest<T> implements Serializable {
    @Serial
    @JsonIgnore
    private static final long serialVersionUID = 1L;
    @Min(value = 0, message = "Page >= 0")
    @Builder.Default
    private int page = 0;
    @Min(value = 1, message = "Size >= 1")
    @Builder.Default
    private int size = Constants.PAGE_SIZE_DEFAULT;
    @JsonIgnore
    @Builder.Default
    private Instant timestamp = Instant.now();
    private Object orderBy; // client send: ["createdDate", "id", DESC] or "createdDate,desc"
    private T data;

    public Pageable getPageable() {
        return PageRequest.of(page, size, parseSort());
    }

    public Sort parseSort() {
        if (orderBy == null) {
            return Sort.by(Sort.Direction.DESC, "id");
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (orderBy instanceof String str) {
            if (!str.isBlank()) {
                orders.addAll(parseStringSort(str));
            }
        }
        else if (orderBy instanceof List<?> list) {
            orders.addAll(parseListSort(list));
        }

        return orders.isEmpty() ? Sort.by(Sort.Direction.DESC, ID) : Sort.by(orders);
    }

    private List<Sort.Order> parseStringSort(String str) {
        String[] parts = str.split(Constants.COMMA);
        String field = parts[0].trim();
        String dir = parts.length > 1 ? parts[1].trim() : ASC;
        return List.of(createOrder(field, dir));
    }

    private List<Sort.Order> parseListSort(List<?> list) {
        List<Sort.Order> result = new ArrayList<>();
        String direction = ASC;

        for (Object item : list) {
            String value = String.valueOf(item).trim();
            if (value.isBlank()) continue;

            if (ASC.equalsIgnoreCase(value) || DESC.equalsIgnoreCase(value)) {
                direction = DESC.equalsIgnoreCase(value) ? DESC : ASC;
            } else {
                result.add(createOrder(value, direction));
            }
        }
        return result;
    }

    private Sort.Order createOrder(String field, String direction) {
        return DESC.equalsIgnoreCase(direction)
                ? Sort.Order.desc(field)
                : Sort.Order.asc(field);
    }
}