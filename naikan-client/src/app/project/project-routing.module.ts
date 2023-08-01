import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ProjectComponent} from './project.component';
import {DETAIL_ROUTE} from './detail/project-detail.route';
import {AuthGuard} from "@naikan/shared";

@NgModule({
  imports: [
    RouterModule.forChild([{
      path: '',
      component: ProjectComponent,
      canActivate: [AuthGuard],
    },
      DETAIL_ROUTE
    ])],
  exports: [RouterModule]
})
export class ProjectRoutingModule {
}
