import "@/assets/global.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
// ElementPlus
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { appConfig } from "@/config/app.ts";

document.title = appConfig.title;

const app = createApp(App);

app.use(createPinia());
app.use(router);

// 注册ElementPlus所有的图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(ElementPlus);
app.mount("#app");

