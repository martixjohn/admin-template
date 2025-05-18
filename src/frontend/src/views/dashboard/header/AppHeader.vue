<script setup lang="ts">
import { appConfig } from "@/configs/app";
import { useUserStore } from "@/stores/user";
import cssConfig from "./config.module.scss";
import { useRouter } from "vue-router";
import { Route } from "@/configs/router";
import { ArrowRight } from "@element-plus/icons-vue";
import { useRouterStore } from "@/stores/router";
import { computed } from "vue";
import { successNotification } from "@/utils/messageNotification";
console.log("css", cssConfig);
const title = appConfig.dashboardTitle;

const user = useUserStore();
const router = useRouter();
const routerStore = useRouterStore();

// 点击退出登录
const onClickLogout = async () => {
  await user.logout();
  successNotification("退出登录成功");
  await router.replace({ name: Route.LOGIN });
};

const breadcrumb = computed(
  () => routerStore.routeMap.get(router.currentRoute.value.name?.toString()!)?.seqNavs ?? [],
);
</script>

<template>
  <header style="display: flex">
    <div style="display: flex; align-items: center">
      <div class="app-icon"></div>
      <div class="app-title">{{ title }}</div>
    </div>
    <div style="flex: 1; display: flex; justify-content: right; align-items: center">
      <el-dropdown>
        <!-- <el-button type="text"> -->
          <el-avatar :size="parseInt(cssConfig.appHeaderHeight) - 10" :src="user.state.avatar" />
        <!-- </el-button> -->
        <template #dropdown>
          <el-dropdown-item>
            <span>个人中心</span>
          </el-dropdown-item>
          <!-- <el-dropdown-item>
            <span>设置</span>
          </el-dropdown-item> -->
          <el-dropdown-item divided @click="onClickLogout">
            <span>退出登录</span>
          </el-dropdown-item>
        </template>
      </el-dropdown>
    </div>
  </header>
  <!-- 面包屑菜单 -->
  <div>
    <div style="height: 10px"></div>
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item
        v-for="item of breadcrumb"
        :to="item.routeName ? { name: item.routeName } : undefined"
        >{{ item.title }}</el-breadcrumb-item
      >
    </el-breadcrumb>
  </div>
</template>

<style scoped lang="scss">
@use "config.module" as config;

header {
  border-bottom: 1px solid #dcdfe6;
}

.app-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: #{config.$app-header-height}px;
  height: #{config.$app-header-height}px;
  background-color: #d1d1d1;
  margin-right: 1rem;
}

.app-icon::before {
  content: "ICON";
}
</style>
