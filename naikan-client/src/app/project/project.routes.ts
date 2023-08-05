import {Routes} from "@angular/router";
import {AuthGuard} from "@naikan/shared";
import {ProjectComponent} from "./project.component";
import {ProjectDetailComponent} from "./detail/project-detail.component";

export const PROJECT_ROUTES: Routes = [
  {
    path: '',
    component: ProjectComponent,
    canActivate: [AuthGuard]
  },
  {
    path: '',
    children: [
      {
        path: ':id',
        component: ProjectDetailComponent
      }
    ]
  }
];