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
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import myAxios from "@/plugins/myAxios";

// 初始化数据
const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);

myAxios.post("/post/list/page/vo", {}).then((res: any) => {
  console.log(res);
  postList.value = res.records;
});

myAxios.post("/user/list/page/vo", {}).then((res: any) => {
  console.log(res);
  userList.value = res.records;
});
// todo : picture controller
// myAxios.post("/picture/list/page/vo", {}).then((res: any) => {
//   pictureList.value = res.records;
// });

const router = useRouter();
const route = useRoute();
// 获取动态路由的传参 -- category（搜索栏的text）同步到页面
const activeKey = route.params.category;

// todo: pageSize ，pageNum 同步到url
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNum: 1,
};

const searchParams = ref(initSearchParams);

// todo: 有必要吗？ text 直接赋值不也可以进行页面同步
watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});
const onSearch = (value: string) => {
  alert(value);
  // 搜索将 searchParams --> url
  router.push({
    query: searchParams.value,
  });
};

const onTabChange = (key: string) => {
  router.push({
    // tab栏改变 将 searchParams --> url.query , key --> url.path.category
    path: `/${key}`,
    query: searchParams.value,
  });
};
</script>
