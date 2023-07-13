import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';
import {TooltipModule} from 'primeng/tooltip';
import {RouterLink} from '@angular/router';
import {TableModule} from 'primeng/table';
import {DateTimePipe, ProjectUrlIcon, ProjectVersion, Url} from '../shared';
import {SharedModule as SharedModule_1} from 'primeng/api';
import {OverviewBom} from "./overview";

@Component({
  selector: 'naikan-overview-project-table',
  template: `
    <p-table [value]="overviewBoms" dataKey="id">
      <ng-template pTemplate="header">
        <tr>
          <th pSortableColumn="project.name">Name
            <p-sortIcon field="project.name"></p-sortIcon>
          </th>
          <th pSortableColumn="project.repository">Repository
            <p-sortIcon field="project.repository"></p-sortIcon>
          </th>
          <th pSortableColumn="timestamp">Last updated
            <p-sortIcon field="timestamp"></p-sortIcon>
          </th>
          <th class="w-10rem" pSortableColumn="project.version">Version
            <p-sortIcon field="project.version"></p-sortIcon>
          </th>
        </tr>
      </ng-template>
      <ng-template pTemplate="body" let-bom>
        <tr>
          <td>
            <span tooltipPosition="top" pTooltip="{{ bom.project?.description }}">
              <a routerLink="/projects/{{bom.id}}">{{ bom.project?.name }}</a>
            </span>

            <naikan-project-url-icon [url]="bom.project?.url"></naikan-project-url-icon>
          </td>
          <td>
            <naikan-url [url]="bom.project?.repository"></naikan-url>
          </td>
          <td>
            {{ bom.timestamp | naikanDateTime }}
          </td>
          <td>
            <naikan-project-version [project]="bom.project" class="ml-1"></naikan-project-version>
          </td>
        </tr>
      </ng-template>
    </p-table>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TableModule,
    SharedModule_1,
    TooltipModule,
    RouterLink,
    ProjectUrlIcon,
    Url,
    ProjectVersion,
    DateTimePipe,
  ],
})
export class OverviewProjectTable {
  @Input() overviewBoms: OverviewBom[];
}