import { Component, OnInit } from "@angular/core";
import { AuthenticationService } from "app/auth/service";
import { User } from "app/model/User";

@Component({
  selector: "app-profile",
  templateUrl: "./profile.component.html",
  styleUrls: ["./profile.component.scss"],
})
export class ProfileComponent implements OnInit {
  public toggleMenu = true;
  public currentUser: User;

  constructor(
    private _authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.currentUser = this._authService.currentUserValue;
  }
}
