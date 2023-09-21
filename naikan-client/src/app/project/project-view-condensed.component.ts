import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {
  DateTimePipe,
  NaikanTags,
  Principal,
  ProjectUrlIcon,
  ProjectVersion,
  Url,
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
    <th>Last updated</th>
    <th>Version</th>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
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
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.environments) }}">
        {{ bomOverview.environments ? bomOverview.environments.length : 0}}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.teams) }}">
        {{ bomOverview.teams ? bomOverview.teams.length : 0}}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.developers) }}">
        {{ bomOverview.developers ? bomOverview.developers.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.contacts) }}">
        {{ bomOverview.contacts ? bomOverview.contacts.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.integrations) }}">
        {{ bomOverview.integrations ? bomOverview.integrations.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"
            tooltipPosition="top"
            pTooltip="{{ tooltipNames(bomOverview.technologies) }}">
        {{ bomOverview.technologies ? bomOverview.technologies.length : 0 }}
      </span>
    </td>

    <td>
      <span class="text-900 font-medium text-lg"> 
        {{ bomOverview.deploymentsCount }} 
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
  standalone: true,
  imports: [
    TooltipModule,
    RouterLink,
    ProjectUrlIcon,
    NgIf,
    NaikanTags,
    ProjectVersion,
    ChartModule,
    DateTimePipe,
    NgClass,
    Url,
    SharedModule
  ],
})
export class ProjectViewCondensedBody extends AbstractProjectView implements OnInit {

  constructor(
      projectService: ProjectService,
      principal: Principal) {

    super(projectService, principal)
  }
}