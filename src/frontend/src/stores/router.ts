/**
 * 路由相关
 */

import { defineStore } from "pinia";
import { reactive } from "vue";

export const useRouterStore = defineStore("router", () => {
  const state = reactive({});

  /**
   * 初始化路由
   */
  function initRouter() {}

  return {
    state,
    initRouter,
  };
});
