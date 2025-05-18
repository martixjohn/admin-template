import { createRouter, createWebHistory } from "vue-router";
import DashboardView from "@/views/dashboard/DashboardView.vue";
import { routerConfig } from "@/configs/router.ts";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routerConfig as any
});

router.beforeEach((to, from, next) => {
  next();
});

export default router;
