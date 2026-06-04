package com.example.lgauthservice.shared.presentation.repository;

import com.example.lgauthservice.shared.domain.models.response.PaginationResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommonRepository {

    <T> PaginationResponse<T> getPage(
            String sql,
            Map<String, Object> params,
            Pageable pageable,
            Class<T> dtoClass
    );
}
