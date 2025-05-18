// 对localStorage封装，方便序列化
class Storage {
  public setItem(key: string, value: any): void {
    if (value === undefined || value === null) value = "";
    else if (typeof value === "object") value = JSON.stringify(value);
    localStorage.setItem(key, value);
  }

  public getItem(key: string): any | null {
    const item = localStorage.getItem(key); // 不可能为undefined
    if (item === null) return item;
    try {
      return JSON.parse(item);
    } catch {
      return item;
    }
  }

  public removeItem(key: string): void {
    localStorage.removeItem(key);
  }
}

const _storage = new Storage();


export interface UserInfoInStorage {
  /** 用户名 */
  username: string;
  /** 昵称 */
  nickname: string;
  /** 电子邮件 */
  email: string;
  /** 头像访问路径 */
  avatar: string;
  /** 角色 */
  roles: string[];
  /** 权限 */
  permissions: string[];
  /** 上次登录时间 */
  lastLoginTime?: string;
  /** 创建时间 */
  createdTime: string;
  /** 更新时间 */
  updatedTime?: string;
  /** CSRF防攻击 */
  token: string;
}

class StorageManager {
  public setUserInfo(info: UserInfoInStorage) {
    _storage.setItem("USER", info);
  }

  public getUserInfo(): UserInfoInStorage | null {
    return _storage.getItem("USER");
  }

  public removeUserInfo() {
    _storage.removeItem("USER");
  }
}

export const storage = new StorageManager();
