import { ElMessage } from "element-plus";


/**
 * 信息提示
 * @param msg 信息文本
 */
export const infoNotification = (msg: string) =>
  ElMessage({
    showClose: true,
    type: "info",
    message: msg,
  });

/**
 * 成功信息提示
 * @param msg 信息文本
 */
export const successNotification = (msg: string = "成功") =>
  ElMessage({
    showClose: true,
    type: "success",
    message: msg,
  });

/**
 * 警告信息提示
 * @param msg 信息文本
 */
export const warningNotification = (msg: string = "成功") =>
  ElMessage({
    showClose: true,
    type: "warning",
    message: msg,
  });

/**
 * 错误信息提示
 * @param msg 信息文本
 */
export const errorNotification = (msg: string = "成功") =>
  ElMessage({
    showClose: true,
    type: "error",
    message: msg,
  });
