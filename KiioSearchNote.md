# 聚合搜索平台

# KiioSearch

## 项目介绍

一个聚合搜索平台，**<mark>可以让用户在同一个入口（同一个页面）集中搜索出不同的来源，不同类型的内容</mark>**。

用户：提升用户检索效率，提升用户体验

企业：无需针对每一个项目去开发一个搜索功能，当你有新的内容，新的网站，可以复用同一套搜索系统，提升开发效率。

**技术栈：**

### 前端

- Vue

- Ant Design Vue

- Lodash

### 后端

- Spring Boot

- MySQL

- Elasticsearch 搜索引擎

- 数据抓取(爬虫)

- 数据同步

- - logstash
  
  - Canal

- Guava Retrying

- 怎么保证API的稳定性

## 业务流程

1. 得到各种不同分类的页面

2. 提供一个搜索页面（单一搜索 + 聚合搜索）

3. (优化。。。关键词优化，防抖节流

## 流程图

以前的单一业务搜索：用户要到不同网站获取不同资源，企业也要搭构不同的backend

![](C:\Users\Ziio\AppData\Roaming\marktext\images\2024-05-31-17-27-30-image.png)

NOW KIIO SEARCH 聚合搜索平台：

![](C:\Users\Ziio\AppData\Roaming\marktext\images\2024-05-31-17-31-12-image.png)

### 第一期

1. 项目初始化 （前端，后端）

2. 前端搜索页面

3. 后端搜索接口

#### 项目初始化

前端：脚手架搭构 ： ant design vue --- [快速上手 - Ant Design Vue (antdv.com)](https://www.antdv.com/docs/vue/getting-started-cn)

后端： spring - init template

http://localhost:8101/api/doc.html#/home

#### tag 导航栏 改造 路由页面同步

url <-------> page 

1. page同步到url ： 用户搜索，点tag 操作的时候改变url地址。 path , query 

2. url同步到page ：route.query , route.param 。。

#### 引入 Axios ， 配拦截器。。

#### [拦截器 | Axios中文文档 | Axios中文网 (axios-http.cn)](https://www.axios-http.cn/docs/interceptors)

#### 快速创建 post ，imgae ， user List 通过 props 传参。

# 第二期

1. 获取多种不同类型的数据源
   1. 文章（内部）
   2. 用户（内部）
   3. 图片（外部，不是我们自己的项目、自己的用户产生的数据）
2. 前后端单独的搜索接口联调，跑通整个页面
3. 分析现有项目的问题 => 优化，聚合接口的开发
4. （提前安装 ES）

### 数据源获取(爬虫 ---> post ，imgae )：

- 直接请求数据接口（最方便）HttpClient、OKHttp、RestTemplate、Hutool（[https://hutool.cn）](https://hutool.cn%EF%BC%89)

- 等网页渲染出明文内容后，从前端页面的内容抓取

- 有一些网站可能是动态请求的，他不会一次性加载所有的数据，而是要你点某个按钮、输入某个验证码才会显示出数据。=> 无头浏览器：java：selenium；node.js：puppeteer

#### post 获取 （接口爬虫）：Hutool [https://hutool.cn）](https://hutool.cn%EF%BC%89)

抓取：[编程导航 - 做您编程路上的导航员](https://www.code-nav.cn/learn/passage)

获取到文章后要入库（定时获取或者只获取一次），**离线抓取

#### image 获取（接口爬虫）[jsoup: Java HTML parser, built for HTML editing, cleaning, scraping, and XSS safety](https://jsoup.org/)

抓取：bing image

实时获取，实现接口，每次请求就转发到 bing request ,不在数据库存储

# 第三期

1. 聚合接口优化（支持前端更灵活的数据）
2. 从 0 开始学习 Elastic Stack（Elasticsearch）
3. 学习数据同步，怎么把一个数据库内的数据同步到其他数据库

## 聚合接口优化

怎么样能让前端又能一次搜出所有数据，又能够分别获取某一类数据（比如分页场景）

新增：前端传 type 调用后端同一个接口, 后端根据 type 调用不同的 service 查询
比如：type = user => userService.query

1. 如果 type 为空，那么搜索出所有的数据
2. 如果 type 不为空
   1. 如果 type 合法，那么查出对应数据
   2. 否则报错

优化问题？ --- 后端 if 语句太多 ,

### 门面模式

帮助我们用户（客户端）去更轻松的实现功能，不需要关心门面背后的细节。

聚合搜索类业务基本就是门面模式：即前端不用关心后端从哪里、怎么去取不同来源、怎么去聚合不同来源的数据，更方便的获取到内容。

补充：当调用你系统（接口）的客户端觉得麻烦的时候，你就应该思考，是不是可以抽象一个门面了。

#### 图解

![image20230325120636868](https://typora-1313423481.cos.ap-guangzhou.myqcloud.com/typora/image-20230325120636868.png)

#### 解决：将 搜索 type 的 if 语句 ，提出一层 ，作为门面。

但又会引出新问题 --- 拓展性差 ， 新的 type 会统一添加修改多个模块

### 适配器模式

1. 定制统一的数据源接入规范（标准）：什么数据源允许接入？你的数据源接入时要满足什么要求？要做什么事情？

**任何接入我们系统的数据，它必须要能够根据关键词搜索、并且支持分页搜索**

声明接口来定义规范

2. 假如我们的数据源已经支持了搜索，但是原有的方法参数和我们的规范不一致，怎么办？

适配器模式的作用：通过转换，让两个系统能够完成对接

### 注册器模式（本质也是单例）

提起通过一个 map 或者其他类型存储好后面需要调用的对象。

#### 解决：使用两模式，对 dataSource进行抽象 ， 然后 通过 map注册不同类型的 dataSource 进行注册和使用。。类似于 BeanRegister

## 搜索优化

问题：搜索不够灵活！！！

比如搜“鱼皮rapper” 无法搜到 “鱼皮是rapper”，因为数据库的 like 是包含查询。

需要分词

## Elastic Stack（一套技术栈）

官网：[Elastic — The Search AI Company | Elastic](https://www.elastic.co/cn/)

包含了数据的整合 => 提取 => 存储 => 使用，一整套！

beats：从各种不同类型的文件 / 应用来 采集数据 a,b,c,d,e,aa,bb,cc

Logstash：从多个采集器或数据源抽取 / 转换数据，向 es 输送 aa,bb,cc

elasticsearch：存储、查询数据

kibana：可视化 es 的数据

### 安装 ES

elasticsearch：[Set up Elasticsearch | Elasticsearch Guide [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/setup.html)

kibana：[Kibana—your window into Elastic | Kibana Guide [7.17] | Elastic](https://www.elastic.co/guide/en/kibana/7.17/introduction.html)

**只要是一套技术，所有版本必须一致！！！此处用 7.17

## 第四期

1. 继续讲 ElasticStack 的概念
2. 学习用 Java 来调用 Elasticsearch
3. 使用 ES 来优化聚合搜索接口
4. 已有的 DB 的数据和 ES 数据同步（增量、全量；实时、非实时）
5. jmeter 压力测试
6. 保障接口稳定性
7. 其他的扩展思路

### 分词器

[Test an analyzer | Elasticsearch Guide [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/test-analyzer.html)

### IK 分词器（ES 插件）

中文友好：[GitHub - infinilabs/analysis-ik: 🚌 The IK Analysis plugin integrates Lucene IK analyzer into Elasticsearch and OpenSearch, support customized dictionary.](https://github.com/medcl/elasticsearch-analysis-ik)

下载地址：[https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v7.17.7（注意版本一致）](https://github.com/medcl/elasticsearch-analysis-ik/releases/tag/v7.17.7%EF%BC%88%E6%B3%A8%E6%84%8F%E7%89%88%E6%9C%AC%E4%B8%80%E8%87%B4%EF%BC%89)

思考：怎么样让 ik 按自己的想法分词？

回答：自定义词典

#### 步骤

1. 在 elasticsearch-7.17.9 目录下新建 plugins 目录
2. 在 plugins 目录下新建 ik 目录
3. 将下载的 zip 包解压到 ik 目录下
4. 将 elasticsearch-analysis-ik-7.17.7 目录中的所有内容移到 ik 目录下

### ES 调用方式

1. HTTP Restful 调用
2. kibana 操作（dev tools）
3. 客户端操作（Java）

### Java 操作 ES

3 种：

1. ES 官方的 Java API

[Introduction | Elasticsearch Java API Client [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/introduction.html)

快速开始：[Connecting | Elasticsearch Java API Client [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/connecting.html)

2. ES 以前的官方 Java API，HighLeavelRestClient（已废弃，不建议用）

3. Spring Data Elasticsearch

spring-data 系列：spring 提供的操作数据库的框架

spring-data-redis：操作 redis 的一套方法

spring-data-mongodb：操作 mongodb的一套方法

spring-data-elasticsearch：操作 elasticsearch 的一套方法

官方文档：[Spring Data Elasticsearch - Reference Documentation](https://docs.spring.io/spring-data/elasticsearch/docs/4.4.10/reference/html/)

自定义方法：

用户可以指定接口的方法名称，框架帮你自动生成查询

## ES 建立 Post 表

#### ES Mapping：

id（可以不放到字段设置里）

ES 中，尽量存放需要用户筛选（搜索）的数据

aliases：别名（为了后续方便数据迁移）

字段类型是 text，这个字段是可被分词的、可模糊查询的；而如果是 keyword，只能完全匹配、精确查询。

analyzer（存储时生效的分词器）：用 ik_max_word，拆的更碎、索引更多，更有可能被搜出来

search_analyzer（查询时生效的分词器）：用 ik_smart，更偏向于用户想搜的分词

如果想要让 text 类型的分词字段也支持精确查询，可以创建 keyword 类型的子字段：

```json
"fields": {
      "keyword": {
        "type": "keyword",
        "ignore_above": 256 // 超过字符数则忽略查询
  }
```

建表结构：

```json
PUT post_v1
{
  "aliases": {
    "post": {}
  },
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "content": {
        "type": "text",
        "analyzer": "ik_max_word",
        "search_analyzer": "ik_smart",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "tags": {
        "type": "keyword"
      },
      "userId": {
        "type": "keyword"
      },
      "createTime": {
        "type": "date"
      },
      "updateTime": {
        "type": "date"
      },
      "isDelete": {
        "type": "keyword"
      }
    }
  }
}
```

## Java 使用 spring data API 增删改查

#### 根据 DSL 构造 query

查询 DSL：

参考文档：

[Query and filter context | Elasticsearch Guide [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/query-filter-context.html)

[Boolean query | Elasticsearch Guide [7.17] | Elastic](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/query-dsl-bool-query.html)

```json
GET post/_search
{
  "query": { 
    "bool": { // 组合条件
      "must": [ // 必须都满足
        { "match": { "title":   "ziio"        }}, // match 模糊查询
        { "match": { "content":   "girls band cry "        }}
      ],
      "filter": [ 
        { "term":  { "status": "published" }}, // term 精确查询
        { "range": { "publish_date": { "gte": "2015-01-01" }}} // range 范围查询
      ]
    }
  }
}
```

```json
POST _search
{
  "query": {
    "bool" : { // bool query  组合多个子查询
      "must" : { // 必须条件
        "term" : { "user.id" : "kimchy" } // 精确查询
      },
      "filter": {// 必须条件 ， 但filter 不影响评分
        "term" : { "tags" : "production" } // 精确查询
      },
      "must_not" : {// 必须不包含条件
        "range" : { // 范围查询
          "age" : { "gte" : 10, "lte" : 20 }
        }
      },
      "should" : [ // 应该包含 ， 根据minimum 设置条件最小个数
        { "term" : { "tags" : "env1" } }, // 精确查询
        { "term" : { "tags" : "deployed" } } // 精确查询
      ],
      "minimum_should_match" : 1,
      "boost" : 1.0
    }
  }
}
```

### 转换为 java 代码

```java
    public Page<Post> searchFromEs(PostQueryRequest postQueryRequest) {
        Long id = postQueryRequest.getId();
        Long notId = postQueryRequest.getNotId();
        String searchText = postQueryRequest.getSearchText();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTags();
        List<String> orTagList = postQueryRequest.getOrTags();
        Long userId = postQueryRequest.getUserId();
        // es 起始页为 0
        long current = postQueryRequest.getCurrent() - 1;
        long pageSize = postQueryRequest.getPageSize();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
        if (notId != null) {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任何一个标签即可
        if (CollUtil.isNotEmpty(orTagList)) {
            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
            for (String tag : orTagList) {
                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
            }
            orTagBoolQueryBuilder.minimumShouldMatch(1);
            boolQueryBuilder.filter(orTagBoolQueryBuilder);
        }
        // 按关键词检索
        if (StringUtils.isNotBlank(searchText)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按标题检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(content)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, PostEsDTO.class);
        Page<Post> page = new Page<>();
        page.setTotal(searchHits.getTotalHits());
        List<Post> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
        if (searchHits.hasSearchHits()) {
            List<SearchHit<PostEsDTO>> searchHitList = searchHits.getSearchHits();
            List<Long> postIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
                    .collect(Collectors.toList());
            List<Post> postList = baseMapper.selectBatchIds(postIdList);
            if (postList != null) {
                Map<Long, List<Post>> idPostMap = postList.stream().collect(Collectors.groupingBy(Post::getId));
                postIdList.forEach(postId -> {
                    if (idPostMap.containsKey(postId)) {
                        resourceList.add(idPostMap.get(postId).get(0));
                    } else {
                        // 从 es 清空 db 已物理删除的数据
                        String delete = elasticsearchRestTemplate.delete(String.valueOf(postId), PostEsDTO.class);
                        log.info("delete post {}", delete);
                    }
                });
            }
        }
        page.setRecords(resourceList);
        return page;
    }
```

## Elastic 没有数据 ，mysql 进行数据同步

## 数据同步

一般情况下，如果做查询搜索功能，使用 ES 来模糊搜索，但是数据是存放在数据库 MySQL 里的，所以说我们需要把 MySQL 中的数据和 ES 进行同步，保证数据一致（以 MySQL 为主）。

MySQL => ES （单向）

首次安装完 ES，把 MySQL 数据全量同步到 ES 里，写一个单次脚本

4 种增量方式，全量同步（首次）+ 增量同步（新数据）：

1. 定时任务，比如 1 分钟 1 次，找到 MySQL 中过去几分钟内（至少是定时周期的 2 倍）发生改变的数据，然后更新到 ES。

​ 优点：简单易懂、占用资源少、不用引入第三方中间件

​ 缺点：有时间差

​ 应用场景：数据短时间内不同步影响不大、或者数据几乎不发生修改

2. 双写：写数据的时候，必须也去写 ES；更新删除数据库同理。（事务：建议先保证 MySQL 写成功，如果 ES 写失败了，可以通过定时任务 + 日志 + 告警进行检测和修复（补偿））
3. 用 Logstash 数据同步管道（一般要配合 kafka 消息队列 + beats 采集器）：

### Logstash

1. 下载安装包

官方文档：[Installing Logstash | Logstash Reference [7.17] | Elastic](https://www.elastic.co/guide/en/logstash/7.17/installing-logstash.html)

下载地址：https://artifacts.elastic.co/downloads/logstash/logstash-7.17.9-windows-x86_64.zip

**传输** 和 **处理** 数据的管道

![image20230330213705988](https://typora-1313423481.cos.ap-guangzhou.myqcloud.com/typora/image-20230330213705988.png)

好处：用起来方便，插件多

缺点：成本更大、一般要配合其他组件使用（比如 kafka）

## lostach mysql 同步到 es

[Jdbc input plugin | Logstash Reference [7.17] | Elastic](https://www.elastic.co/guide/en/logstash/7.17/plugins-inputs-jdbc.html)

```nginx
input {
  jdbc {
    jdbc_driver_library => "H:\Desktop\Java\yupi_project\yuso\ElasticStack\logstash-7.17.9\config\mysql-connector-java-8.0.29.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    jdbc_connection_string => "jdbc:mysql://localhost:3306/yu_search"
    jdbc_user => "root"
    jdbc_password => "root"
    statement => "SELECT * from post where updateTime > :sql_last_value and updateTime < now() order by updateTime desc"
    // 动态缓存的字段 updatetime ，下次比较的是此次最后一条数据
    tracking_column => "updatetime"
    tracking_column_type => "timestamp"
    use_column_value => true
    // 预编译
    parameters => { "favorite_artist" => "Beethoven" }
    schedule => "*/5 * * * * *"
    jdbc_default_timezone => "Asia/Shanghai"
  }
}

filter {
    mutate {
        rename => {
            "updatetime" => "updateTime"
            "userid" => "userId"
            "createtime" => "createTime"
            "isdelete" => "isDelete"
        }
        remove_field => ["thumbnum", "favournum"]
    }
}

output {
  stdout { codec => rubydebug }
  elasticsearch {
    hosts => "http://localhost:9200"
    index => "post_v1"
    document_id => "%{id}"
  }
}
```

#### 配置 kibana 可视化看板

1. 创建索引
2. 导入数据
3. 创建索引模式
4. 选择图标、托拉拽
5. 保存
