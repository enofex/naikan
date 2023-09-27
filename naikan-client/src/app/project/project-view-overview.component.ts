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
  Charts, CommitId,
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
import {TagModule} from "primeng/tag";

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
          <div class="flex sm:align-self-end align-self-start align-items-center">
            <p-tag *ngIf="bomOverview.repository" tooltipPosition="top"
                   pTooltip="{{ bomOverview.repository.url }}"
                   class="mr-1">{{ bomOverview.repository.name }}
            </p-tag>
            <naikan-project-version [project]="bomOverview.project"
                                    class="flex">
            </naikan-project-version>
          </div>
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
                    pTooltip="{{ tooltipNames(bomOverview.environmentNames) }}">
                {{ bomOverview.environmentNames ? bomOverview.environmentNames.length : 0}}
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
                    pTooltip="{{ tooltipNames(bomOverview.teamNames) }}">
                {{ bomOverview.teamNames ? bomOverview.teamNames.length : 0}}
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
                    pTooltip="{{ tooltipNames(bomOverview.developerNames) }}">
                {{ bomOverview.developerNames ? bomOverview.developerNames.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.contactNames) }}">
                {{ bomOverview.contactNames ? bomOverview.contactNames.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.integrationNames) }}">
                {{ bomOverview.integrationNames ? bomOverview.integrationNames.length : 0 }}
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
                    pTooltip="{{ tooltipNames(bomOverview.technologyNames) }}">
                {{ bomOverview.technologyNames ? bomOverview.technologyNames.length : 0 }}
              </span>
            </div>
          </div>
        </div>
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3 sm:border-right-1 border-gray-300">
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
        <div
            class="sm:flex-row flex-1 flex align-items-center justify-content-center m-1 px-3 py-3">
          <div>
            <span class="block text-900 font-medium mb-3">
              <i class="pi pi-clock m-1"></i>  Commits
            </span>
            <div class="flex justify-content-center">
              <span class="text-900 font-medium text-lg"
                    tooltipPosition="top" 
                    pTooltip="Contributions to default branch, excluding merge commits"> 
                {{ bomOverview.commitsCount }} 
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
            <span class="flex text-500 pb-3" *ngIf="bomOverview.repository">
              <span class="text-500 text-sm text-overflow-ellipsis mt-2">
                <p-tag severity="success">First commit</p-tag>
                <naikan-commit-id commitId=" {{ bomOverview.repository?.firstCommit?.commitId }}"></naikan-commit-id>
                on
                <span class="text-700"
                      tooltipPosition="top"
                      pTooltip="{{ bomOverview.repository?.firstCommit?.author.name }}">
                  {{ bomOverview.repository?.firstCommit?.timestamp | naikanDateTime }}
                </span>
              </span>
            </span>
          
            <span class="flex text-500" *ngIf="this.bomOverview.deploymentsCount > 0">
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

          <div class="flex flex-column align-items-end"
               *ngIf="this.bomOverview.deploymentsPerMonth.counts.length >0 
               || this.bomOverview.commitsPerMonth.counts.length > 0">
            <p-chart type="line" height="80px" width="400px"
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
    TagModule,
    CommitId,
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
      datasets: [
        {
          label: "Deployments",
          data: this.bomOverview.deploymentsPerMonth.names.map((name, index) => ({
            x: name,
            y: this.bomOverview.deploymentsPerMonth.counts[index],
          })),
          fill: true,
          backgroundColor: Charts.documentPrimaryStyleWithDefaultOpacity(),
          borderColor: Charts.documentPrimaryStyle(),
          borderWidth: 1,
          pointStyle: false
        },
        {
          label: "Commits",
          data: this.bomOverview.commitsPerMonth.names.map((name, index) => ({
            x: name,
            y: this.bomOverview.commitsPerMonth.counts[index],
          })),
          fill: true,
          backgroundColor: Charts.documentGreenStyleWithDefaultOpacity(),
          borderColor: Charts.documentGreenStyle(),
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