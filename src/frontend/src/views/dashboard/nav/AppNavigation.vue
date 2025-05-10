<script setup lang="ts">
import { navConfig } from "@/config/nav.tsx";
import SubMenu from "@/views/dashboard/nav/SubMenu.vue";
</script>

<template>
  <el-menu style="height: 100%; width: 100%">
    <template v-for="(menu, index) in navConfig">
      <SubMenu v-bind="menu" :index="index + ''"  />
    </template>
  </el-menu>
</template>

<style scoped></style>
