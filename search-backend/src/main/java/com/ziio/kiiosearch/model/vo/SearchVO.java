package com.ziio.kiiosearch.model.vo;

import cn.hutool.json.JSONUtil;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.model.entity.Post;
import com.ziio.kiiosearch.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/Jackzion">ziio.</a>
 * 
 */
@Data
public class SearchVO implements Serializable {

    private List<Picture> pictureList;

    private List<PostVO> postList;

    private List<UserVO> userList;

    private static final long serialVersionUID = 1L;
}
