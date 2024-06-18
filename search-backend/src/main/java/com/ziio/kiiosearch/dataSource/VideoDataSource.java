package com.ziio.kiiosearch.dataSource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.common.ErrorCode;
import com.ziio.kiiosearch.exception.BusinessException;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.model.entity.Video;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VideoDataSource implements DataSource<Video> {
    @Override
    public Page<Video> doSearch(String searchText, int pageNum, int pageSize) {
        int current = (pageNum - 1) * pageSize;
        // 请求连接
        String url = String.format("https://www.bing.com/videos/search?q=%s&first=%s",searchText , current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        // 根据样式选择元素
        Elements elements = doc.select(".mc_vtvc_link.mc_vtvc_el");
        // 封装成 Video
        List<Video> videos = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址
            String imageUrl = null;
            Elements rmsImgElements = element.select(".rms_img");
            if (rmsImgElements != null && !rmsImgElements.isEmpty()) {
                imageUrl = rmsImgElements.get(0).attr("src");
            } else {
                // 如果 .rms_img 元素不存在，则尝试从 .rms_iac 元素中获取图片地址
                Elements rmsIacElements = element.select(".rms_iac");
                if (rmsIacElements != null && !rmsIacElements.isEmpty()) {
                    imageUrl = rmsIacElements.get(0).attr("src");
                }
            }
            // 取 video 地址
            String originUrl = element.attr("href");
            // 取标题
            String title = element.attr("aria-label");
            // 封装
            Video video = new Video();
            video.setTitle(title);
            video.setOriginUrl(originUrl);
            video.setImageUrl(imageUrl);
            videos.add(video);
            if (videos.size() >= pageSize) {
                break;
            }
        }
        // List 转 Page
        Page<Video> videoPage = new Page<>(pageNum, pageSize);
        videoPage.setRecords(videos);
        return videoPage;
    }
}
