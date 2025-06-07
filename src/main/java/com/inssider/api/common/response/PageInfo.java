package com.inssider.api.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageInfo {
    private int page;
    private int limit;
    private Long totalElements;
    private int totalPages;
}

