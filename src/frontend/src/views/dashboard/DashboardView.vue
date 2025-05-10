<script setup lang="ts">
import AppNavigation from "@/views/dashboard/nav/AppNavigation.vue";
import { onMounted } from "vue";
import { isLogin } from "@/api/user.ts";



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
      <el-header>Header</el-header>
      <!--      主体内容-->
      <el-main>
        <router-view v-slot="{ Component }">
          <transition>
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>
