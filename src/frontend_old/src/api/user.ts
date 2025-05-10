import { http } from "@/utils/http";
import type { BaseResult, VoidResult } from "@/api/types";

export interface UserResult extends BaseResult<any> {
  data: {
    /** 头像 */
    avatar: string;
    /** 用户名 */
    username: string;
    /** 昵称 */
    nickname: string;
    /** 当前登录用户的角色 */
    roles: Array<string>;
    /** 按钮级别权限 */
    permissions: Array<string>;
    /** `token` */
    // accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    // refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    // expires: Date;
  };
}

export interface LoginResult extends BaseResult<any> {
  data: {
    /* CSRF保护token */
    csrfToken: string;
  };
}



/** 登录 */
export const requestLogin = (data?: object) => {
  return http.request<LoginResult>("post", "/api/login", { data });
};

/**
 * 请求当前用户信息
 */
export const requestMyUserInfo = () => {
  return http.get<UserResult>("/api/user/my-info");
};

/**
 * 登出用户
 */
export const requestLogout = () => {
  return http.request<VoidResult>("post", "/api/logout");
};

// export type RefreshTokenResult = {
//   success: boolean;
//   data: {
//     /** `token` */
//     accessToken: string;
//     /** 用于调用刷新`accessToken`的接口时所需的`token` */
//     refreshToken: string;
//     /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
//     expires: Date;
//   };
// };

// /** 刷新`token` */
// export const refreshTokenApi = (data?: object) => {
//   return http.request<RefreshTokenResult>("post", "/refresh-token", { data });
// };
