import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home.component";
import { CoreCommonModule } from "@core/common.module";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { UploadModule } from "../component/upload/upload.module";
import { PostListModule } from "../component/post-list/post-list.module";
import { NgSelectModule } from "@ng-select/ng-select";
import { ReactiveFormsModule } from "@angular/forms";
import { Ng2FlatpickrModule } from 'ng2-flatpickr';

const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
  },
];

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CoreCommonModule,
    NgbModule,
    UploadModule,
    PostListModule,
    NgSelectModule,
    ReactiveFormsModule,
    Ng2FlatpickrModule
  ],
  exports: [HomeComponent],
})
export class HomeModule {}
