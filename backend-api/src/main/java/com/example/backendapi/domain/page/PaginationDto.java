package com.example.backendapi.domain.page;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class PaginationDto<T>{
    private List<T> data;
    private int totalPages;
    private long totalElements;
}
