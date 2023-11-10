import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class CommentReactService {
  constructor(private _http: HttpClient) {}

  createByCommentId(commentId: number) {
    return this._http.post<BaseResponse<boolean>>(
      `${environment.apiUrl}/comment/${commentId}/react`,
      null
    );
  }
}
