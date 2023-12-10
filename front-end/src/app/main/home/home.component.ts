import {
  Component,
  HostListener,
  OnInit,
  ViewEncapsulation,
} from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
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
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class HomeComponent implements OnInit {
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
    {
      label: "Được gắn thẻ",
      value: EPostSort.TAG,
    },
  ];
  public form: FormGroup;

  public posts: Post[] = [];

  constructor(
    private _postService: PostService,
    private _fb: FormBuilder,
    private _modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.form = this._fb.group({
      sort: this.sortOption[0].value,
      content: "",
      fromDate: "",
      toDate: "",
      page: 0,
      size: 10,
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
    this.posts = [];
    this.form.patchValue({ page: 0 });
    this.totalPages = 0;
  }

  modalOpen(modal) {
    this._modalService.open(modal, {
      size: "lg",
      scrollable: true,
    });
  }
}
