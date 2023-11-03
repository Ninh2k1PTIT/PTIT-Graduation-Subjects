import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Post } from "app/model/Post";

@Component({
  selector: "app-comment",
  templateUrl: "./comment.component.html",
  styleUrls: ["./comment.component.scss"],
})
export class CommentComponent implements OnInit {
  @Input("post") public post: Post;
  @ViewChild("modal") public modal: NgbActiveModal;

  constructor(private _modalService: NgbModal) {}

  ngOnInit(): void {}

  modalOpen() {
    this._modalService.open(this.modal, {
      scrollable: true,
      size: "lg",
      centered: true,
    });
  }
}
