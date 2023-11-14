import { Component, Input, OnInit } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Comment } from "app/model/Comment";
import { CommentReact } from "app/model/CommentReact";
import { CommentReactService } from "app/services/comment-react.service";
import { CommentService } from "app/services/comment.service";
import { switchMap } from "rxjs/operators";

@Component({
  selector: "app-comment-react",
  templateUrl: "./comment-react.component.html",
  styleUrls: ["./comment-react.component.scss"],
})
export class CommentReactComponent implements OnInit {
  @Input("comment") public comment: Comment;
  public reacts: CommentReact[] = [];
  public currentPage = 0;
  public totalPages = 0;

  constructor(
    private _commentReactService: CommentReactService,
    private _commentService: CommentService,
    private _modalService: NgbModal
  ) {}

  ngOnInit(): void {}

  reactComment() {
    this._commentReactService
      .createByCommentId(this.comment.id)
      .pipe(switchMap(() => this._commentService.getById(this.comment.id)))
      .subscribe((res) => {
        this.comment = res.data;
      });
  }

  modalOpen(modal) {
    this._modalService.open(modal, {
      scrollable: true,
      centered: true,
    });
    this.currentPage = 0;
    this.totalPages = 0;
    this.getReacts();
  }

  getReacts() {
    this._commentReactService
      .getByCommentId(this.comment.id, { page: this.currentPage, size: 15 })
      .subscribe((res) => {
        this.reacts = res.data.data;
        this.totalPages = res.data.totalPages;
      });
  }
}
