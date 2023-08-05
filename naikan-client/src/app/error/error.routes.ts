import {Routes} from "@angular/router";
import {AuthGuard} from "@naikan/shared";
import {Error403Component} from "./403/403.component";
import {Error500Component} from "./500/500.component";
import {Error404Component} from "./404/404.component";

export const ERROR_ROUTES: Routes = [
  {
    path: '403',
    component: Error403Component,
    canActivate: [AuthGuard]
  },
  {
    path: '404',
    component: Error404Component,
    canActivate: [AuthGuard]
  },
  {
    path: '500',
    component: Error500Component,
    canActivate: [AuthGuard]
  }
];