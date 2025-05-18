// @ts-ignore
/* eslint-disable */
import { request } from "@/request/request.ts";

/** 返回对应状态码的响应 GET /debug/status */
export async function status(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.statusParams,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>("/debug/status", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
