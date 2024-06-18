package com.ziio.kiiosearch.dataSource;

import com.ziio.kiiosearch.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private VideoDataSource videoDataSource;

    private Map<String, DataSource<T>> typeDataSourcesMap;

    /**
     * 注入依赖之前， 完成初始化
     */
    @PostConstruct
    public void doInit() {
        typeDataSourcesMap = new HashMap() {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            put(SearchTypeEnum.VIDEO.getValue(), videoDataSource);
        }};
    }

    /**
     * 单例模式
     * @param type
     * @return
     */
    public DataSource getDataSourceByType(String type) {
        if (typeDataSourcesMap == null) {
            return null;
        }
        return typeDataSourcesMap.get(type);
    }

}