import {Route} from '@angular/router';
import {ProjectDetailComponent} from './project-detail.component';

export const DETAIL_ROUTE: Route = {
  path: '',
  children: [
    {
      path: ':id',
      component: ProjectDetailComponent
    }
  ]
};
