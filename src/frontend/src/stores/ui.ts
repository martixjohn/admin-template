import { defineStore } from "pinia";
import { ref } from "vue";

// ui相关
export const useUIStore = defineStore("ui", () => {

  const state = ref({
    loading: false,
  });

  return {
    state
  }
});