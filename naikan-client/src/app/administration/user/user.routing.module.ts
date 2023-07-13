import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {UserComponent} from "./user.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: UserComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  }])],
  exports: [RouterModule]
})
export class UserRoutingModule {
}
