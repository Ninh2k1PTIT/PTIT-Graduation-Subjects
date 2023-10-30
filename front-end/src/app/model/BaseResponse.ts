export class BaseResponse<T> {
  data: T;
  success: boolean;
  userMessage: string;
  debugMessage: string;
}
