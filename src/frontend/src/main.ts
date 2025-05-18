import "@/assets/global.css";
import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
// ElementPlus
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import { appConfig } from "@/configs/app.ts";
import { errorNotification } from "@/utils/messageNotification.ts";
import { AppError } from "./error";
import { Route } from "./configs/router";

document.title = appConfig.title;

const app = createApp(App);

app.use(createPinia());
app.use(router);

// 注册ElementPlus所有的图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(ElementPlus);

app.config.errorHandler = (err: unknown) => {
  if (err instanceof AppError) {
    errorNotification(err.message);
    console.debug("ERROR", err.type, err.detail, err.stack);
    // 未认证
    if (err.type === "unauthenticated") {
      router.replace({ name: Route.LOGIN });
    }
    return;
  } else {
    errorNotification("错误！请联系开发者: " + err);
    console.error("ERROR", err);
  }
};

app.mount("#app");
