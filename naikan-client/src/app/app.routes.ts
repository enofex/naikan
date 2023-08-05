import {Routes} from '@angular/router';
import {AppLayoutComponent} from '@naikan/layout/app.layout.component';
import {AuthGuard} from "@naikan/shared";

export const APP_ROUTES: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./login').then(m => m.LOGIN_ROUTES)
  },
  {
    path: '',
    component: AppLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'projects',
        loadChildren: () => import('./project').then(m => m.PROJECT_ROUTES)
      },
      {
        path: 'overview',
        loadChildren: () => import('./overview').then(m => m.OVERVIEW_ROUTES)
      },
      {
        path: 'administration',
        loadChildren: () => import('./administration').then(m => m.ADMINISTRATION_ROUTES)
      },
      {
        path: 'error',
        loadChildren: () => import('./error').then(m => m.ERROR_ROUTES)
      },
      {path: '', redirectTo: '/projects', pathMatch: "full"},
      {path: '**', redirectTo: '/error/404'}
    ]
  }
];
