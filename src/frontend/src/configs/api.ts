import type { CreateAxiosDefaults } from "axios";

interface APIConfig {
  // 后端API的基础URL
  baseURL: string;
  // axios的配置
  // 参考：https://axios-http.com/docs/req_config
  request: CreateAxiosDefaults;
  // 允许匿名访问的URL，即不携带token的URL，无需baseURL
  permitAllUrls: string[];
}

// 后端请求配置
export const apiConfig: APIConfig = {
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "/api",
  request: {
    // 超时，毫秒
    timeout: 1000 * 10,
    baseURL: import.meta.env.VITE_API_BASE_URL ?? "/api",
  },
  permitAllUrls: ["/login"]
}