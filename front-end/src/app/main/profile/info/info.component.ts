import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'app/auth/service';
import { User } from 'app/model/User';
import { UserService } from 'app/services/user.service';
import { FlatpickrOptions } from 'ng2-flatpickr';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class InfoComponent implements OnInit {
  public birthDateOptions: FlatpickrOptions = {
    altInput: true,
    altFormat: "d-m-Y",
    mode: "single",
    locale: require("flatpickr/dist/l10n/vn").default.vn,
    onClose: (res: [Date]) => {
      setTimeout(() => {
        this.form.patchValue({
          birthday: res[0].getTime(),
        });
      })
    },
    clickOpens: false
  };

  public form: FormGroup
  public user: User

  constructor(private _fb: FormBuilder, private _route: ActivatedRoute, private _authService: AuthenticationService, private _toastrService: ToastrService, private _userService: UserService) { }

  ngOnInit(): void {
    this.form = this._fb.group({
      username: [{ value: '', disabled: !this.isEdit() }, Validators.required],
      phoneNumber: [{ value: '', disabled: !this.isEdit() }],
      address: [{ value: '', disabled: !this.isEdit() }],
      birthday: [{ value: '', disabled: !this.isEdit() }],
      description: [{ value: '', disabled: !this.isEdit() }],
      gender: [{ value: '', disabled: !this.isEdit() }]
    })

    this._userService.getById(this._route.snapshot.parent.params.id || this._authService.currentUserValue.id).subscribe(res => {
      this.user = res
      this.reset()
    })
  }

  onSubmit() {
    this._authService.update(this.form.value).subscribe(res => {
      this.user = res
      this.reset()
      this._toastrService.success(
        "Đã cập nhật thông tin",
        "Thành công",
        { toastClass: "toast ngx-toastr", closeButton: true }
      );
    })
  }

  reset() {
    this.form.patchValue(this.user)
    this.birthDateOptions.defaultDate = this.user.birthday
  }

  isEdit() {
    if (this._route.snapshot.parent.params.id == undefined)
      return true

    return this._authService.currentUserValue.id == this._route.snapshot.parent.params.id
  }
}
