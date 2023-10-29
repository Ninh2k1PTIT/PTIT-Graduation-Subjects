import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { UploadComponent } from './upload.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [UploadComponent],
  imports: [
    CommonModule,
    NgbModule
  ],
  exports: [UploadComponent]
})
export class UploadModule { }
