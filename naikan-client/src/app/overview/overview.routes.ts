import {Routes} from "@angular/router";
import {AuthGuard} from "@naikan/shared";
import {ContactComponent} from "./contact/contact.component";
import {DeploymentComponent} from "./deployment/deployment.component";
import {DeveloperComponent} from "./developer/developer.component";
import {EnvironmentComponent} from "./environment/environment.component";
import {IntegrationComponent} from "./integration/integration.component";
import {TagComponent} from "./tag/tag.component";
import {TeamComponent} from "./team/team.component";
import {TechnologyComponent} from "./technology/technology.component";
import {OverviewComponent} from "./overview.component";

export const OVERVIEW_ROUTES: Routes = [
  {
    path: '',
    component: OverviewComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'contacts',
    component: ContactComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'deployments',
    component: DeploymentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'developers',
    component: DeveloperComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'environments',
    component: EnvironmentComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'integrations',
    component: IntegrationComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'tags',
    component: TagComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'teams',
    component: TeamComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'technologies',
    component: TechnologyComponent,
    canActivate: [AuthGuard]
  }
];