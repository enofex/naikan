import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {DeploymentComponent} from './deployment.component';
import {AuthGuard} from "@naikan/shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: DeploymentComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class DeploymentRoutingModule {
}
