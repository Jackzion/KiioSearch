package com.ziio.kiiosearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.model.entity.Picture;

import java.util.List;

public interface PictureService {
    /**
     * 根据关键字分页获取图片
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);

}
