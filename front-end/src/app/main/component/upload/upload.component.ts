import {
  Component,
  ElementRef,
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
  public isPhotoPreview = false;
  public isPhotoEdit = false;
  public photos: Photo[] = [];
  public photoEditIndex: number

  constructor(
    private _modalService: NgbModal,
    private _fb: FormBuilder,
    private _uploadService: UploadService
  ) { }

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

  togglePhotoPreview() {
    this.isPhotoPreview = !this.isPhotoPreview;
  }

  openPhotoEdit(index: number) {
    this.isPhotoEdit = true
    this.photoEditIndex = index
    setTimeout(() => {
      const crop = document.getElementById('crop-wrapper') as HTMLDivElement
      const img = document.getElementById('photo-edit') as HTMLImageElement
      crop.style.width = img.width + "px"
      crop.style.height = img.height + "px"

      const resizable = document.getElementsByClassName('resize')
      for (let i = 0; i < resizable.length; i++) {
        const element = resizable.item(i) as HTMLDivElement
        let resize = (event: MouseEvent) => {
          const startLeft = crop.offsetLeft, startX = event.clientX, startTop = crop.offsetTop, startY = event.clientY, startWidth = crop.offsetWidth, startHeight = crop.offsetHeight
          console.log(crop.offsetLeft);

          let onMouseMove = (event: MouseEvent) => {

            if (i == 0) {
              crop.style.width = startWidth - (event.clientX - startX) + "px"
              crop.style.height = startHeight - (event.clientY - startY) + "px"
              crop.style.left = startLeft + (event.clientX - startX) + "px"
              crop.style.top = startTop + (event.clientY - startY) + "px"
            } else if (i == 1) {
              crop.style.width = startWidth - (startX - event.clientX) + "px"
              crop.style.height = startHeight - (event.clientY - startY) + "px"
              crop.style.top = startTop + (event.clientY - startY) + "px"
            } else if (i == 2) {
              crop.style.width = startWidth - (event.clientX - startX) + "px"
              crop.style.height = startHeight - (startY - event.clientY) + "px"
              crop.style.left = startLeft + (event.clientX - startX) + "px"
            } else if (i == 3) {
              crop.style.width = startWidth - (startX - event.clientX) + "px"
              crop.style.height = startHeight - (startY - event.clientY) + "px"
            }
          }
          let onMouseUp = () => {
            document.removeEventListener('mousemove', onMouseMove);
            document.removeEventListener('mouseup', onMouseUp);
          };
          document.addEventListener('mousemove', onMouseMove);
          document.addEventListener('mouseup', onMouseUp);
        };
        element.addEventListener('mousedown', resize)

      }

    })

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
          this._modalService.dismissAll()
        }
      });
  }

  deletePhotoItem(i: number) {
    this.photos.splice(i, 1);
    if (this.photos.length == 0) this.isPhotoEdit = false;
  }

  // crop(index: string) {
  //   const canvas = document.getElementById("canvas") as HTMLCanvasElement;
  //   const ctx = canvas.getContext("2d");
  //   const img = document.getElementById(index) as HTMLImageElement;
  //   console.log(img.offsetWidth);

  //   ctx.drawImage(img, 0, 0, 800, 550, 0, 0, 400, 300);
  // }

  savePhotoEdit() {
    const crop = document.getElementById('crop-wrapper') as HTMLDivElement
    const img = document.getElementById('photo-edit') as HTMLImageElement
    const ratioX = img.naturalWidth / img.offsetWidth, ratioY = img.naturalHeight / img.offsetHeight
    const canvas = document.createElement('canvas')
    canvas.width = crop.offsetWidth * ratioX
    canvas.height = crop.offsetHeight * ratioY
    const ctx = canvas.getContext("2d");
    console.log(crop.offsetLeft, crop.offsetTop, crop.offsetWidth, crop.offsetHeight, crop.offsetWidth, crop.offsetHeight);

    ctx.drawImage(img, crop.offsetLeft * ratioX, crop.offsetTop * ratioY, canvas.width, canvas.height, 0, 0, canvas.width, canvas.height);
    console.log(canvas.toDataURL());
    canvas.toBlob((blob: File) => {
      this.photos[this.photoEditIndex] = {
        b64: canvas.toDataURL(),
        file: blob
      }
      this.isPhotoEdit = false
    })


  }
}
