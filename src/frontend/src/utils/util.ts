import dayjs from "dayjs";

export namespace Util {
  /**
   * 
   * @param date 日期
   * @param format dayjs的format格式，默认YYYY-MM-DDTHH:mm:ss
   * @returns 字符串
   */
  export function formatDate(date: Date | string, format: string = "YYYY-MM-DDTHH:mm:ss"): string {
    return typeof date === "string" ? dayjs(date).format(format) : dayjs(date).format(format);
  }

  /**
   * 判断字符串是否为空或者任何空字符串
   * @param str 字符串
   * @return boolean
   */
  export function isBlank(str: string | undefined | null): boolean {
    return str === undefined || str === null || str.trim() === "";
  }

    /**
   * 合并所有路径，以/连接
   * @param args 路径
   * @return string
   */
  export function join(...args: string[]): string{
    if(args.length == 0) return "";
    if(args.length == 1) return args[0];
    let url = args[0].replace(/\/*$/, "");
    for (let i = 1; i < args.length; i++) {
      url += args[i].replace(/^\/*/, "") + "/";
    }
    return url;
  }
}
