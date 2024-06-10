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
