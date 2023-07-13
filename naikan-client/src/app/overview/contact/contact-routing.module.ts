import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ContactComponent} from './contact.component';
import {AuthGuard} from "../../shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: ContactComponent,
    canActivate: [AuthGuard],
  }])],
  exports: [RouterModule]
})
export class ContactRoutingModule {
}
