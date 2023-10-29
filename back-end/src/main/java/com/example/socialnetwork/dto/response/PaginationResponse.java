package com.example.socialnetwork.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> data;
    private Integer currentPage;
    private Integer totalItems;
    private Integer totalPages;
}
