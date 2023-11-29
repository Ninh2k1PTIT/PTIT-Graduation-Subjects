import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject, Observable, of, throwError } from "rxjs";
import { catchError, map, switchMap, tap } from "rxjs/operators";

import { environment } from "environments/environment";
import { ToastrService } from "ngx-toastr";
import { BaseResponse } from "app/model/BaseResponse";
import { Jwt } from "app/model/Jwt";
import { User } from "app/model/User";

@Injectable({ providedIn: "root" })
export class AuthenticationService {
  //public
  public currentUser: Observable<User>;

  //private
  private currentUserSubject: BehaviorSubject<User>;
  private currentJwtSubject: BehaviorSubject<string>;

  /**
   *
   * @param {HttpClient} _http
   * @param {ToastrService} _toastrService
   */
  constructor(
    private _http: HttpClient,
    private _toastrService: ToastrService
  ) {
    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem("currentUser"))
    );
    this.currentJwtSubject = new BehaviorSubject<string>(
      JSON.parse(localStorage.getItem("accessToken"))
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  // getter: currentUserValue
  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  // getter: currentJwrValue
  public get currentJwtValue(): string {
    return this.currentJwtSubject.value;
  }

  /**
   *  Confirms if user is admin
   */
  // get isAdmin() {
  //   return (
  //     this.currentUser && this.currentUserSubject.value.role === Role.Admin
  //   );
  // }

  /**
   *  Confirms if user is client
   */
  // get isClient() {
  //   return (
  //     this.currentUser && this.currentUserSubject.value.role === Role.Client
  //   );
  // }

  /**
   * User login
   *
   * @param email
   * @param password
   * @returns user
   */
  login(email: string, password: string) {
    return this._http
      .post<BaseResponse<Jwt>>(`${environment.apiUrl}/auth/login`, {
        email,
        password,
      })
      .pipe(
        switchMap((res) => {
          if (res.success) {
            localStorage.setItem(
              "accessToken",
              JSON.stringify(res.data.accessToken)
            );
            this.currentJwtSubject.next(res.data.accessToken);
            return this.getCurrentUser().pipe(
              map((res) => {
                this._toastrService.success(
                  "👋 Xin chào, " + res.data.username + "!",
                  "Thành công",
                  { toastClass: "toast ngx-toastr", closeButton: true }
                );
                localStorage.setItem("currentUser", JSON.stringify(res.data));
                this.currentUserSubject.next(res.data);
                return res.data;
              })
            );
          }

          this._toastrService.error(res.userMessage, "Thất bại", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
          });
          return of(null);
        })
      );
  }

  signup(body: { email: string; password: string; username: string }) {
    return this._http.post<User>(`${environment.apiUrl}/auth/signup`, body);
  }

  googleSignin(body: { credential: string; }) {
    return this._http
      .post<BaseResponse<Jwt>>(`${environment.apiUrl}/auth/googleSignIn`, body)
      .pipe(
        switchMap((res) => {
          if (res.success) {
            localStorage.setItem(
              "accessToken",
              JSON.stringify(res.data.accessToken)
            );
            this.currentJwtSubject.next(res.data.accessToken);
            return this.getCurrentUser().pipe(
              map((res) => {
                this._toastrService.success(
                  "👋 Xin chào, " + res.data.username + "!",
                  "Thành công",
                  { toastClass: "toast ngx-toastr", closeButton: true }
                );
                localStorage.setItem("currentUser", JSON.stringify(res.data));
                this.currentUserSubject.next(res.data);
                return res.data;
              })
            );
          }

          this._toastrService.error(res.userMessage, "Thất bại", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
          });
          return of(null);
        })
      );
  }

  getCurrentUser() {
    return this._http.get<BaseResponse<User>>(
      `${environment.apiUrl}/user/self`
    );
  }
  /**
   * User logout
   *
   */
  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem("currentUser");
    // notify
    this.currentUserSubject.next(null);
  }
}
