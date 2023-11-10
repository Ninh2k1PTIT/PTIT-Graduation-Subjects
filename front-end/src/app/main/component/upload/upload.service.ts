import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { Post } from "app/model/Post";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class UploadService {
  constructor(private _http: HttpClient) {}

  create(post: Post, files: File[]) {
    const formData = new FormData();
    formData.append("post", JSON.stringify(post));
    files.forEach((file) => {
      formData.append("files", file);
    });
    return this._http.post<BaseResponse<Post>>(
      `${environment.apiUrl}/post`,
      formData
    );
  }
}
