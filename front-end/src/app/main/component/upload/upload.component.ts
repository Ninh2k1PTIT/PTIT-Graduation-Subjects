import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {
  @ViewChild('modalBasic') public modal: NgbActiveModal
  public form: FormGroup

  constructor(private _modalService: NgbModal, private _fb: FormBuilder) { }

  get f() {
    return this.form.controls
  }

  ngOnInit(): void {
    this.form = this._fb.group({
      content: null,
      photos: [[]]
    })
  }

  modalOpen() {
    this._modalService.open(this.modal, {
      windowClass: 'modal',
      centered: true
    });
  }


  async onFileInput(event: Event) {
    const target = event.target as HTMLInputElement
    const files = target.files
    if (files.length > 0) {
      let value = this.form.get('photos').value as any[]
      for (let i = 0; i < files.length; i++)
        value.push({
          file: files.item(i),
          b64: await this.convertFileToUrl(files.item(i))
        })
      this.form.patchValue({ photos: value })
    }
  }

  convertFileToUrl(file: File) {
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader()
      reader.onload = () => {
        resolve(reader.result.toString())
      }
      reader.readAsDataURL(file)
    })
  }

  onSubmit() {
    console.log(this.form.value);
  }
}
