import {Routes} from "@angular/router";
import {AuthGuard} from "@naikan/shared";
import {UserComponent} from "./user/user.component";
import {TokenComponent} from "./token/token.component";
import {AdministrationComponent} from "./administration.component";
import {ProjectComponent} from "./project/project.component";
import {ProfileComponent} from "./profile/profile.component";


export const ADMINISTRATION_ROUTES: Routes = [
  {
    path: '',
    component: AdministrationComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'users',
    component: UserComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'tokens',
    component: TokenComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'projects',
    component: ProjectComponent,
    canActivate: [AuthGuard],
    data: {
      authorities: ['ROLE_ADMIN']
    }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard],
  }
];