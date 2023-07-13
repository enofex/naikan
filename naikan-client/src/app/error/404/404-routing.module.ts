import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {Error404Component} from "./404.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: Error404Component,
    canActivate: [AuthGuard],
  }])],
  exports: [RouterModule]
})
export class Error404RoutingModule {
}
