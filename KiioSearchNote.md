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
