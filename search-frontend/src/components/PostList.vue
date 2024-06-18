<template>
  <a-list item-layout="vertical" size="large" :data-source="props.postList">
    <template #renderItem="{ item }">
      <a-list-item key="item.title">
        <template #extra>
          <img
            width="272"
            alt="logo"
            src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
          />
        </template>
        <a-list-item-meta :description="item.tags">
          <template #title>
            <a :href="item.href">{{ item.title }}</a>
          </template>
          <template #avatar><a-avatar :src="item.avatar" /></template>
        </a-list-item-meta>
        {{ item.content }}
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import link from "../assets/logo.png";
import { withDefaults, defineProps } from "vue";

interface Props {
  postList: any[];
}

// props 接收父组件传参
const props = withDefaults(defineProps<Props>(), {
  postList: () => [],
});
</script>

<style scoped>
.link {
  width: 200px;
}
</style>
