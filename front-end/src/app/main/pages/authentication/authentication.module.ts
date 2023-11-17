import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { CoreCommonModule } from '@core/common.module';

import { AuthLoginV2Component } from 'app/main/pages/authentication/auth-login-v2/auth-login-v2.component';
import { AuthRegisterComponent } from './auth-register/auth-register.component';

// routing
const routes: Routes = [
  {
    path: 'authentication/login',
    component: AuthLoginV2Component,
  },
  {
    path: 'authentication/register',
    component: AuthRegisterComponent
  },
];

@NgModule({
  declarations: [AuthLoginV2Component, AuthRegisterComponent],
  imports: [CommonModule, RouterModule.forChild(routes), NgbModule, FormsModule, ReactiveFormsModule, CoreCommonModule]
})
export class AuthenticationModule { }
