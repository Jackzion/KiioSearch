<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <!--    {{ JSON.stringify(postList) }}-->
    <MyDivider />

    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片" force-render>
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="video" tab="视频" force-render>
        <VideoList :video-list="videoList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import VideoList from "@/components/VideoList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import myAxios from "@/plugins/myAxios";

const router = useRouter();
const route = useRoute();
// 获取动态路由的传参 -- category（搜索栏的text）同步到页面
const activeKey = route.params.category;
// 初始化数据
const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);
const videoList = ref([]);
// todo: pageSize ，pageNum 同步到url
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNum: 1,
};
const searchParams = ref(initSearchParams);

/**
 * 统一加载数据 --> 调用后端统一搜索接口
 * @param params
 */
const loadData = (params: any) => {
  const query = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("/search/all", query).then((res: any) => {
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
    videoList.value = res.videoList;
  });
};

loadData(searchParams.value.text);
/**
 * 数据改变的时候， do something
 * @param value
 */
// todo: 有必要吗？ text 直接赋值不也可以进行页面同步
watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});
/**
 * 搜索
 * @param value
 */
const onSearch = (value: string) => {
  alert(value);
  // 搜索将 searchParams --> url
  router.push({
    query: searchParams.value,
  });
  loadData(searchParams.value);
};

// tab 栏变更同步 url
const onTabChange = (key: string) => {
  router.push({
    // tab栏改变 将 searchParams --> url.query , key --> url.path.category
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
