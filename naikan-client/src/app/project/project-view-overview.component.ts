import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  ViewEncapsulation
} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {
  Charts,
  DateTimePipe,
  Deployment,
  NaikanTags,
  Principal,
  ProjectUrlIcon,
  ProjectVersion
} from "@naikan/shared";
import {ChartModule} from "primeng/chart";
import {RouterLink} from "@angular/router";
import {ProjectService} from "./project.service";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {Subscription} from "rxjs";
import {AbstractProjectView} from "./abstract-project-view.component";
import {SharedModule} from "primeng/api";

@Component({
  selector: '.naikan-project-view-overview-body',
  template: `

    <div class="card mb-2">
      <div class="flex flex-column justify-content-between sm:flex-row sm:align-items-start p-4">

        <div class="flex flex-column align-items-start">
          <div class="text-xl font-bold text-900">
            <i (click)="onFavoriteToggle(bom.id)" [ngClass]="isFavorite(bom.id) 
            ? 'pi pi-star-fill mt-2 mr-2 text-primary-400'
            : 'pi pi-star mt-2 mr-2 text-primary-400'"></i>
            <span tooltipPosition="top"
                  [escape]="false"
                  pTooltip="{{tooltipProject(bom)}}">
              <a routerLink="./{{bom.id}}">{{ bom.project?.name }}</a>
            </span>
            <naikan-project-url-icon [url]="bom.project?.url"></naikan-project-url-icon>
          </div>

          <span class="text-500" *ngIf="bom.tags && bom.tags.length > 0">
            <i class="pi pi-tag mt-2 mr-2"></i>
            <naikan-tags [tags]="bom.tags"></naikan-tags>
          </span>
        </div>

        <div class="flex flex-column align-items-end sm:align-items-start">
          <naikan-project-version [project]="bom.project"
                                  class="flex sm:align-self-end align-self-start">
          </naikan-project-version>

          <div *ngIf="bom.timestamp" class="align-self-start sm:align-self-end mt-2">
            <span class="font-normal text-500 text-sm"> last updated
              <span class="text-700">{{ bom.timestamp | naikanDateTime }}</span>
            </span>
          </div>
        </div>

      </div>

      <div class="flex flex-column justify-content-between surface-50 sm:flex-row">
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-box m-1"></i> Environments
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.environments) }}">
                {{ bom.environments ? bom.environments.length : 0}}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-users m-1"></i> Teams
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.teams) }}">
                {{ bom.teams ? bom.teams.length : 0}}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-user m-1"></i> Developers
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.developers) }}">
                {{ bom.developers ? bom.developers.length : 0 }}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-envelope m-1"></i> Contacts
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.contacts) }}">
                {{ bom.contacts ? bom.contacts.length : 0 }}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-link m-1"></i> Integrations
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.integrations) }}">
                {{ bom.integrations ? bom.integrations.length : 0 }}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-code m-1"></i> Technologies
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top"
                    pTooltip="{{ tooltipNames(bom.technologies) }}">
                {{ bom.technologies ? bom.technologies.length : 0 }}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-cloud-upload m-1"></i>  Deployments
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"> 
                {{ bom.deployments ? bom.deployments.length : 0 }} 
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="flex flex-column sm:flex-row sm:align-items-start pt-4"
           *ngIf="hasDeployments(bom.deployments)">
        <div
            class="flex flex-column sm:flex-row justify-content-between align-items-center sm:align-items-start flex-1">

          <div class="flex flex-column align-items-start">
            <span class="flex text-500">
              <span class="text-500 text-sm text-overflow-ellipsis mt-2">
                Found deployments on 
                <span class="text-700">
                  {{ distinctEnvironmentsCount(bom.deployments) }}
                </span> environments and
                <span class="text-700">
                  {{ distinctVersionsCount(bom.deployments) }}
                </span> versions.
              </span>
            </span>

            <span class="flex text-500">
              <span class="text-500 text-sm text-overflow-ellipsis mt-2">
                Last deployment on
                <span class="text-700">
                  {{ latestEnvironment(bom.deployments) }}
                </span> with version
                <span class="text-700">
                  {{ latestVersion(bom.deployments) }}
                </span> on
                <span class="text-700">
                  {{ latestTimestamp(bom.deployments) | naikanDateTime }}
                </span>
              </span>
            </span>
          </div>

          <div class="flex flex-column align-items-end">
            <p-chart type="line" height="50px"
                     [data]="projectDeploymentsChart(bom.deployments)"
                     [options]="chartOptions">
            </p-chart>
          </div>
        </div>
      </div>
    </div>
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
    SharedModule,
  ],
})
export class ProjectViewOverviewBody extends AbstractProjectView implements OnInit, OnDestroy {

  chartOptions: any;
  subscription!: Subscription;

  constructor(
      projectService: ProjectService,
      principal: Principal,
      private readonly layoutService: LayoutService) {

    super(projectService, principal)

    this.subscription = this.layoutService.configUpdate$.subscribe(() => {
      this.initChart();
    });
  }

  override ngOnInit(): void {
    super.ngOnInit();
    this.initChart()
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  projectDeploymentsChart(deployments: Deployment[]): any {
    if (this.hasDeployments(this.bom.deployments)) {
      const documentStyle = Charts.documentStyle();
      const documentStyleWithDefaultOpacity = Charts.documentStyleWithDefaultOpacity();

      const countsByMonth = deployments.reduce((acc, deployment) => {
        if (deployment.timestamp) {
          const month = new Date(deployment.timestamp).toISOString().substring(0, 7);
          acc[month] = acc[month] ? acc[month] + 1 : 1;
        }
        return acc;
      }, {});

      return {
        labels: Object.keys(countsByMonth),
        datasets: [
          {
            label: 'Projects',
            data: Object.values(countsByMonth),
            fill: true,
            backgroundColor: documentStyleWithDefaultOpacity,
            borderColor: documentStyle,
            borderWidth: 1,
            pointStyle: false
          }
        ]
      };
    }

    return {};
  }

  latestEnvironment(deployments: Deployment[]): string {
    if (this.hasDeployments(deployments)) {
      const environment = deployments[deployments.length - 1].environment;
      return environment ? environment : 'unknown';
    }

    return 'unknown';
  }

  latestVersion(deployments: Deployment[]): string {
    if (this.hasDeployments(deployments)) {
      const version = deployments[deployments.length - 1].version;
      return version ? version : 'unknown';
    }

    return 'unknown';
  }

  latestTimestamp(deployments: Deployment[]): Date {
    if (this.hasDeployments(deployments)) {
      return deployments[deployments.length - 1].timestamp;
    }

    return undefined;
  }

  distinctVersionsCount(deployments: Deployment[]): number {
    if (this.hasDeployments(deployments)) {
      return new Set(deployments.map(deployment => deployment.version)).size;
    }

    return 0;
  }

  distinctEnvironmentsCount(deployments: Deployment[]): number {
    if (this.hasDeployments(deployments)) {
      return new Set(deployments.map(deployment => deployment.environment)).size;
    }

    return 0;
  }

  hasDeployments(deployments: Deployment[]): boolean {
    return deployments && deployments.length > 0;
  }

  private initChart(): void {
    this.chartOptions = {
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        x: {
          display: false
        },
        y: {
          display: true,
          ticks: {
            display: false
          }
        },
      }
    };
  }
}