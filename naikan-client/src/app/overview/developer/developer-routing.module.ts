import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {DeveloperComponent} from './developer.component';
import {AuthGuard} from "../../shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: DeveloperComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class DeveloperRoutingModule {
}
