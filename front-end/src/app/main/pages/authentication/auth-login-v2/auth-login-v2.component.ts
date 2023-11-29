import { AfterViewInit, Component, OnInit, ViewEncapsulation } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { takeUntil } from "rxjs/operators";
import { Subject } from "rxjs";
import { Router } from "@angular/router";
import { CoreConfigService } from "@core/services/config.service";
import { AuthenticationService } from "app/auth/service";

@Component({
  selector: "app-auth-login-v2",
  templateUrl: "./auth-login-v2.component.html",
  styleUrls: ["./auth-login-v2.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class AuthLoginV2Component implements OnInit, AfterViewInit {
  //  Public
  public coreConfig: any;
  public loginForm: FormGroup;
  public loading = false;
  public submitted = false;
  public returnUrl: string;
  public error = "";
  public passwordTextType: boolean;

  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @param {CoreConfigService} _coreConfigService
   */
  constructor(
    private _coreConfigService: CoreConfigService,
    private _formBuilder: FormBuilder,
    private _route: ActivatedRoute,
    private _router: Router,
    private _authService: AuthenticationService
  ) {
    this._unsubscribeAll = new Subject();

    // Configure the layout
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
  ngAfterViewInit(): void {
    google.accounts.id.initialize({
      client_id: "427947856574-9kuvkbobk276vvq4eude89kvvs7724p7.apps.googleusercontent.com",
      callback: (response: any) => this.handleGoogleSignIn(response)
    });
    google.accounts.id.renderButton(
      document.getElementById("google-btn"),
      { size: "large", type: "icon", shape: "square" }  // customization attributes
    );
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  /**
   * Toggle password
   */
  togglePasswordTextType() {
    this.passwordTextType = !this.passwordTextType;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    // Login
    this.loading = true;

    this._authService
      .login(this.f.email.value, this.f.password.value)
      .subscribe(
        (res) => {
          if (res) this._router.navigate(["/home"]);
          this.loading = false;
        },
        (err) => {
          this.loading = false;
        }
      );
  }

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {


    this.loginForm = this._formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", Validators.required],
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this._route.snapshot.queryParams["returnUrl"] || "/";

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

  handleGoogleSignIn(response) {
    this._authService.googleSignin({ credential: response.credential }).subscribe(
      (res) => {
        console.log(res);
        this._router.navigate(["/home"]);
        this.loading = false;
      },
      (err) => {
        this.loading = false;

      })
  }
}
