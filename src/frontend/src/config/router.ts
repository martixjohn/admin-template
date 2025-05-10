/**
 * 配置路由，会关联到菜单显示
 * @description 会被store: router进行初始化
 */

import { Role } from "@/config/auth.ts";
import type {RouteRecordRaw} from "vue-router";

declare module 'vue-router' {
  interface RouteMeta {
    // 标识路由需要的角色，没有或空标识完全放行
    roles?: string[];
    // 标识路由需要的权限，没有或空标识完全放行
    permissions?: string[];
  }
}

/**
 * 路由名称枚举
 * @description 对应route name和route path
 */
export const enum Route {
  // 登录面板
  LOGIN = "/login",

  // 管理面板
  DASHBOARD = "/dashboard",

  // 用户管理
  USER_MANAGEMENT = "/system/user-management",

  // 找不到
  NOT_FOUND = "/not-found",
}

// 路由配置
export const routerConfig: RouteRecordRaw[] = [
  {
    name: Route.DASHBOARD,
    path: Route.DASHBOARD,
    meta: {
      roles: [Role.SUPER_ADMIN, Role.ADMIN]
    },
    component: () => import("@/views/dashboard/DashboardView.vue"),
    children: [
      {
        name: Route.USER_MANAGEMENT,
        path: Route.USER_MANAGEMENT,
        meta: {
          roles: [Role.SUPER_ADMIN, Role.ADMIN]
        },
        component: () => import("@/views/dashboard/main/system/user/UserManagementView.vue"),
      },
      {
        name: Route.NOT_FOUND,
        path: Route.NOT_FOUND,
        component: () => import("@/views/dashboard/error/NotFoundView.vue")
      }
    ]
  },
  {
    name: Route.LOGIN,
    path: Route.LOGIN,
    meta: {},
    component: () => import("@/views/login/LoginView.vue")
  },
  {
    path: "/",
    redirect: {
      name: Route.DASHBOARD
    }
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: {
      name: Route.LOGIN
    }
  }
];

