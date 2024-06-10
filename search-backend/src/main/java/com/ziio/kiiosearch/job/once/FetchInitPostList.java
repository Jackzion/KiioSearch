package com.ziio.kiiosearch.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ziio.kiiosearch.model.entity.Post;
import com.ziio.kiiosearch.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 取消 @Component 注释后, 每次启动 SpringBoot 项目会执行一次 run 方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
        // 发送请求
        String json = "{\"pageSize\":8,\"sortOrder\":\"descend\",\"sortField\":\"priority\",\"tags\":[],\"reviewStatus\":1,\"current\":1}";
        String url = "https://api.code-nav.cn/api/essay/list/page/vo";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        // json 解析 response
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        if (records == null) {
            return;
        }
        // 封装成 PostList
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            if (tempRecord == null) {
                return;
            }
            // 封装成 Post
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
        }
        // 数据库入库
        postService.saveBatch(postList);
    }
}