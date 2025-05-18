/**
 * 路由相关
 */
import { navConfig, type NavMenuItemConfig } from "@/configs/nav";
import { defineStore } from "pinia";
import { shallowRef } from "vue";

// 处理后的菜单配置
export interface ProceedNavMenuItem {
  // 菜单index el-menu使用
  menuIndex: string;
  // 菜单名称
  title: string;
  // 菜单图标
  icon?: any;
  // 菜单路由
  routeName?: string;
  // 子菜单
  children?: ProceedNavMenuItem[];
}

interface RouteMapValue {
  // 菜单index el-menu使用
  menuIndex?: string;
  // 用于el-breadcrumb渲染
  seqNavs: { title: string; routeName?: string }[];
}


// 路由、菜单相关
export const useRouterStore = defineStore("router", () => {


  const routeMap = shallowRef<Map<string, RouteMapValue>>(new Map());

  const menus = shallowRef<ProceedNavMenuItem[]>();
  /**
   * 初始化导航菜单，执行一次即可
   */
  const init = () => {
    const _routeMap = new Map<string, RouteMapValue>();

    const _menus: ProceedNavMenuItem[] = [];

    // 递归处理菜单
    const walk = (
      item: NavMenuItemConfig,
      menuIndex: string,
      prevSeqNavs: RouteMapValue["seqNavs"],
    ) => {
      const curMenu: ProceedNavMenuItem = { ...item, menuIndex, children: undefined };

      let seqNavs = [
        ...prevSeqNavs,
        {
          title: item.title,
          routeName: item.routeName, // 也许为undefined
        },
      ];
      
      const curRouteMapValue: RouteMapValue = {
        menuIndex,
        seqNavs,
      };

      if (item.routeName) {
        _routeMap.set(item.routeName, curRouteMapValue);
      }

      if (item.children) {
        curMenu.children = item.children.map((child, index) =>
          walk(child, `${menuIndex}-${index}`, seqNavs),
        );
      }

      return curMenu;
    };

    navConfig.forEach((item, index) => {
      _menus.push(walk(item, index + "", []));
    });
    console.log("menus", _menus);

    routeMap.value = _routeMap;
    menus.value = _menus;

  };

  init();

  return {
    menus,
    routeMap,
    init
  };
});
