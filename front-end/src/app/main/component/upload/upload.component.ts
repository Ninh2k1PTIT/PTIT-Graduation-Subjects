import {
  Component,
  EventEmitter,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Photo } from "app/model/Photo";
import { Post } from "app/model/Post";
import { UploadService } from "./upload.service";
import { User } from "app/model/User";
import { Observable, Subject, of } from "rxjs";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";
import { UserService } from "app/services/user.service";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-upload",
  templateUrl: "./upload.component.html",
  styleUrls: ["./upload.component.scss"],
})
export class UploadComponent implements OnInit {
  @ViewChild("modalBasic") public modal: NgbActiveModal;
  @Output() public onSuccess = new EventEmitter<Post>();
  public users: Observable<User[]> = null;
  public $search = new Subject<string>();
  public form: FormGroup;
  public isPhotoPreview = false;
  public isPhotoEdit = false;
  public isCrop = false;
  public isAddTag = false;
  public photos: Photo[] = [];
  public photoEditIndex: number;
  public binding;

  constructor(
    private _modalService: NgbModal,
    private _fb: FormBuilder,
    private _uploadService: UploadService,
    private _userService: UserService,
    private _toastrService: ToastrService
  ) {}

  get f() {
    return this.form.controls;
  }

  ngOnInit(): void {
    this.$search
      .pipe(debounceTime(500), distinctUntilChanged())
      .subscribe((val) => {
        this.users = this._filter(val);
      });
  }

  private _filter(value: string) {
    return value
      ? this._userService.getByUsername({ username: value })
      : of([] as User[]);
  }

  modalOpen() {
    this.photos = [];
    this.isPhotoPreview = false;
    this.isPhotoEdit = false;
    this.isCrop = false;
    this.isAddTag = false;
    this.form = this._fb.group({
      content: "",
    });
    this._modalService.open(this.modal, {
      size: "lg",
      scrollable: true,
    });
  }

  togglePhotoPreview() {
    this.isPhotoPreview = !this.isPhotoPreview;
  }

  openPhotoEdit(index: number) {
    this.isPhotoEdit = true;
    this.photoEditIndex = index;
    setTimeout(() => {
      const crop = document.getElementById("crop-wrapper") as HTMLDivElement;
      const cropBorder = document.getElementById(
        "crop-border"
      ) as HTMLDivElement;
      const img = document.getElementById("photo-edit") as HTMLImageElement;
      crop.style.width = img.width + "px";
      crop.style.height = img.height + "px";

      const resizable = document.getElementsByClassName("resize");
      for (let i = 0; i < resizable.length; i++) {
        const element = resizable.item(i) as HTMLDivElement;
        let resize = (event: MouseEvent) => {
          const startLeft = crop.offsetLeft,
            startX = event.clientX,
            startTop = crop.offsetTop,
            startY = event.clientY,
            startWidth = crop.offsetWidth,
            startHeight = crop.offsetHeight;
          let onMouseMove = (event: MouseEvent) => {
            event.preventDefault();
            const offsetX = event.clientX - startX,
              offsetY = event.clientY - startY;

            if (element.id == "top-left") {
              if (startLeft + offsetX >= 0) {
                crop.style.width = startWidth - offsetX + "px";
                crop.style.left = startLeft + offsetX + "px";
              }
              if (startTop + offsetY >= 0) {
                crop.style.height = startHeight - offsetY + "px";
                crop.style.top = startTop + offsetY + "px";
              }
            } else if (element.id == "top-right") {
              if (startLeft + startWidth + offsetX <= img.offsetWidth)
                crop.style.width = startWidth + offsetX + "px";
              if (startTop + offsetY >= 0) {
                crop.style.height = startHeight - offsetY + "px";
                crop.style.top = startTop + offsetY + "px";
              }
            } else if (element.id == "bottom-left") {
              if (startLeft + offsetX >= 0) {
                crop.style.width = startWidth - offsetX + "px";
                crop.style.left = startLeft + offsetX + "px";
              }
              if (startTop + startHeight + offsetY <= img.offsetHeight)
                crop.style.height = startHeight + offsetY + "px";
            } else if (element.id == "bottom-right") {
              if (startLeft + startWidth + offsetX <= img.offsetWidth)
                crop.style.width = startWidth + offsetX + "px";
              if (startTop + startHeight + offsetY <= img.offsetHeight)
                crop.style.height = startHeight + offsetY + "px";
            }

            cropBorder.style.borderWidth = `${crop.offsetTop}px ${
              img.width - crop.offsetWidth - crop.offsetLeft
            }px ${img.height - crop.offsetHeight - crop.offsetTop}px ${
              crop.offsetLeft
            }px`;
          };
          let onMouseUp = () => {
            document.removeEventListener("mousemove", onMouseMove);
            document.removeEventListener("mouseup", onMouseUp);
          };
          document.addEventListener("mousemove", onMouseMove);
          document.addEventListener("mouseup", onMouseUp);
        };
        element.addEventListener("mousedown", resize);
      }

      img.addEventListener("mousedown", (event: MouseEvent) => {
        if (this.isAddTag) {
          this.binding = null;
          const search = document.getElementById(
            "search-user"
          ) as HTMLDivElement;
          search.style.display = "block";
          search.style.top = event.offsetY + "px";
          search.style.left = event.offsetX + "px";
        }
      });
    });
  }

  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;

    if (files.length > 0) {
      for (let i = 0; i < files.length; i++)
        this.photos.push({
          file: files.item(i),
          b64: await this.convertFileToUrl(files.item(i)),
          tags: [],
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

  onSubmit() {
    const post = new Post();
    post.content = this.f.content.value;
    post.audience = 0;
    this._uploadService
      .create(
        post,
        this.photos.map((photo) => photo.file)
      )
      .subscribe((res) => {
        if (res.success) {
          this.onSuccess.emit(res.data);
          this._modalService.dismissAll();
          this._toastrService.success("Đã tạo bài viết", "Thành công", {
            toastClass: "toast ngx-toastr",
            closeButton: true,
          });
        }
      });
  }

  deletePhotoItem(i: number) {
    this.photos.splice(i, 1);
    if (this.photos.length == 0) this.isPhotoEdit = false;
  }

  savePhotoEdit() {
    const img = document.getElementById("photo-edit") as HTMLImageElement;
    const crop = document.getElementById("crop-wrapper") as HTMLDivElement;
    const ratioX = img.naturalWidth / img.offsetWidth,
      ratioY = img.naturalHeight / img.offsetHeight;
    const canvas = document.createElement("canvas");
    canvas.width = crop.offsetWidth * ratioX;
    canvas.height = crop.offsetHeight * ratioY;

    const ctx = canvas.getContext("2d");
    ctx.drawImage(
      img,
      crop.offsetLeft * ratioX,
      crop.offsetTop * ratioY,
      canvas.width,
      canvas.height,
      0,
      0,
      canvas.width,
      canvas.height
    );

    canvas.toBlob((blob: File) => {
      this.photos[this.photoEditIndex].b64 = canvas.toDataURL();
      this.photos[this.photoEditIndex].file = blob;
      this.isPhotoEdit = false;
    });
  }

  onSelectedUser(event: User) {
    const search = document.getElementById("search-user") as HTMLDivElement;
    const img = document.getElementById("photo-edit") as HTMLImageElement;
    const ratioX = img.naturalWidth / img.offsetWidth;
    const ratioY = img.naturalHeight / img.offsetHeight;
    this.photos[this.photoEditIndex].tags.push({
      left: search.offsetLeft * ratioX,
      top: search.offsetTop * ratioY,
      user: event,
    });
    console.log(this.photos);

    search.style.display = "none";
  }

  convertCoordinates(imageId: string, left: number, top: number) {
    const img = document.getElementById(imageId) as HTMLImageElement;
    const ratioX = img.naturalWidth / img.offsetWidth;
    const ratioY = img.naturalHeight / img.offsetHeight;

    return {
      left: left / ratioX + "px",
      top: top / ratioY + "px",
    };
  }
}
