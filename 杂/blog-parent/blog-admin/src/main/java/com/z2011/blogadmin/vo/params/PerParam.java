package com.z2011.blogadmin.vo.params;

import lombok.Data;

/**
 * currentPage: 1
 * pageSize: 10
 * queryString: null
 * total: 0
 */
@Data
public class PerParam {
    private Integer currentPage;
    private Integer pageSize;
    private String queryString;
    private Integer total;
}