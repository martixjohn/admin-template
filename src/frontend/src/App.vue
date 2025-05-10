<script setup lang="ts">
import { RouterView } from "vue-router";
import { ElConfigProvider } from "element-plus";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import { status } from "@/api/debug.ts";
import { request } from "@/request.ts";
import { errorNotification } from "@/utils/messageNotification.ts";

// const { success, data } = await isLogin();
// console.log("isLogin", data);

request.interceptors.response.use(
  (config) => {
    // 状态码：2xx
    console.debug("AXIOS SUCCESS", config);
    let data = config.data;
    if (data) {
      // JSON
      if (data.success === 0) {
        return data;
      } else {
        console.warn("ERROR", data);

      }
    }
    // TODO 处理非JSON返回
  },
  (err) => {
    const response = err.response;
    let message = "";
    // 有响应
    if (response) {
      message = "服务器返回" + response.status + ": " + response.statusText;
    }
    // 无响应
    else {
      message = "无法连接到服务器";
    }
    errorNotification(message);
    return {
      success: false,
      msg: message,
    };
  },
);
try {
  await status({
    status: 500,
  });
  console.log("OK");
} catch (e) {
  console.error("APP", e);
}
</script>

<template>
  <ElConfigProvider :locale="zhCn">
    <RouterView />
  </ElConfigProvider>
</template>
