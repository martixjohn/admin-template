import axios, { type AxiosInstance, type AxiosRequestConfig } from "axios";
import { apiConfig } from "@/config/api.ts";
import { storage } from "@/storage";

const axiosInstance = axios.create(apiConfig);

// /**
//  * 响应回调，返回值作为新的结果
//  */
// interface ResponseInterceptor {
//   (config: AxiosResponse): any;
// }
//
// const responseInterceptors: ResponseInterceptor[] = [];
//
// /**
//  * 注册响应回调
//  * @description 可以解决循环依赖的问题
//  * @param callback
//  */
// export function registerResponseInterceptor(callback: ResponseInterceptor) {
//   if (callback) {
//     responseInterceptors.push(callback);
//   }
// }

axiosInstance.interceptors.request.use((config) => {
  let userInfo = storage.getUserInfo();
  if (userInfo && userInfo.csrfToken) {
    config.headers["X-CSRF-Token"] = userInfo.csrfToken;
  }
  return config;
});



interface Request extends AxiosInstance {
  <R = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<R>;

  request<R = any, D = any>(config: AxiosRequestConfig<D>): Promise<R>;

  get<R = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<R>;

  delete<R = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<R>;

  head<R = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<R>;

  options<R = any, D = any>(url: string, config?: AxiosRequestConfig<D>): Promise<R>;

  post<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;

  put<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;

  patch<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;

  postForm<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;

  putForm<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;

  patchForm<R = any, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>): Promise<R>;
}

export const request = axiosInstance as Request;
