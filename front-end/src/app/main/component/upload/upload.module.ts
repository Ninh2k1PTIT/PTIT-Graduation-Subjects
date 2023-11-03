import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { UploadComponent } from "./upload.component";

@NgModule({
  declarations: [UploadComponent],
  imports: [CommonModule, NgbModule, ReactiveFormsModule],
  exports: [UploadComponent],
})
export class UploadModule {}
