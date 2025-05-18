<script setup lang="ts">
import { request } from "@/request/request.ts";
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import { onBeforeMount } from "vue";
import { RouterView, useRouter } from "vue-router";
import { apiConfig } from "./configs/api";
import { Route } from "./configs/router";
import { AppError } from "./error";
import { isLogin } from "./request/api/user";
import { useRouterStore } from "./stores/router";
import { useUserStore } from "./stores/user";
import { warningNotification } from "./utils/messageNotification";
import { StrUtil } from "./utils/strUtils";

const router = useRouter();
const userStore = useUserStore();
const routerStore = useRouterStore();
// store初始化
onBeforeMount(() => {
  userStore.init();
  routerStore.init();
});

request.interceptors.request.use((config) => {
  console.debug("AXIOS REQUEST", config);
  const isPass = apiConfig.permitAllUrls.includes(config.url!);
  if (isPass) {
    return config;
  }
  let userInfo = userStore.state;
  // console.log("url: %s ,user token: %s", config.url,userInfo.token);

  if (userInfo && !StrUtil.isBlank(userInfo.token)) {
    config.headers["Authorization"] = "Bearer " + userInfo.token;
    console.debug("request token", userInfo.token);
  }
  return config;
});

request.interceptors.response.use(
  (res) => {
    // 状态码：2xx
    console.debug("AXIOS SUCCESS", res);
    let data = res.data;
    const contentType = res.headers["content-type"] as string | undefined;
    if (contentType !== undefined) {
      // JSON
      if (contentType.startsWith("application/json")) {
        console.debug("返回JSON", data);
        if (data === undefined) {
          throw new AppError({
            type: "response-error",
            message: "服务器返回空数据",
          });
        }
        // 正常
        if (data.code === 0) {
          return data;
        }
        // 没有认证
        else if (data.code / 1000 === 401) {
          throw new AppError({
            type: "unauthenticated",
            message: data.msg,
          });
        } else {
          console.warn("返回错误", data);
          throw new AppError({
            type: "response-error",
            message: data.msg,
          });
        }
      }
    }
    // TODO 处理非JSON返回
    console.debug("AXIOS返回非JSON, contentType=%s", contentType);
    throw new AppError({
      type: "response-error",
      message: "不支持的类型",
    });
  },
  (err) => {
    const response = err.response;
    let message = "";
    // 有响应
    if (response !== undefined) {
      message = "服务器返回状态" + response.status + ": " + response.statusText;
    }
    // 无响应
    else {
      message = "无法连接到服务器";
    }
    throw new AppError({
      type: "response-error",
      message,
    });
  },
);

router.beforeEach(async (to, from) => {
  // 跳转到登录页
  if (to.name === Route.LOGIN) {
    return true;
  }

  const { data } = await isLogin().catch(() => ({data: false}));
  if (data) {
    return true;
  } else {
    await router.replace({ name: Route.LOGIN });
    warningNotification("请先登录");
    return false;
  }
});
</script>

<template>
  <ElConfigProvider :locale="zhCn">
    <RouterView v-slot="{ Component }">
      <Transition name="app-route-view">
        <component :is="Component" />
      </Transition>
    </RouterView>
  </ElConfigProvider>
</template>
<style lang="scss">
.app-route-view {
  &-enter-active,
  &-leave-active {
    transition: opacity 0.5s ease;
  }
  &-enter-from,
  &-leave-to {
    opacity: 0;
  }
}
</style>
