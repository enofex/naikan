import {Routes} from '@angular/router';
import {AppLayoutComponent} from './layout/app.layout.component';
import {AuthGuard} from "./shared";

export const APP_ROUTES: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./login/login-routing.module').then(m => m.LoginRoutingModule)
  },
  {
    path: '', component: AppLayoutComponent, canActivate: [AuthGuard],
    children: [
      {
        path: 'projects',
        loadChildren: () => import('./project/project-routing.module').then(m => m.ProjectRoutingModule)
      },
      {
        path: 'overview',
        loadChildren: () => import('./overview/overview.routing.module').then(m => m.OverviewRoutingModule)
      },
      {
        path: 'overview/environments',
        loadChildren: () => import('./overview/environment/environment-routing.module').then(m => m.EnvironmentRoutingModule)
      },
      {
        path: 'overview/teams',
        loadChildren: () => import('./overview/team/team-routing.module').then(m => m.TeamRoutingModule)
      },
      {
        path: 'overview/developers',
        loadChildren: () => import('./overview/developer/developer-routing.module').then(m => m.DeveloperRoutingModule)
      },
      {
        path: 'overview/contacts',
        loadChildren: () => import('./overview/contact/contact-routing.module').then(m => m.ContactRoutingModule)
      },
      {
        path: 'overview/integrations',
        loadChildren: () => import('./overview/integration/integration-routing.module').then(m => m.IntegrationRoutingModule)
      },
      {
        path: 'overview/technologies',
        loadChildren: () => import('./overview/technology/technology-routing.module').then(m => m.TechnologyRoutingModule)
      },
      {
        path: 'overview/deployments',
        loadChildren: () => import('./overview/deployment/deployment-routing.module').then(m => m.DeploymentRoutingModule)
      },
      {
        path: 'administration',
        loadChildren: () => import('./administration/administration.routing.module').then(m => m.AdministrationRoutingModule)
      },
      {
        path: 'administration/profile',
        loadChildren: () => import('./administration/profile/profile.routing.module').then(m => m.ProfileRoutingModule)
      },
      {
        path: 'administration/users',
        loadChildren: () => import('./administration/user/user.routing.module').then(m => m.UserRoutingModule)
      },
      {
        path: 'administration/projects',
        loadChildren: () => import('./administration/project/project-routing.module').then(m => m.ProjectRoutingModule)
      },
      {
        path: 'administration/tokens',
        loadChildren: () => import('./administration/token/token.routing.module').then(m => m.TokenRoutingModule)
      },
      {
        path: 'error/403',
        loadChildren: () => import('./error/403/403-routing.module').then(m => m.Error403RoutingModule)
      },
      {
        path: 'error/404',
        loadChildren: () => import('./error/404/404-routing.module').then(m => m.Error404RoutingModule)
      },
      {
        path: 'error/500',
        loadChildren: () => import('./error/500/500-routing.module').then(m => m.Error500RoutingModule)
      },
      {path: '', redirectTo: '/projects', pathMatch: "full"},
      {path: '**', redirectTo: '/error/404'}
    ]
  }
];
