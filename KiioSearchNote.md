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
