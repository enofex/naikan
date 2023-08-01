import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {TechnologyComponent} from './technology.component';
import {AuthGuard} from "@naikan/shared";

@NgModule({
  imports: [RouterModule.forChild([{
    path: '',
    component: TechnologyComponent,
    canActivate: [AuthGuard]
  }])],
  exports: [RouterModule]
})
export class TechnologyRoutingModule {
}
