declare namespace API {
  type ApiPageResponseUserSafeVO = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: PageVOUserSafeVO;
  };

  type ApiResponseBoolean = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: boolean;
  };

  type ApiResponseUserLoginVO = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: UserLoginVO;
  };

  type ApiResponseUserSafeVO = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: UserSafeVO;
  };

  type ApiResponseVoid = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: Record<string, any>;
  };

  type PageQueryRequest = {
    /** 页大小 */
    pageSize: number;
    /** 当前页数, 1开始 */
    current: number;
    /** 排序字段，如["username", "asc"] */
    orders: string[][];
    /** 查询条件，二元运算，逻辑AND拼接. 如 [["username","like","ali"],["age",">=","20"]] */
    queryCondition?: string[][];
  };

  type PageVOUserSafeVO = {
    /** 数据库中总数 */
    total: number;
    /** 当前页，从1开始 */
    current: number;
    /** 页大小 */
    pageSize: number;
    /** 数据 */
    records: UserSafeVO[];
  };

  type statusParams = {
    status: number;
  };

  type UserAddRequest = {
    /** 用户名 */
    username: string;
    /** 密码 */
    password: string;
    /** 角色 */
    role: string;
  };

  type UserChangePasswordRequest = {
    /** 旧密码 */
    oldPassword: string;
    /** 新密码 */
    newPassword: string;
  };

  type UserLoginRequest = {
    /** 用户名 */
    username: string;
    /** 密码 */
    password: string;
  };

  type UserLoginVO = {
    /** 用户名 */
    username: string;
    /** 昵称 */
    nickname: string;
    /** 电子邮件 */
    email: string;
    /** 头像访问路径 */
    avatar: string;
    /** 上一次登录时间 */
    lastLoginTime?: string;
    /** 用户创建时间 */
    createdTime: string;
    /** 用户数据更新时间 */
    updatedTime?: string;
    /** 角色 */
    roles: string[];
    /** 权限 */
    permissions: string[];
    /** TOKEN */
    token: string;
  };

  type UserRegisterRequest = {
    /** 用户名 */
    username: string;
    /** 密码 */
    password: string;
  };

  type UserSafeVO = {
    /** 用户名 */
    username: string;
    /** 昵称 */
    nickname: string;
    /** 电子邮件 */
    email: string;
    /** 头像访问路径 */
    avatar: string;
    /** 上一次登录时间 */
    lastLoginTime?: string;
    /** 用户创建时间 */
    createdTime: string;
    /** 用户数据更新时间 */
    updatedTime?: string;
    /** 角色 */
    roles: string[];
    /** 权限 */
    permissions: string[];
  };
}
