import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { UserService } from "app/services/user.service";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";

@Component({
  selector: "app-search",
  templateUrl: "./search.component.html",
  styleUrls: ["./search.component.scss"],
})
export class SearchComponent implements OnInit {
  public form: FormGroup;
  suggestions = []

  constructor(private _userService: UserService, private _fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this._fb.group({
      page: 0,
      size: 10,
      username: "",
    });

    this.form
      .get("username")
      .valueChanges.pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe((res) => {
        console.log(res);
      });
  }
}
