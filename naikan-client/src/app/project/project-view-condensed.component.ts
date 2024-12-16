import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {NgClass} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {
  DateTimePipe,
  Principal,
  ProjectUrlIcon,
  ProjectVersion
} from "@naikan/shared";
import {ChartModule} from "primeng/chart";
import {RouterLink} from "@angular/router";
import {ProjectService} from "./project.service";
import {AbstractProjectView} from "./abstract-project-view.component";
import {SharedModule} from "primeng/api";

@Component({
  selector: '.naikan-project-view-condensed-header',
  template: `
    <th>Name</th>
    <th>Environments</th>
    <th>Teams</th>
    <th>Developers</th>
    <th>Contacts</th>
    <th>Integrations</th>
    <th>Technologies</th>
    <th>Deployments</th>
    <th>Commits</th>
    <th>Last updated</th>
    <th>Version</th>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class ProjectViewCondensedHeader {
}

@Component({
    selector: '.naikan-project-view-condensed-body',
    template: `
    <td>
      <i (click)="onFavoriteToggle(bomOverview.id)" [ngClass]="isFavorite(bomOverview.id) 
            ? 'pi pi-star-fill mt-2 mr-2 text-primary-400'
            : 'pi pi-star mt-2 mr-2 text-primary-400'"></i>
      <span tooltipPosition="top"
            [escape]="false"
            pTooltip="{{tooltipProject(bomOverview)}}">
              <a routerLink="./{{bomOverview.id}}">{{ bomOverview.project?.name }}</a>
            </span>
      <naikan-project-url-icon [url]="bomOverview.project?.url"></naikan-project-url-icon>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.environmentNames) }}">
        {{ bomOverview.environmentNames ? bomOverview.environmentNames.length : 0}}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.teamNames) }}">
        {{ bomOverview.teamNames ? bomOverview.teamNames.length : 0}}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.developerNames) }}">
        {{ bomOverview.developerNames ? bomOverview.developerNames.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.contactNames) }}">
        {{ bomOverview.contactNames ? bomOverview.contactNames.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.integrationNames) }}">
        {{ bomOverview.integrationNames ? bomOverview.integrationNames.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.technologyNames) }}">
        {{ bomOverview.technologyNames ? bomOverview.technologyNames.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"> 
        {{ bomOverview.deploymentsCount }} 
      </span>
    </td>

    <td>
      <span class="text-surface-900 font-medium text-lg"> 
        {{ bomOverview.commitsCount }} 
      </span>
    </td>

    <td>
      {{ bomOverview.timestamp | naikanDateTime }}
    </td>

    <td>
      <naikan-project-version [project]="bomOverview.project" class="ml-1"></naikan-project-version>
    </td>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
  imports: [
    TooltipModule,
    ChartModule,
    DateTimePipe,
    NgClass,
    SharedModule,
    ProjectVersion,
    ProjectUrlIcon,
    RouterLink
  ]
})
export class ProjectViewCondensedBody extends AbstractProjectView implements OnInit {

  constructor(
      projectService: ProjectService,
      principal: Principal) {

    super(projectService, principal)
  }
}