import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { PaginationResponse } from "app/model/PaginationResponse";
import { PostReact } from "app/model/PostReact";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class PostReactService {
  constructor(private _http: HttpClient) {}

  createByPostId(postId: number) {
    return this._http.post<BaseResponse<boolean>>(
      `${environment.apiUrl}/post/${postId}/react`,
      null
    );
  }

  getByPostId(
    postId: number,
    params: {
      page: number;
      size: number;
    }
  ) {
    return this._http.get<BaseResponse<PaginationResponse<PostReact>>>(
      `${environment.apiUrl}/post/${postId}/reacts`,
      { params: new HttpParams({ fromObject: params }) }
    );
  }
}
