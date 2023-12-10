import {
  Component,
  HostListener,
  OnInit,
  ViewEncapsulation,
} from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { AuthenticationService } from "app/auth/service";
import { EPostSort } from "app/model/EPostSort";
import { Post } from "app/model/Post";
import { PostService } from "app/services/post.service";
import { FlatpickrOptions } from "ng2-flatpickr";
import { combineLatest } from "rxjs";
import {
  debounceTime,
  delay,
  distinctUntilChanged,
  mergeMap,
  startWith
} from "rxjs/operators";

@Component({
  selector: "app-posts",
  templateUrl: "./posts.component.html",
  styleUrls: ["./posts.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class PostsComponent implements OnInit {
  public DateRangeOptions: FlatpickrOptions = {
    altInput: true,
    mode: "range",
    altFormat: "d-m-Y",
    locale: require("flatpickr/dist/l10n/vn").default.vn,
    onClose: (res: [Date, Date]) => {
      this.form.patchValue({
        fromDate: res[0].getTime(),
        toDate: res[1].getTime(),
      });
    },
  };

  public totalPages = 0;
  public sortOption = [
    {
      label: "Mới nhất",
      value: "",
    },
    {
      label: "Nhiều bình luận nhất",
      value: EPostSort.COMMENT,
    },
    {
      label: "Nhiều lượt thích nhất",
      value: EPostSort.REACT,
    },
  ];
  public form: FormGroup;

  public posts: Post[] = [];

  constructor(
    private _postService: PostService,
    private _fb: FormBuilder,
    private _modalService: NgbModal,
    private _authService: AuthenticationService,
    private _route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    if (this.isEdit())
      this.sortOption.push({
        label: "Được gắn thẻ",
        value: EPostSort.TAG,
      })

    this.form = this._fb.group({
      sort: this.sortOption[0].value,
      content: "",
      fromDate: "",
      toDate: "",
      page: 0,
      size: 10,
      userId: this._route.snapshot.parent.params.id || this._authService.currentUserValue.id,
    });

    this.form
      .get("page")
      .valueChanges.pipe(
        delay(0),
        mergeMap(() => this._postService.search(this.form.value))
      )
      .subscribe((res) => {
        this.posts = [...this.posts, ...res.data.data];
        this.totalPages = res.data.totalPages;
      });

    combineLatest([
      this.form
        .get("content")
        .valueChanges.pipe(
          debounceTime(1000),
          startWith(""),
          distinctUntilChanged()
        ),
      this.form
        .get("sort")
        .valueChanges.pipe(startWith(this.sortOption[0].value)),
      this.form.get("toDate").valueChanges.pipe(startWith("")),
    ]).subscribe(() => {
      this.posts = [];
      this.totalPages = 0;
      this.form.patchValue({ page: 0 });
    });
  }

  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {
    const pos =
      (document.documentElement.scrollTop || document.body.scrollTop) +
      document.documentElement.offsetHeight;
    const max = document.documentElement.scrollHeight;
    const currentPage = this.form.get("page").value;
    if (pos == max && currentPage < this.totalPages - 1)
      this.form.patchValue({ page: currentPage + 1 });
  }

  newPost(post: Post) {
    this._postService.getById(post.id).subscribe(res => {
      this.posts = [res.data, ...this.posts];
    })
  }

  refresh() {
    this.form.patchValue({ page: 0 });
    this.totalPages = 0;
  }

  modalOpen(modal) {
    this._modalService.open(modal, {
      size: "lg",
      scrollable: true,
    });
  }

  isEdit() {
    if (this._route.snapshot.parent.params.id == undefined)
      return true

    return this._authService.currentUserValue.id == this._route.snapshot.parent.params.id
  }
}
