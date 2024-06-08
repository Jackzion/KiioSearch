<template>
  <!--  <img :src="link" class="link" />-->
  <a-list item-layout="horizontal" :data-source="props.postList">
    <template #renderItem="{ item }">
      <a-list-item>
        <a-list-item-meta :description="item.content">
          <template #title>
            <a href="https://www.antdv.com/">{{ item.title }}</a>
          </template>
          <template #avatar>
            <a-avatar :src="link" />
          </template>
        </a-list-item-meta>
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
