import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FriendComponent } from './friend.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CoreCommonModule } from '@core/common.module';
import { SearchComponent } from './search/search.component';

const routes: Routes = [
  {
    path: '',
    component: FriendComponent
  }
]

@NgModule({
  declarations: [FriendComponent, SearchComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    CoreCommonModule,
    NgbModule
  ]
})
export class FriendModule { }
