import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { PaginationResponse } from "app/model/PaginationResponse";
import { User } from "app/model/User";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class UserService {
  constructor(private _http: HttpClient) {}

  search(params: { page: number; size: number; username: string }) {
    return this._http.get<BaseResponse<PaginationResponse<User>>>(
      `${environment.apiUrl}/users`,
      { params: new HttpParams({ fromObject: params }) }
    );
  }

  getByUsername(params: { username: string }) {
    return this._http.get<User[]>(
      `${environment.apiUrl}/users/all`,
      { params: new HttpParams({ fromObject: params }) }
    );
  }
}
