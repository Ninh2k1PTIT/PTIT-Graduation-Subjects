import {
  Component,
  EventEmitter,
  OnInit,
  Output,
  ViewChild,
} from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { UploadService } from "./upload.service";
import { Post } from "app/model/Post";
import { Photo } from "app/model/Photo";

@Component({
  selector: "app-upload",
  templateUrl: "./upload.component.html",
  styleUrls: ["./upload.component.scss"],
})
export class UploadComponent implements OnInit {
  @ViewChild("modalBasic") public modal: NgbActiveModal;
  @Output() public onSuccess = new EventEmitter<void>();
  public form: FormGroup;
  public isPhotoEdit = false;
  public photos: Photo[] = [];

  constructor(
    private _modalService: NgbModal,
    private _fb: FormBuilder,
    private _uploadService: UploadService
  ) {}

  get f() {
    return this.form.controls;
  }

  ngOnInit(): void {
    this.form = this._fb.group({
      content: "",
    });
  }

  modalOpen() {
    this._modalService.open(this.modal, {
      size: "lg",
      scrollable: true,
    });
  }

  togglePhotoEdit() {
    this.isPhotoEdit = !this.isPhotoEdit;
  }

  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement;
    const files = target.files;
    console.log(files);

    if (files.length > 0) {
      for (let i = 0; i < files.length; i++)
        this.photos.push({
          file: files.item(i),
          b64: await this.convertFileToUrl(files.item(i)),
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
          this.onSuccess.emit();
          this.modal.close();
        }
      });
  }

  deletePhotoItem(i: number) {
    this.photos.splice(i, 1);
    if (this.photos.length == 0) this.isPhotoEdit = false;
  }
}
