// @ts-ignore
/* eslint-disable */
import { request } from "@/request.ts";

/** 登录 POST /login */
export async function login(body: API.UserLoginRequest, options?: { [key: string]: any }) {
  return request<API.ApiResponseUserLoginVO>("/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** 登出 GET /logout */
export async function logout(options?: { [key: string]: any }) {
  return request<API.ApiResponseVoid>("/logout", {
    method: "GET",
    ...(options || {}),
  });
}
