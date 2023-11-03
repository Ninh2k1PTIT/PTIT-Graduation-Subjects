import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { EPostSort } from "app/model/EPostSort";
import { PaginationResponse } from "app/model/PaginationResponse";
import { Post } from "app/model/Post";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class PostService {
  constructor(private _http: HttpClient) {}

  create(post: Post, files: File[]) {
    const formData = new FormData();
    formData.append("post", JSON.stringify(post));
    files.forEach((file) => {
      formData.append("files", file);
    });
    return this._http.post(`${environment.apiUrl}/post`, formData);
  }

  search(params: {
    page: number;
    size: number;
    content?: string;
    sort?: EPostSort;
  }) {
    return this._http.get<BaseResponse<PaginationResponse<Post>>>(
      `${environment.apiUrl}/posts`,
      {
        params: new HttpParams({ fromObject: params }),
      }
    );
  }
}
