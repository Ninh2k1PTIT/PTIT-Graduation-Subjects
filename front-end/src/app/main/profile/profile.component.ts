import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { AuthenticationService } from "app/auth/service";
import { User } from "app/model/User";
import { UserService } from "app/services/user.service";
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
    private _userService: UserService
  ) {}

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
}
