package com.ziio.kiiosearch.model.dto.picture;

import com.ziio.kiiosearch.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/Jackzion">ziio.</a>
 * 
 */
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

    /**
     *
     */
    private String SearchText;



    private static final long serialVersionUID = 1L;
}