import { defineStore } from "pinia";
import { type UserType } from "../utils";

import { ref } from "vue";
import { requestLogin, requestLogout, requestMyUserInfo } from "@/api/user";
import { removeCsrfToken, setCsrfToken } from "@/utils/auth";

const createEmptyUser = () => {
  return {
    /** 头像 */
    avatar: "",
    /** 用户名 */
    username: "",
    /** 昵称 */
    nickname: "",
    /** 当前登录用户的角色 */
    roles: [] as string[],
    /** 按钮级别权限 */
    permissions: [] as string[]
  };
};

export const useUserStore = defineStore("user", () => {
  const user = ref<UserType>(createEmptyUser());

  const login = async ({
    username,
    password
  }: {
    username: string;
    password: string;
  }) => {
    const result = await requestLogin({
      username,
      password
    });
    console.log("登录结果", result);
    if (!result.success || !result.data.csrfToken) {
      console.error("登录失败", result.code, result.msg);
      throw result.msg;
    }

    // 存储CSRF Token
    setCsrfToken(result.data.csrfToken);
    console.log("存储CSRF Token完成");

    const data = await requestMyUserInfo();
    console.log("请求用户信息", data);
    if (!data.success) {
      console.error("请求用户信息失败", result.code, result.msg);
      throw data.msg;
    }
    user.value = { ...user.value, ...data.data };
    console.log("登录成功");
  };

  const logout = async () => {
    const { success, msg } = await requestLogout();
    if (!success) {
      throw new Error("登出失败! " + msg);
    }
    user.value = createEmptyUser();
    removeCsrfToken();
  };

  return {
    user,
    login,
    logout
  };
});

// 过时用法
// export const useUserStore = defineStore({
//   id: "pure-user",
//   state: (): userType => ({
//     // 头像
//     avatar: storageLocal().getItem<DataInfo<number>>(userKey)?.avatar ?? "",
//     // 用户名
//     username: storageLocal().getItem<DataInfo<number>>(userKey)?.username ?? "",
//     // 昵称
//     nickname: storageLocal().getItem<DataInfo<number>>(userKey)?.nickname ?? "",
//     // 页面级别权限
//     roles: storageLocal().getItem<DataInfo<number>>(userKey)?.roles ?? [],
//     // 按钮级别权限
//     permissions:
//       storageLocal().getItem<DataInfo<number>>(userKey)?.permissions ?? [],
//     // 是否勾选了登录页的免登录
//     isRemembered: false,
//     // 登录页的免登录存储几天，默认7天
//     loginDay: 7
//   }),
//   actions: {
//     /** 存储头像 */
//     SET_AVATAR(avatar: string) {
//       this.avatar = avatar;
//     },
//     /** 存储用户名 */
//     SET_USERNAME(username: string) {
//       this.username = username;
//     },
//     /** 存储昵称 */
//     SET_NICKNAME(nickname: string) {
//       this.nickname = nickname;
//     },
//     /** 存储角色 */
//     SET_ROLES(roles: Array<string>) {
//       this.roles = roles;
//     },
//     /** 存储按钮级别权限 */
//     SET_PERMS(permissions: Array<string>) {
//       this.permissions = permissions;
//     },
//     /** 存储是否勾选了登录页的免登录 */
//     SET_ISREMEMBERED(bool: boolean) {
//       this.isRemembered = bool;
//     },
//     /** 设置登录页的免登录存储几天 */
//     SET_LOGINDAY(value: number) {
//       this.loginDay = Number(value);
//     },
//     /** 登入 */
//     async loginByUsername(data) {
//       return new Promise<UserResult>((resolve, reject) => {
//         getLogin(data)
//           .then(data => {
//             if (data?.success) setToken(data.data);
//             resolve(data);
//           })
//           .catch(error => {
//             reject(error);
//           });
//       });
//     },
//     /** 前端登出（不调用接口） */
//     logOut() {
//       this.username = "";
//       this.roles = [];
//       this.permissions = [];
//       removeToken();
//       useMultiTagsStoreHook().handleTags("equal", [...routerArrays]);
//       resetRouter();
//       router.push("/login");
//     },
//     /** 刷新`token` */
//     async handRefreshToken(data) {
//       return new Promise<RefreshTokenResult>((resolve, reject) => {
//         refreshTokenApi(data)
//           .then(data => {
//             if (data) {
//               setToken(data.data);
//               resolve(data);
//             }
//           })
//           .catch(error => {
//             reject(error);
//           });
//       });
//     }
//   }
// });

// export function useUserStoreHook() {
//   return useUserStore(store);
// }
