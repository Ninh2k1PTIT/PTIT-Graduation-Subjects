import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule, Routes } from "@angular/router";
import { ProfileComponent } from "./profile.component";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { CoreCommonModule } from "@core/common.module";
import { PostListModule } from "../component/post-list/post-list.module";
import { InfoComponent } from "./info/info.component";
import { PostsComponent } from "./posts/posts.component";
import { FriendsComponent } from "./friends/friends.component";
import { ReactiveFormsModule } from "@angular/forms";
import { NgSelectModule } from "@ng-select/ng-select";
import { Ng2FlatpickrModule } from "ng2-flatpickr";
import { UploadModule } from "../component/upload/upload.module";

const routes: Routes = [
  {
    path: "",
    component: ProfileComponent,
    children: [
      {
        path: "posts",
        component: PostsComponent,
      },
      {
        path: "info",
        component: InfoComponent,
      },
      {
        path: "friends",
        component: FriendsComponent,
      },
      {
        path: "",
        redirectTo: "/profile/posts",
        pathMatch: "full",
      },
    ],
  },
];

@NgModule({
  declarations: [
    ProfileComponent,
    InfoComponent,
    PostsComponent,
    FriendsComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    NgbModule,
    CoreCommonModule,
    PostListModule,
    UploadModule,
    NgSelectModule,
    ReactiveFormsModule,
    Ng2FlatpickrModule,
  ],
})
export class ProfileModule {}
