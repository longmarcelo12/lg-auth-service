package com.example.lgauthservice.shared.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    public List<T> data;
    public Long total;
}
