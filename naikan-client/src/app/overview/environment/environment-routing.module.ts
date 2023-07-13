import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {EnvironmentComponent} from './environment.component';
import {AuthGuard} from "../../shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '', component: EnvironmentComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class EnvironmentRoutingModule {
}
