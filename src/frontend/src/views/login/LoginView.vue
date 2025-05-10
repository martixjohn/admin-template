<script setup lang="ts">
import { ref } from "vue";
import { useUserStore } from "@/stores/user.ts";
import SecurityIcon from "@/components/icons/SecurityIcon.vue";
import { useRouter } from "vue-router";
import { Route } from "@/config/router.ts";
import { errorNotification, successNotification } from "@/utils/messageNotification.ts";

const loginModel = ref({
  username: "",
  password: ""
});

const userStore = useUserStore();

const router = useRouter();

async function onSubmit() {
  await userStore.login({ ...loginModel.value })
    .catch((error) => {
      errorNotification(error);
    });
  successNotification("登录成功！");
  await router.replace({ name: Route.DASHBOARD });
}
</script>

<template>
  <div class="root">
    <div class="login-container">
      <div class="login-icon-container">
        <SecurityIcon />
      </div>
      <div class="login-box-container">
        <div class="login-form-container">
          <div class="login-title" size="large" style="display: flex; justify-content: center">
            登录
          </div>
          <el-form :model="loginModel" label-width="auto">
            <el-form-item label="用户名">
              <el-input v-model="loginModel.username" autocomplete="off" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input type="password" v-model="loginModel.password" show-password />
            </el-form-item>
          </el-form>
<!--          <div style="display: flex; justify-content: center">-->
<!--            <el-checkbox v-model:value="loginModel.remember" label="记住我" />-->
<!--          </div>-->
          <div style="display: flex; justify-content: center">
            <el-button type="primary" @click="onSubmit">登录</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
.root {
  width: 100vw;
  height: 100vh;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #70a6fd;
}

.login-container {
  box-sizing: border-box;
  width: 60vw;
  height: 70vh;
  min-width: 600px;
  box-shadow:
    0 2px 4px 0 rgba(0, 0, 0, 0.2),
    0 2px 10px 0 rgba(0, 0, 0, 0.19);
  border-radius: 1px;
  display: flex;
  background-color: #fff;
}

.login-icon-container {
  flex-grow: 1;
  flex-shrink: 1;
  //overflow: hidden;
  > * {
    width: 100%;
    height: 100%;
  }
}

.login-box-container {
  width: 40vw;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-form-container {
    .login-title {
      padding-bottom: 30px;
      font-size: 2rem;
    }
  }
}
</style>
