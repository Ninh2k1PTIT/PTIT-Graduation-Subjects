import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { UserService } from "app/services/user.service";
import {
  debounceTime,
  distinctUntilChanged,
  startWith,
  switchMap,
} from "rxjs/operators";

@Component({
  selector: "app-search",
  templateUrl: "./search.component.html",
  styleUrls: ["./search.component.scss"],
})
export class SearchComponent implements OnInit {
  public form: FormGroup;
  public users = [];

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
      .subscribe((res: string) => {
        if (res.trim() == "") this.users = [];
        else this.form.patchValue({ page: 0 });
      });

    this.form
      .get("page")
      .valueChanges.pipe(
        switchMap(() => this._userService.search(this.form.value))
      )
      .subscribe((res) => {
        this.users = res.data.data;
      });
  }

  onScroll(event: Event) {
    console.log(event.target);
  }
}
