package com.ziio.kiiosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.common.BaseResponse;
import com.ziio.kiiosearch.common.ErrorCode;
import com.ziio.kiiosearch.common.ResultUtils;
import com.ziio.kiiosearch.exception.ThrowUtils;
import com.ziio.kiiosearch.model.dto.picture.PictureQueryRequest;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                         HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }

}
