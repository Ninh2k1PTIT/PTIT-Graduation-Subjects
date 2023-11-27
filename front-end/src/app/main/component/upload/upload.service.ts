import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { Post } from "app/model/Post";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class UploadService {
  constructor(private _http: HttpClient) { }

  create(post: Post) {
    return this._http.post<Post>(
      `${environment.apiUrl}/post`,
      post
    );
  }
}
