import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {TokenComponent} from "./token.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: TokenComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  }])],
  exports: [RouterModule]
})
export class TokenRoutingModule {
}
