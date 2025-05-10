export interface BaseResult<T> {
  code: number;
  msg: string;
  success: boolean;
  data: T;
}

export type VoidResult = BaseResult<void>;
