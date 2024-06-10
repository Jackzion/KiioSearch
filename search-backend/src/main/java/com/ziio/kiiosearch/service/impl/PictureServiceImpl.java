package com.ziio.kiiosearch.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ziio.kiiosearch.common.ErrorCode;
import com.ziio.kiiosearch.exception.BusinessException;
import com.ziio.kiiosearch.model.entity.Picture;
import com.ziio.kiiosearch.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        // 请求连接
        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        // 根据样式选择元素
        Elements elements = doc.select(".iuscp.isv");
        // 封装成 Picture
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
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        // List 转 Page
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }

}
