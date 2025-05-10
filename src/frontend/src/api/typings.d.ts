declare namespace API {
  type ApiResponseBoolean = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: boolean;
    /** 是否成功 */
    success: boolean;
  };

  type ApiResponseUserLoginVO = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: UserLoginVO;
    /** 是否成功 */
    success: boolean;
  };

  type ApiResponseUserSafeVO = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: UserSafeVO;
    /** 是否成功 */
    success: boolean;
  };

  type ApiResponseVoid = {
    /** 错误码 */
    code: number;
    /** 错误信息 */
    msg: string;
    /** 实际数据 */
    data?: Record<string, any>;
    /** 是否成功 */
    success: boolean;
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
    /** CSRF-TOKEN，后续需要放在请求头X-CSRF-TOKEN */
    csrfToken: string;
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
