<script setup lang="ts">
import { useRouterStore } from "@/stores/router";
import NavMenuItem from "@/views/dashboard/nav/NavMenuItem.vue";
import { onBeforeMount, onMounted, ref, useTemplateRef } from "vue";
import { useRoute } from "vue-router";
const route = useRoute();


const routerStore = useRouterStore();


const handleSelect = (index: string) => {
  // console.log("Selected index:", index);
};


const elMenuRef = useTemplateRef("elMenuRef");

onMounted(() => {
  // console.log("elMenuRef", elMenuRef.value);
  // const { index, parentIndex } = routeIndexMap.get(route.fullPath) ?? { index: "0" };
  let activeIndex = routerStore.routeMap.get(route.fullPath)?.menuIndex;
  if (activeIndex !== undefined) {
    /* @ts-ignore */
    elMenuRef.value!.updateActiveIndex(activeIndex);
  }
});
</script>

<template>
  <el-menu ref="elMenuRef" @select="handleSelect" style="height: 100%; width: 100%">
    <NavMenuItem
      v-for="(menu) in routerStore.menus"
      v-bind="menu"
    />
  </el-menu>
</template>

<style scoped></style>
