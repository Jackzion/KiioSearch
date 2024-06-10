package com.ziio.kiiosearch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.common.BaseResponse;
import com.ziio.kiiosearch.common.ResultUtils;
import com.ziio.kiiosearch.model.dto.SearchRequest;
import com.ziio.kiiosearch.model.dto.post.PostQueryRequest;
import com.ziio.kiiosearch.model.dto.user.UserQueryRequest;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.model.entity.Video;
import com.ziio.kiiosearch.model.vo.PostVO;
import com.ziio.kiiosearch.model.vo.SearchVO;
import com.ziio.kiiosearch.model.vo.UserVO;
import com.ziio.kiiosearch.service.PictureService;
import com.ziio.kiiosearch.service.PostService;
import com.ziio.kiiosearch.service.UserService;
import com.ziio.kiiosearch.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @Resource
    private VideoService videoService;

    /**
     * 统一搜索请求
     * @param searchRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        // 搜索图片
        Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 30);

        // 搜索视频
        Page<Video> videoPage = videoService.searchVideo(searchText, 1, 7);

        // 搜索用户
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        // 搜索文章
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

        SearchVO searchVO = new SearchVO();
        searchVO.setUserList(userVOPage.getRecords());
        searchVO.setPostList(postVOPage.getRecords());
        searchVO.setPictureList(picturePage.getRecords());
        searchVO.setVideoList(videoPage.getRecords());
        return ResultUtils.success(searchVO);
    }
//    并发查询。。。更慢了
//    @PostMapping("/all")
//    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
//        String searchText = searchRequest.getSearchText();
//
//        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
//            UserQueryRequest userQueryRequest = new UserQueryRequest();
//            userQueryRequest.setUserName(searchText);
//            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
//            return userVOPage;
//        });
//
//        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
//            PostQueryRequest postQueryRequest = new PostQueryRequest();
//            postQueryRequest.setSearchText(searchText);
//            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
//            return postVOPage;
//        });
//
//        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
//            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
//            return picturePage;
//        });
//
//        CompletableFuture.allOf(userTask, postTask, pictureTask).join();
//        try {
//            Page<UserVO> userVOPage = userTask.get();
//            Page<PostVO> postVOPage = postTask.get();
//            Page<Picture> picturePage = pictureTask.get();
//
//            SearchVO searchVO = new SearchVO();
//            searchVO.setUserList(userVOPage.getRecords());
//            searchVO.setPostList(postVOPage.getRecords());
//            searchVO.setPictureList(picturePage.getRecords());
//            return ResultUtils.success(searchVO);
//        } catch (Exception e) {
//            log.error("查询异常", e);
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
//        }
//
//    }

}
