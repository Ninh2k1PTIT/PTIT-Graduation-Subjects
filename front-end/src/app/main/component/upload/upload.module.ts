import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { UploadComponent } from "./upload.component";
import { CoreCommonModule } from "@core/common.module";

@NgModule({
  declarations: [UploadComponent],
  imports: [CommonModule, NgbModule, ReactiveFormsModule, CoreCommonModule],
  exports: [UploadComponent],
})
export class UploadModule {}
