import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostListComponent } from './post-list.component';
import { PostItemComponent } from './post-item/post-item.component';
import { CoreCommonModule } from '@core/common.module';
import { CommentComponent } from './post-item/comment/comment.component';
import { ReactListComponent } from './post-item/react-list/react-list.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [PostListComponent, PostItemComponent, CommentComponent, ReactListComponent],
  imports: [
    CommonModule, CoreCommonModule, NgbModule
  ],
  exports: [PostListComponent]
})
export class PostListModule { }
