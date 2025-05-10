import { Route } from "@/config/router.ts";
import { House } from "@element-plus/icons-vue";
import type { VNode } from "vue";

export interface NavMenuItem {
  // 菜单标题
  title: string;
  // 图标
  icon?: VNode;
  // 路由名称
  routeName?: string;
  // 子菜单
  children?: NavMenuItem[];
}

// 导航菜单配置
export const navConfig: NavMenuItem[] = [
  {
    title: "主页",
    routeName: Route.DASHBOARD,
    icon: <House />,
    children: [
      {
        title: "测试2",
        routeName: Route.DASHBOARD,
        icon: <House />,
        children: [
          {
            title: "测试3",
            routeName: Route.DASHBOARD,
            icon: <House />,
          },
        ]
      }
    ]
  },
  {
    title: "系统管理",
    icon: <House />,
    children: [
      {
        title: "用户管理",
        routeName: Route.DASHBOARD,
      }
    ]
  },
];
