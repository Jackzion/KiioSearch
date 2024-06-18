package com.ziio.kiiosearch;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ziio.kiiosearch.common.ErrorCode;
import com.ziio.kiiosearch.exception.BusinessException;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.model.entity.Post;
import com.ziio.kiiosearch.model.entity.Video;
import com.ziio.kiiosearch.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    // 请求爬虫测试
    @Test
    void testFetchPassage() {
        // 发送请求
        String json = "{\"pageSize\":10,\"sortOrder\":\"descend\",\"sortField\":\"priority\",\"tags\":[],\"reviewStatus\":1,\"current\":1}";
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

    // pictre 接口爬虫测试
    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        // 连接请求
        String url = "https://www.bing.com/images/search?q=mygo&first=" + current;
        Document doc = Jsoup.connect(url).get();
        // 根据样式选择元素
        Elements elements = doc.select(".iuscp.isv");
        // 保证成 Picture
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址 (murl)
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    @Test
    void testFetchVideo(){
//        String url = String.format("https://www.acfun.cn/search?keyword=MYGO");
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(url).get();
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
//        }
//         配置 ChromeDriver 路径 ， 虚拟浏览器
        System.setProperty("webdriver.edge.driver", "C:\\Users\\Ziio\\OneDrive\\桌面\\new\\edgedriver_win64\\msedgedriver.exe");

        String url = "https://www.acfun.cn/search?keyword=mygo"; // 目标URL
        WebDriver driver = new EdgeDriver();
        driver.get(url);

        // 等待元素加载
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(3, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-video")));

        // 获取页面源码
        String pageSource = driver.getPageSource();
        driver.quit();
        // 使用 Jsoup 解析
        Document doc = Jsoup.parse(pageSource);

        // 根据样式选择元素
        Elements elements = doc.select(".search-video");
        // 封装成 Video
        List<Video> videos = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String imageUrl = element.selectFirst("img").attr("src");
            // 取 video 地址
            String href =  element.selectFirst(".video__cover").selectFirst("a").attr("href");
            String originUrl = "https://www.acfun.cn" + href;
            // 取标题
            String attr = element.attr("data-exposure-log");
            Map<String,Object> map = JSONUtil.toBean(attr, Map.class);
            String title = (String)map.get("title");
            // 封装
            Video video = new Video();
            video.setTitle(title);
            video.setOriginUrl(originUrl);
            video.setImageUrl(imageUrl);
            videos.add(video);
        }
        System.out.println("safsaf");
        return ;
    }

    // todo: 校验码反爬 ： 无头浏览器？
    @Test
    void testFetchVideoByYouKu(){
        String url = String.format("https://so.youku.com/search_video/q_%s?searchfrom=1","mygo");
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        // 配置 ChromeDriver 路径 ， 虚拟浏览器
//        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//
//        String url = "https://www.acfun.cn/search?keyword=mygo"; // 目标URL
//        WebDriver driver = new ChromeDriver();
//        driver.get(url);
//
//        // 等待元素加载
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("search-video")));
//
//        // 获取页面源码
//        String pageSource = driver.getPageSource();
//        driver.quit();
//        // 使用 Jsoup 解析
//        Document doc = Jsoup.parse(pageSource);

        // 根据样式选择元素
        Elements elements = doc.select(".pack-cover_nw6A9");
        // 封装成 Video
        List<Video> videos = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String imageUrl = element.selectFirst("img").attr("src");
            // 取 video 地址
            String href =  element.selectFirst(".video__cover").selectFirst("a").attr("href");
            String originUrl = "https://www.acfun.cn" + href;
            // 取标题
            String attr = element.attr("data-exposure-log");
            Map<String,Object> map = JSONUtil.toBean(attr, Map.class);
            String title = (String)map.get("title");
            // 封装
            Video video = new Video();
            video.setTitle(title);
            video.setOriginUrl(originUrl);
            video.setImageUrl(imageUrl);
            videos.add(video);
        }
        System.out.println("safsaf");
        return ;
    }


}
