import type { CreateAxiosDefaults } from "axios";

// 后端请求配置
export const apiConfig: CreateAxiosDefaults = {
  // 超时，毫秒
  timeout: 1000 * 10,
  baseURL: 'http://localhost/api'
}