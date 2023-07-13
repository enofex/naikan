import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {Error403Component} from "./403.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: Error403Component,
    canActivate: [AuthGuard],
  }])],
  exports: [RouterModule]
})
export class Error403RoutingModule {
}
