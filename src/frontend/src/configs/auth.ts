/**
 * 所有角色
 */
export enum Role {
  // 匿名用户，未登录
  ANONYMOUS = "ROLE_ANONYMOUS",
  // 一般用户
  USER = "ROLE_USER",
  // 管理员
  ADMIN = "ROLE_ADMIN",
  // 超级管理员
  SUPER_ADMIN = "ROLE_SUPER_ADMIN",
}

/**
 * 所有细分权限
 */
export enum Permissions {}

/**
 * 获取角色中文名称
 * @param role 角色
 * @description 获取角色名称
 * @returns 角色名词
 */
export const getRoleName = (role: Role) => {
  switch (role) {
    case Role.ANONYMOUS:
      return "匿名用户";
    case Role.USER:
      return "普通用户";
    case Role.ADMIN:
      return "管理员";
    case Role.SUPER_ADMIN:
      return "超级管理员";
    default:
      return "";
  }
} 