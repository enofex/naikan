import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {Error500Component} from "./500.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: Error500Component,
    canActivate: [AuthGuard],
  }])],
  exports: [RouterModule]
})
export class Error500RoutingModule {
}
