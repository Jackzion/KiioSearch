package com.ziio.kiiosearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.model.entity.Video;

public interface VideoService {
    /**
     * 根据关键字分页获取视频
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Video> searchVideo(String searchText, long pageNum, long pageSize);

}
