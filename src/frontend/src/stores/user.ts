import { defineStore } from "pinia";
import { ref } from "vue";
import { storage, type UserInfoInStorage } from "@/storage";
import { login as requestLogin, logout as requestLogout } from "@/request/api/login.ts";
import { StrUtil } from "@/utils/strUtils";
import { appConfig } from "@/configs/app";

const emptyState = {
  username: "",
  nickname: "",
  email: "",
  avatar: appConfig.defaultUserAvatar,
  lastLoginTime: new Date() as Date | undefined,
  createdTime: new Date() as Date,
  updatedTime: undefined as Date | undefined,
  roles: [] as string[],
  permissions: [] as string[],
  token: "",
};

export const useUserStore = defineStore("user", () => {
  const state = ref({ ...emptyState });

  // 登录
  const login = async ({ username, password }: { username: string; password: string }) => {
    let loginRes = await requestLogin({
      username,
      password,
    });
    const info = loginRes.data!;
    const newState = {
      ...info,
      createdTime: new Date(info.createdTime),
      updatedTime: info.updatedTime ? new Date(info.updatedTime) : undefined,
      lastLoginTime: info.lastLoginTime ? new Date(info.lastLoginTime) : undefined,
    };
    // 默认的头像
    if (StrUtil.isBlank(newState.avatar)) {
      newState.avatar = appConfig.defaultUserAvatar;
    }
    state.value = newState;
    // 存储到本地
    saveToStorage();
  };

  // 登出
  const logout = async () => {
    await requestLogout();
    clear();
  };

  // 从本地存储加载用户信息
  const loadFromStorage = () => {
    const stored = storage.getUserInfo();
    if (stored) {
      state.value = {
        ...stored,
        createdTime: new Date(stored.createdTime),
        updatedTime: stored.updatedTime ? new Date(stored.updatedTime) : undefined,
        lastLoginTime: stored.lastLoginTime ? new Date(stored.lastLoginTime) : undefined,
      };
    }
  };

  // 存储到本地
  const saveToStorage = () => {
    const s = state.value;
    storage.setUserInfo({
      ...s,
      createdTime: s.createdTime.toString(),
      updatedTime: s.updatedTime?.toString(),
      lastLoginTime: s.lastLoginTime?.toString(),
    });
  };

  // 清除用户信息
  const clear = () => {
    state.value = { ...emptyState };
    storage.removeUserInfo();
  }

  // 初始化
  const init = () => {
    loadFromStorage();
  };

  return {
    state,
    login,
    logout,
    clear,
    init
  };
});
