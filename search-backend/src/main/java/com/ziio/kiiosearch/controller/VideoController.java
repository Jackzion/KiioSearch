package com.ziio.kiiosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.common.BaseResponse;
import com.ziio.kiiosearch.common.ErrorCode;
import com.ziio.kiiosearch.common.ResultUtils;
import com.ziio.kiiosearch.exception.ThrowUtils;
import com.ziio.kiiosearch.model.dto.SearchRequest;
import com.ziio.kiiosearch.model.entity.Video;
import com.ziio.kiiosearch.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {

    @Resource
    private VideoService videoService;

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Video>> listVideoByPage(@RequestBody SearchRequest searchRequest,
                                                         HttpServletRequest request) {
        long current = searchRequest.getCurrent();
        long size = searchRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        Page<Video> videoPage = videoService.searchVideo(searchText, current, size);
        return ResultUtils.success(videoPage);
    }

}
