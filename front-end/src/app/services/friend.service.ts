import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BaseResponse } from "app/model/BaseResponse";
import { ERelationship } from "app/model/ERelationship";
import { Friend } from "app/model/Friend";
import { PaginationResponse } from "app/model/PaginationResponse";
import { environment } from "environments/environment";

@Injectable({
  providedIn: "root",
})
export class FriendService {
  constructor(private _http: HttpClient) {}

  search(params: {
    page: number;
    size: number;
    username: string;
    relationship: ERelationship;
  }) {
    return this._http.get<BaseResponse<PaginationResponse<Friend>>>(
      `${environment.apiUrl}/friends`,
      { params: new HttpParams({ fromObject: params }) }
    );
  }
}
