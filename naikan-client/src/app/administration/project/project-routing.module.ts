import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AuthGuard} from "../../shared";
import {ProjectComponent} from "./project.component";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: ProjectComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  }])],
  exports: [RouterModule]
})
export class ProjectRoutingModule {
}
