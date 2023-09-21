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
            <i (click)="onFavoriteToggle(bomOverview.id)" [ngClass]="isFavorite(bomOverview.id) 
            ? 'pi pi-star-fill mt-2 mr-2 text-primary-400'
            : 'pi pi-star mt-2 mr-2 text-primary-400'"></i>
            <span tooltipPosition="top"
                  [escape]="false"
                  pTooltip="{{tooltipProject(bomOverview)}}">
              <a routerLink="./{{bomOverview.id}}">{{ bomOverview.project?.name }}</a>
            </span>
            <naikan-project-url-icon [url]="bomOverview.project?.url"></naikan-project-url-icon>
          </div>

          <span class="text-500" *ngIf="bomOverview.tags && bomOverview.tags.length > 0">
            <i class="pi pi-tag mt-2 mr-2"></i>
            <naikan-tags [tags]="bomOverview.tags"></naikan-tags>
          </span>
        </div>

        <div class="flex flex-column align-items-end sm:align-items-start">
          <naikan-project-version [project]="bomOverview.project"
                                  class="flex sm:align-self-end align-self-start">
          </naikan-project-version>

          <div *ngIf="bomOverview.timestamp" class="align-self-start sm:align-self-end mt-2">
            <span class="font-normal text-500 text-sm"> last updated
              <span class="text-700">{{ bomOverview.timestamp | naikanDateTime }}</span>
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
                    pTooltip="{{ tooltipNames(bomOverview.environments) }}">
                {{ bomOverview.environments ? bomOverview.environments.length : 0}}
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
                    pTooltip="{{ tooltipNames(bomOverview.teams) }}">
                {{ bomOverview.teams ? bomOverview.teams.length : 0}}
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
                    pTooltip="{{ tooltipNames(bomOverview.developers) }}">
                {{ bomOverview.developers ? bomOverview.developers.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.contacts) }}">
                {{ bomOverview.contacts ? bomOverview.contacts.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.integrations) }}">
                {{ bomOverview.integrations ? bomOverview.integrations.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.technologies) }}">
                {{ bomOverview.technologies ? bomOverview.technologies.length : 0 }}
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
                {{ bomOverview.deploymentsCount }} 
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="flex flex-column sm:flex-row sm:align-items-start pt-4"
           *ngIf="this.bomOverview.deploymentsCount > 0">
        <div
            class="flex flex-column sm:flex-row justify-content-between align-items-center sm:align-items-start flex-1">

          <div class="flex flex-column align-items-start">
            <span class="flex text-500">
              <span class="text-500 text-sm text-overflow-ellipsis mt-2">
                Found deployments on 
                <span class="text-700">
                  {{ bomOverview.deploymentsEnvironmentsCount}}
                </span> environments and
                <span class="text-700">
                  {{ bomOverview.deploymentsVersionsCount}}
                </span> versions.
              </span>
            </span>

            <span class="flex text-500" *ngIf="bomOverview.lastDeployment">
              <span class="text-500 text-sm text-overflow-ellipsis mt-2">
                Last deployment on
                <span class="text-700">
                  {{ bomOverview.lastDeployment.environment }}
                </span> with version
                <span class="text-700">
                  {{ bomOverview.lastDeployment.version }}
                </span> on
                <span class="text-700">
                  {{ bomOverview.lastDeployment.timestamp | naikanDateTime }}
                </span>
              </span>
            </span>
          </div>

          <div class="flex flex-column align-items-end">
            <p-chart type="line" height="50px"
                     [data]="projectDeploymentsChart()"
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

  projectDeploymentsChart(): any {
    return {
      labels: this.bomOverview.deploymentsPerMonth?.months,
      datasets: [
        {
          data: this.bomOverview.deploymentsPerMonth?.counts,
          fill: true,
          backgroundColor: Charts.documentStyleWithDefaultOpacity(),
          borderColor: Charts.documentStyle(),
          borderWidth: 1,
          pointStyle: false
        }
      ]
    };
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