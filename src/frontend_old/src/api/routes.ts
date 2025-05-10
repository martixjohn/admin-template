import { http } from "@/utils/http";
import type { BaseResult } from "@/api/types";


export type RouteResult = BaseResult<Array<any>>

export const requestRoutes = () => {
  return http.request<RouteResult>("get", "/api/routes");
};
