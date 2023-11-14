import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { CommentReact } from "app/model/CommentReact";
import { PaginationResponse } from "app/model/PaginationResponse";
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

  getByCommentId(
    commentId: number,
    params: {
      page: number;
      size: number;
    }
  ) {    
    return this._http.get<BaseResponse<PaginationResponse<CommentReact>>>(
      `${environment.apiUrl}/comment/${commentId}/reacts`,
      { params: new HttpParams({ fromObject: params }) }
    );
  }
}
