package com.ziio.kiiosearch.model.dto;

import com.ziio.kiiosearch.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class SearchRequest extends PageRequest implements Serializable {

    /**
     * 搜索条件
     */
    private String SearchText;



    private static final long serialVersionUID = 1L;

}
