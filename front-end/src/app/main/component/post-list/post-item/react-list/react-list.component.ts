import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Post } from "app/model/Post";
import { PostReact } from "app/model/PostReact";
import { PostReactService } from "app/services/post-react.service";
import { PostService } from "app/services/post.service";
import { switchMap } from "rxjs/operators";

@Component({
  selector: "app-react-list",
  templateUrl: "./react-list.component.html",
  styleUrls: ["./react-list.component.scss"],
})
export class ReactListComponent implements OnInit {
  @Input("post") public post: Post;
  @ViewChild("modal") public modal: NgbActiveModal;
  public reacts: PostReact[] = [];
  public currentPage = 0;
  public totalPages = 0;
  constructor(
    private _modalService: NgbModal,
    private _postReactService: PostReactService,
    private _postService: PostService
  ) {}

  ngOnInit(): void {}

  modalOpen() {
    this._modalService.open(this.modal, {
      scrollable: true,
      centered: true,
    });
    this.currentPage = 0;
    this.totalPages = 0;
    this.getReacts();
  }

  updateReact() {
    this._postReactService
      .createByPostId(this.post.id)
      .pipe(switchMap(() => this._postService.getById(this.post.id)))
      .subscribe((res) => {
        this.post = res.data;
      });
  }

  onScroll(event: Event) {
    const target = event.target as HTMLDivElement;
    if (
      target.scrollTop + target.offsetHeight == target.scrollHeight &&
      this.currentPage < this.totalPages - 1
    ) {
      this.currentPage += 1;
      this.getReacts();
    }
  }

  getReacts() {
    this._postReactService
      .getByPostId(this.post.id, { page: this.currentPage, size: 15 })
      .subscribe((res) => {
        this.reacts = res.data.data;
        this.totalPages = res.data.totalPages;
      });
  }
}
