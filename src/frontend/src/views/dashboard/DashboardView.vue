<script setup lang="ts">
import AppNavigation from "@/views/dashboard/nav/AppNavigation.vue";
import { onMounted } from "vue";
import { isLogin } from "@/request/api/user.ts";
import AppHeader from "./header/AppHeader.vue";

onMounted(async () => {
  console.log("Component mounted.");
});
</script>

<template>
  <el-container style="width: 100vw; height: 100vh">
    <!--    菜单-->
    <el-aside width="200px">
      <AppNavigation />
    </el-aside>
    <el-container>
      <!--      头部-->
      <el-header>
        <AppHeader />
      </el-header>
      <!--      主体内容-->
      <el-main>
        <br />
        <router-view v-slot="{ Component }">
          <Transition name="app-dashboard-route-view">
            <component :is="Component" />
          </Transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss">
.app-dashboard-route-view {
  &-enter-active {
    transition: all 0.5s ease;
  }

  &-enter-from {
    opacity: 0;
    transform: translateY(-10px);
  }

  &-leave-to {
    display: none;
  }
  &-leave-active {
    display: none;
  }
}
</style>
