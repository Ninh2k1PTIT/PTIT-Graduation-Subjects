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
import { CommentReactComponent } from './post-item/comment/comment-react/comment-react.component';
import { RouterModule } from "@angular/router";
import { PickerModule } from "@ctrl/ngx-emoji-mart";
import { EmojiModule } from "@ctrl/ngx-emoji-mart/ngx-emoji";

@NgModule({
  declarations: [
    PostListComponent,
    PostItemComponent,
    CommentComponent,
    ReactListComponent,
    CommentListComponent,
    CommentReactComponent,
  ],
  imports: [CommonModule, CoreCommonModule, NgbModule, PerfectScrollbarModule, RouterModule, EmojiModule,
    PickerModule,],
  exports: [PostListComponent],
})
export class PostListModule { }
