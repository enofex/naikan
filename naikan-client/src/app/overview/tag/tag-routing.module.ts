import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {TagComponent} from './tag.component';
import {AuthGuard} from "@naikan/shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '', component: TagComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class TagRoutingModule {
}
