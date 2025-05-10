// @ts-ignore
/* eslint-disable */
import { request } from "@/request.ts";

/** 管理员添加用户 POST /user/add */
export async function addUser(body: API.UserAddRequest, options?: { [key: string]: any }) {
  return request<API.ApiResponseVoid>("/user/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 改变密码 PUT /user/change-password */
export async function changePassword(
  body: API.UserChangePasswordRequest,
  options?: { [key: string]: any }
) {
  return request<API.ApiResponseVoid>("/user/change-password", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 检查是否登录 GET /user/is-login */
export async function isLogin(options?: { [key: string]: any }) {
  return request<API.ApiResponseBoolean>("/user/is-login", {
    method: "GET",
    ...(options || {}),
  });
}

/** 获取自己的用户信息 GET /user/my-info */
export async function geCurrentUser(options?: { [key: string]: any }) {
  return request<API.ApiResponseUserSafeVO>("/user/my-info", {
    method: "GET",
    ...(options || {}),
  });
}

/** 用户自行注册 POST /user/register */
export async function register(body: API.UserRegisterRequest, options?: { [key: string]: any }) {
  return request<API.ApiResponseVoid>("/user/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
