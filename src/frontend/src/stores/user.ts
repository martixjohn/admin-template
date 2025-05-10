import { defineStore } from "pinia";
import { ref } from "vue";
import { storage, type UserInfoInStorage } from "@/storage";
import { login as requestLogin } from "@/api/login.ts";
import { geCurrentUser } from "@/api/user.ts";

const emptyState: UserInfoInStorage = {
  username: "",
  nickname: "",
  email: "",
  avatar: "",
  lastLoginTime: new Date(),
  createdTime: new Date(),
  updatedTime: undefined,
  roles: [],
  permissions: [],
  csrfToken: ""
};

export const useUserStore = defineStore("user", () => {
  const state = ref<UserInfoInStorage>({ ...emptyState });

  async function login({ username, password }: { username: string; password: string }) {
    let loginRes = await requestLogin({
      username,
      password,
    });

    if (!loginRes.success) {
      throw loginRes.msg;
    }
    const { csrfToken } = loginRes.data!;
    const res = await geCurrentUser();
    if (!res.success) {
      throw res.msg;
    }

    const info = res.data!;
    const stored = {
      csrfToken,
      ...info,
      createdTime: new Date(info.createdTime),
      updatedTime: info.updatedTime ? new Date(info.updatedTime) : undefined,
      lastLoginTime: info.lastLoginTime ? new Date(info.lastLoginTime) : undefined,
    };
    state.value = stored;
    storage.setUserInfo(stored);
  }

  return {
    state,
    login,
  };
});
