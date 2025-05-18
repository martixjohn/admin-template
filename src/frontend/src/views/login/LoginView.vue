<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useUserStore } from "@/stores/user.ts";
import SecurityIcon from "@/components/icons/SecurityIcon.vue";
import { useRouter } from "vue-router";
import { Route } from "@/configs/router.ts";
import { errorNotification, successNotification } from "@/utils/messageNotification.ts";
import { storage } from "@/storage";

const loginModel = ref({
  username: "",
  password: "",
});

const userStore = useUserStore();

const router = useRouter();

const loading = ref(false);

async function onSubmit() {
  loading.value = true;
  try {
    await userStore.login({ ...loginModel.value });
    console.debug("登录成功");
    successNotification("登录成功！");
    await router.replace({ name: Route.DASHBOARD });
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  storage.removeUserInfo();
  // console.debug("cleared all user info");
});
</script>

<template>
  <!-- <div class="root"> -->
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
          <el-button :loading="loading" type="primary" @click="onSubmit">登录</el-button>
        </div>
      </div>
    </div>
  </div>
  <!-- </div> -->
</template>

<style lang="scss">
// .root {
// width: 100vw;
// height: 100vh;
// position: relative;
// display: flex;
// justify-content: center;
// align-items: center;
// background-color: #70a6fd;
// }

.login-container {
  box-sizing: border-box;
  // width: 60vw;
  // height: 70vh;
  // min-width: 600px;
  width: 100vw;
  height: 100vh;
  box-shadow:
    0 2px 4px 0 rgba(0, 0, 0, 0.2),
    0 2px 10px 0 rgba(0, 0, 0, 0.19);
  border-radius: 1px;
  display: flex;
  background-color: #fff;
}

.login-icon-container {
  box-sizing: border-box;
  width: 50vw;
  height: 100vh;
  border-right: 1px solid #e4e7ed;
  display: flex;
  justify-content: center;
  align-items: center;
  //overflow: hidden;
  > * {
    // width: 100%;
    height: 60vh;
  }
}

.login-box-container {
  width: 50vw;
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
