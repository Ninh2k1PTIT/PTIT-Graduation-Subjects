import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { Comment } from "app/model/Comment";
import { PaginationResponse } from "app/model/PaginationResponse";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class CommentService {
  constructor(private _http: HttpClient) {}

  // create(comment: Comment, files: File[]) {
  //   const formData = new FormData();
  //   formData.append("comment", JSON.stringify(comment));
  //   files.forEach((file) => {
  //     formData.append("files", file);
  //   });
  //   return this._http.post<BaseResponse<Comment>>(
  //     `${environment.apiUrl}/comment`,
  //     formData
  //   );
  // }

  create(comment: Comment) {
    return this._http.post<Comment>(`${environment.apiUrl}/comment`, comment);
  }

  getByPostId(
    postId: number,
    params: { lastCommentId?: number; size: number }
  ) {
    return this._http.get<BaseResponse<PaginationResponse<Comment>>>(
      `${environment.apiUrl}/post/${postId}/comments`,
      {
        params: new HttpParams({ fromObject: params }),
      }
    );
  }

  getById(id: number) {
    return this._http.get<BaseResponse<Comment>>(
      `${environment.apiUrl}/comment/${id}`
    );
  }
}
