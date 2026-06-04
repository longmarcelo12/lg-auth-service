package com.example.lgauthservice.shared.presentation.repository.impl;

import com.example.lgauthservice.shared.domain.models.response.PaginationResponse;
import com.example.lgauthservice.shared.presentation.mapper.TupleMapper;
import com.example.lgauthservice.shared.presentation.repository.CommonRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@ConditionalOnProperty(
        prefix = "spring.datasource",
        name = "url"
)
public class CommonRepositoryImpl implements CommonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> PaginationResponse<T> getPage(@NotBlank String sql, Map<String, Object> params, Pageable pageable, Class<T> dtoClass) {
        String pagedSql = sql + " LIMIT :limit OFFSET :offset";
        Query dataQuery = entityManager.createNativeQuery(pagedSql, Tuple.class);
        setParams(dataQuery, params);

        dataQuery.setParameter("limit", pageable.getPageSize());
        dataQuery.setParameter("offset", pageable.getOffset());

        @SuppressWarnings("unchecked")
        List<Tuple> tuples = dataQuery.getResultList();

        List<T> data = TupleMapper.mapTuplesToDtos(tuples, dtoClass);

        String countSql = "SELECT COUNT(*) FROM (" + sql + ") x";
        Query countQuery = entityManager.createNativeQuery(countSql);
        setParams(countQuery, params);

        Number total = (Number) countQuery.getSingleResult();

        return new PaginationResponse<>(data, total.longValue());
    }

    private void setParams(Query query, Map<String, Object> params) {
        if (params == null) return;
        params.forEach(query::setParameter);
    }
}
