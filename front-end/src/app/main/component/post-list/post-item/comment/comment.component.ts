import { Component, Input, OnInit } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Post } from "app/model/Post";

@Component({
  selector: "app-comment",
  templateUrl: "./comment.component.html",
  styleUrls: ["./comment.component.scss"],
})
export class CommentComponent implements OnInit {
  @Input("post") public post: Post;

  constructor(private _modalService: NgbModal) {}

  ngOnInit(): void {}

  modalOpen(modal) {
    this._modalService.open(modal, {
      size: "lg",
      scrollable: true,
      centered: true,
    });
  }
}
