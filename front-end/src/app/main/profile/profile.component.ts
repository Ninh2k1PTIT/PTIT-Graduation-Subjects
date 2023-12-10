import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthenticationService } from "app/auth/service";
import { User } from "app/model/User";
import { UserService } from "app/services/user.service";
import { ToastrService } from "ngx-toastr";
import { switchMap } from "rxjs/operators";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.scss"],
})
export class ProfileComponent implements OnInit {
  public toggleMenu = true;
  public currentUser: User;

  constructor(
    private _authService: AuthenticationService,
    private _route: ActivatedRoute,
    private _userService: UserService,
    private _toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this._route.params
      .pipe(
        switchMap((params) => {
          let id = this._authService.currentUserValue.id;
          if (Object.keys(params).length > 0) id = params.id;
          return this._userService.getById(id);
        })
      )
      .subscribe((res) => {
        this.currentUser = res;
      });
  }

  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;

    if (files.length > 0)
      this._authService.updateAvatar(files[0]).subscribe((res) => {
        this.currentUser = res
        this._toastrService.success("Đã cập nhật ảnh đại diện", "Thành công", {
          toastClass: "toast ngx-toastr",
          closeButton: true,
        });
      });
  }
}
