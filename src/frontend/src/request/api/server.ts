// @ts-ignore
/* eslint-disable */
import { request } from "@/request/request.ts";

/** 服务器健康检查 GET /system/server/health */
export async function health(options?: { [key: string]: any }) {
  return request<API.ApiResponseVoid>("/system/server/health", {
    method: "GET",
    ...(options || {}),
  });
}
