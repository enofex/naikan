import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "@naikan/shared";
import {ProfileComponent} from "./profile.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: ProfileComponent,
    canActivate: [AuthGuard],
  }])],
  exports: [RouterModule]
})
export class ProfileRoutingModule {
}
