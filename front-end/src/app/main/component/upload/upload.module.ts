import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";
import { UploadComponent } from "./upload.component";
import { CoreCommonModule } from "@core/common.module";
import { NgSelectModule } from "@ng-select/ng-select";

@NgModule({
  declarations: [UploadComponent],
  imports: [
    CommonModule,
    NgbModule,
    ReactiveFormsModule,
    CoreCommonModule,
    NgSelectModule,
  ],
  exports: [UploadComponent],
})
export class UploadModule {}
