import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {IntegrationComponent} from './integration.component';
import {AuthGuard} from "../../shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: IntegrationComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class IntegrationRoutingModule {
}
