import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {TeamComponent} from './team.component';
import {AuthGuard} from "../../shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: TeamComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class TeamRoutingModule {
}
