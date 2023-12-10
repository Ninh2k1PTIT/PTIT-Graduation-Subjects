import { Component, ElementRef, Input, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { AuthenticationService } from "app/auth/service";
import { Comment } from "app/model/Comment";
import { CommentPhoto } from "app/model/CommentPhoto";
import { Post } from "app/model/Post";
import { CommentService } from "app/services/comment.service";

@Component({
  selector: "app-comment-list",
  templateUrl: "./comment-list.component.html",
  styleUrls: ["./comment-list.component.scss"],
})
export class CommentListComponent implements OnInit {
  @Input("post") public post: Post;
  @Input("modal") public modal: NgbActiveModal;
  @ViewChild("scrollMe") public scrollMe: ElementRef<HTMLDivElement>;
  public scrollTop: number = null;
  public form: FormGroup;
  public photos: CommentPhoto[] = [];
  public comments: Comment[] = [];
  public stop = false;

  constructor(
    private _commentService: CommentService,
    private _authService: AuthenticationService,
    private _fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.form = this._fb.group({
      content: [null, Validators.required],
    });

    this._commentService
      .getByPostId(this.post.id, { size: 20 })
      .subscribe((res) => {
        this.comments = res.data.data.reverse();
        setTimeout(() => {
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
        }, 0);
        if (res.data.currentPage == res.data.totalPages - 1) this.stop = true;
      });
  }

  onSubmit() {
    if (this.form.valid || this.photos.length > 0) {
      let comment = new Comment();
      comment.postId = this.post.id;
      comment.content = this.form.get("content").value;
      comment.createdAt = null;
      comment.isReact = false;
      comment.totalReact = 0;
      comment.createdBy = this._authService.currentUserValue;
      comment.photos = this.photos;
      this.comments.push(comment);
      setTimeout(() => {
        this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
      }, 0);
      this._commentService.create(comment).subscribe((res) => {
        this.comments[this.comments.length - 1] = res;
        setTimeout(() => {
          this.scrollTop = this.scrollMe.nativeElement.scrollHeight;
        }, 0);
      });
      this.form.reset();
      this.photos = [];
    }
  }

  onKeydown(event: KeyboardEvent) {
    if (event.key == "Enter" && !event.shiftKey) {
      event.preventDefault();
      this.onSubmit();
    }
  }

  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;

    if (files.length > 0) {
      for (let i = 0; i < files.length; i++)
        this.photos.push({
          content: await this.convertFileToUrl(files.item(i)),
          type: files.item(i).type,
          name: files.item(i).name,
        });
    }
  }

  onScroll(event: Event) {
    const target = event.target as HTMLDivElement;
    const element = this.scrollMe.nativeElement as HTMLDivElement;
    const before = element.scrollHeight;

    if (target.scrollTop == 0 && !this.stop) {
      this._commentService
        .getByPostId(this.post.id, {
          lastCommentId: this.comments[0].id,
          size: 20,
        })
        .subscribe((res) => {
          this.comments = [...res.data.data.reverse(), ...this.comments];
          setTimeout(() => {
            this.scrollTop = element.scrollHeight - before;
          }, 0);
          if (res.data.currentPage == res.data.totalPages - 1) this.stop = true;
        });
    }
  }

  convertFileToUrl(file: File) {
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        resolve(reader.result.toString());
      };
      reader.readAsDataURL(file);
    });
  }
}
