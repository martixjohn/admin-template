<script setup lang="ts">
import type { NavMenuItemConfig } from "@/configs/nav.tsx";
import type { ProceedNavMenuItem } from "@/stores/router";
import { useRouter } from "vue-router";

type Props = ProceedNavMenuItem;

const props = defineProps<Props>();

const router = useRouter();

// 根据权限判断是否显示
function canPass(item: NavMenuItemConfig) {
  return true;
  // let route = router.resolve({
  //   name: item.routeName
  // });
  // let passCount = 0;
  // let roles = route.meta.roles;
  // if(roles && roles.length > 0) {

  // } else {
  //   passCount++;
  // }

  // let permissions = route.meta.permissions;
  // if(permissions && permissions.length > 0) {

  // } else {
  //   passCount++;
  // }
  // return passCount === 2;
}

// 按按钮，跳转路由
function onClickMenu() {
  if (props.routeName) router.push({ name: props.routeName });
}
</script>
<template>
  <!-- 有子 -->
  <el-sub-menu v-if="props.children && canPass(props)" :index="props.menuIndex">
    <template #title>
      <el-icon v-if="props.icon">
        <component :is="props.icon" />
      </el-icon>
      <span>{{ props.title }}</span>
    </template>

    <template v-for="child in props.children">
      <NavMenuItem v-bind="child" />
    </template>
  </el-sub-menu>
  <!-- 无子 -->
  <el-menu-item
    v-else-if="!props.children && canPass(props)"
    :index="props.menuIndex"
    @click="onClickMenu"
  >
    <el-icon v-if="props.icon">
      <component :is="props.icon" />
    </el-icon>
    <span>{{ props.title }}</span>
  </el-menu-item>
</template>
<style scoped>
/*  */
</style>
