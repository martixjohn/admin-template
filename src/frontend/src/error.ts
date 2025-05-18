type AppErrorType = "response-error" | "unauthenticated" | "other";

// 全局异常
export class AppError extends Error {
  // 提示信息
  message: string;
  // 细节，不展示
  detail: string | undefined;
  // 类型
  type: AppErrorType;
  constructor({
    message,
    type,
    detail,
  }: {
    message: string;
    type: AppErrorType;
    detail?: string;
  }) {
    super(message);
    this.message = message;
    this.name = "AppError";
    this.type = type;
    this.detail = detail;
  }
  
}
