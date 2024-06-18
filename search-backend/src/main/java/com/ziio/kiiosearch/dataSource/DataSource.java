package com.ziio.kiiosearch.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface DataSource<T> {

    /**
     * 定义 dataSource 接口规则
     */
    Page<T> doSearch(String searchText , int pageNum , int pageSize);
}
