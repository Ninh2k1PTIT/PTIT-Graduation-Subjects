import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  Validators,
  ValidatorFn,
  AbstractControl,
} from "@angular/forms";
import { Router } from "@angular/router";
import { CoreConfigService } from "@core/services/config.service";
import { AuthenticationService } from "app/auth/service";
import { ToastrService } from "ngx-toastr";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

@Component({
  selector: "app-auth-register",
  templateUrl: "./auth-register.component.html",
  styleUrls: ["./auth-register.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class AuthRegisterComponent implements OnInit {
  public coreConfig: any;
  public passwordTextType: boolean;
  public registerForm: FormGroup;
  public submitted = false;

  private _unsubscribeAll: Subject<any>;

  constructor(
    private _coreConfigService: CoreConfigService,
    private _formBuilder: FormBuilder,
    private _toastr: ToastrService,
    private _authService: AuthenticationService,
    private _router: Router
  ) {
    this._unsubscribeAll = new Subject();

    this._coreConfigService.config = {
      layout: {
        navbar: {
          hidden: true,
        },
        menu: {
          hidden: true,
        },
        footer: {
          hidden: true,
        },
        customizer: false,
        enableLocalStorage: false,
      },
    };
  }

  get f() {
    return this.registerForm.controls;
  }

  togglePasswordTextType() {
    this.passwordTextType = !this.passwordTextType;
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    this._authService.signup(this.registerForm.value).subscribe(
      () => {
        this._toastr.success("Đã đăng kí tài khoản mới", "Thành công", {
          toastClass: "toast ngx-toastr",
          closeButton: true,
        });
        this._router.navigate(["/pages/authentication/login"]);
      },
      (err) => {
        this._toastr.error(err, "Thất bại", {
          toastClass: "toast ngx-toastr",
          closeButton: true,
        });
      }
    );
  }

  ngOnInit(): void {
    this.registerForm = this._formBuilder.group(
      {
        username: ["", [Validators.required]],
        email: ["", [Validators.required, Validators.email]],
        password: ["", Validators.required],
        confirmPassword: ["", Validators.required],
      },
      {
        validators: this.matchValidator("password", "confirmPassword"),
      }
    );

    // Subscribe to config changes
    this._coreConfigService.config
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe((config) => {
        this.coreConfig = config;
      });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  matchValidator(
    controlName: string,
    matchingControlName: string
  ): ValidatorFn {
    return (abstractControl: AbstractControl) => {
      const control = abstractControl.get(controlName);
      const matchingControl = abstractControl.get(matchingControlName);

      if (
        matchingControl!.errors &&
        !matchingControl!.errors?.["confirmedValidator"]
      ) {
        return null;
      }

      if (control!.value !== matchingControl!.value) {
        const error = { confirmedValidator: "Passwords do not match." };
        matchingControl!.setErrors(error);
        return error;
      } else {
        matchingControl!.setErrors(null);
        return null;
      }
    };
  }
}
