import { Route } from "@/configs/router.ts";
import { House, Monitor, Operation, User } from "@element-plus/icons-vue";

export interface NavMenuItemConfig {
  // 菜单标题
  title: string;
  // 图标，最终包裹在el-icon内
  icon?: any;
  // 路由名称
  routeName?: string;
  // 子菜单
  children?: NavMenuItemConfig[];
}

// 导航菜单配置
export const navConfig: NavMenuItemConfig[] = [
  {
    title: "仪表盘",
    routeName: Route.DASHBOARD,
    icon: House,
  },
  {
    title: "系统管理",
    icon: Operation,
    children: [
      {
        title: "用户管理",
        routeName: Route.USER_MANAGEMENT,
        icon: User
      },
      {
        title: "运行监控",
        routeName: Route.RUNTIME_MONITOR,
        icon: Monitor
      }
    ]
  },
];
