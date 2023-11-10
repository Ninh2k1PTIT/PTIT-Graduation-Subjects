import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { PostListComponent } from "./post-list.component";
import { PostItemComponent } from "./post-item/post-item.component";
import { CoreCommonModule } from "@core/common.module";
import { CommentComponent } from "./post-item/comment/comment.component";
import { ReactListComponent } from "./post-item/react-list/react-list.component";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { PerfectScrollbarModule } from "ngx-perfect-scrollbar";
import { CommentListComponent } from './post-item/comment/comment-list/comment-list.component';

@NgModule({
  declarations: [
    PostListComponent,
    PostItemComponent,
    CommentComponent,
    ReactListComponent,
    CommentListComponent,
  ],
  imports: [CommonModule, CoreCommonModule, NgbModule, PerfectScrollbarModule],
  exports: [PostListComponent],
})
export class PostListModule {}
