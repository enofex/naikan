import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {
  Bom,
  Charts,
  DateTimePipe,
  NaikanTags,
  Principal,
  ProjectUrlIcon,
  ProjectVersion,
  Url,
} from "@naikan/shared";
import {ChartModule, UIChart} from "primeng/chart";
import {RouterLink} from "@angular/router";
import {ProjectService} from "./project.service";
import {AbstractProjectView} from "./abstract-project-view.component";
import {SharedModule} from "primeng/api";
import {TabViewChangeEvent, TabViewModule} from "primeng/tabview";
import {Subscription} from "rxjs";
import {LayoutService} from "@naikan/layout/app.layout.service";

@Component({
  selector: '.naikan-project-view-insights-header',
  template: `
    <p-tabView styleClass="ml-2" (onChange)="onChange($event)">

      <p-tabPanel header="Summarization">
        <div class="chart-panel">
          <p-chart type="bar" height="100%" width="100%"
                   #chartSummarizationRef
                   [data]="chartSummarization.data"
                   [options]="chartSummarization.options">
          </p-chart>
        </div>
      </p-tabPanel>

      <p-tabPanel header="Technologies">
        <div class="chart-panel">
          <p-chart type="bar" height="100%" width="100%"
                   #chartTechnologiesRef
                   [data]="chartTechnologies.data"
                   [options]="chartTechnologies.options">
          </p-chart>
        </div>
      </p-tabPanel>

      <p-tabPanel header="Deployments">
        <div class="chart-panel">
          <p-chart type="line" height="100%" width="100%"
                   #chartDeploymentsRef
                   [data]="chartDeployments.data"
                   [options]="chartDeployments.options">
          </p-chart>
        </div>
      </p-tabPanel>
    </p-tabView>
  `,
  styles: ['.chart-panel {height: 50vh; position: relative; overflow-y: auto; overflow-x: auto; margin-top: 20px;}'],
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
    SharedModule,
    TabViewModule
  ],
})
export class ProjectViewInsightsHeader extends AbstractProjectView implements OnDestroy, OnInit {

  private static readonly LAST_MONTHS = 24;

  private _boms: Bom[];
  private activeIndex = 0;
  subscription!: Subscription;

  @ViewChild('chartSummarizationRef') chartSummarizationRef: UIChart;
  @ViewChild('chartTechnologiesRef') chartTechnologiesRef: UIChart;
  @ViewChild('chartDeploymentsRef') chartDeploymentsRef: UIChart;

  @Input() set boms(boms: Bom[]) {
    this._boms = boms;
    this.initCharts();
  }

  chartSummarization = {
    options: {
      plugins: {
        legend: {
          display: true
        },
        title: {
          display: false
        }
      },
      indexAxis: 'y',
      scales: {
        x: {
          stacked: true,
        },
        y: {
          stacked: true
        }
      }
    },
    data: {} as any
  };

  chartTechnologies = {
    options: {
      plugins: {
        legend: {
          display: false
        },
        title: {
          display: false
        }
      },
      indexAxis: 'y'
    },
    data: {} as any
  };

  chartDeployments = {
    options: {
      y: {
        display: true,
        ticks: {
          display: false
        }
      },
      scale: {
        ticks: {
          precision: 0
        }
      },
      plugins: {
        legend: {
          display: true
        },
        title: {
          display: true,
          text: `Last ${ProjectViewInsightsHeader.LAST_MONTHS} months`
        }
      }
    },
    data: {} as any
  };

  constructor(
      private readonly layoutService: LayoutService,
      projectService: ProjectService,
      principal: Principal) {

    super(projectService, principal)

    this.subscription = this.layoutService.configUpdate$.subscribe(() => {
      this.initCharts();
    });
  }

  onChange(event: TabViewChangeEvent): void {
    this.activeIndex = event.index;
    this.initCharts();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private initSummarizationChart(): void {
    if (!this._boms) {
      return;
    }

    const dataProperties = [
      {label: "Environments", prop: "environments"},
      {label: "Teams", prop: "teams"},
      {label: "Developers", prop: "developers"},
      {label: "Contacts", prop: "contacts"},
      {label: "Integrations", prop: "integrations"},
      {label: "Technologies", prop: "technologies"},
      {label: "Deployments", prop: "deployments"},
      {label: "Tags", prop: "tags"}
    ];

    this.chartSummarization.data.labels = this._boms.map(bom => bom.project.name);
    this.chartSummarization.data.datasets = dataProperties.map(propData => ({
      label: propData.label,
      data: this._boms.map(bom => bom[propData.prop] ? bom[propData.prop].length : 0)
    }));

    if (this.chartSummarizationRef?.chart) {
      this.chartSummarizationRef.chart.update();
    }
  }

  private initTechnologiesChart(): void {
    if (!this._boms) {
      return;
    }

    const technologyCounts = new Map<string, number>();

    this._boms.forEach(bom => bom.technologies.forEach((tech) => {
      const name = tech.name + (tech.version ? " " + tech.version : "");
      const count = technologyCounts.get(name) || 0;
      technologyCounts.set(name, count + 1);
    }));

    const sortedTechnologies = Array.from(
        technologyCounts,
        ([name, count]) => ({
          name,
          count
        })
    ).sort((a, b) => b.count - a.count);

    this.chartTechnologies.data.labels = sortedTechnologies.map(tech => tech.name);
    this.chartTechnologies.data.datasets = [{
      data: sortedTechnologies.map(tech => tech.count),
    }];

    if (this.chartTechnologiesRef?.chart) {
      this.chartTechnologiesRef.chart.update();
    }
  }

  private initDeploymentsChart(): void {
    if (!this._boms) {
      return;
    }

    const deploymentCounts = new Map<string, number>();
    const uniqueLocations = new Map<string, Set<string>>();
    const today = new Date();
    const since = new Date(today.getFullYear(), today.getMonth() - ProjectViewInsightsHeader.LAST_MONTHS + 1, 1);
    const to = new Date(today.getFullYear(), today.getMonth() + 1, 1);

    for (let yearMonth = new Date(since); yearMonth <= to; yearMonth.setMonth(yearMonth.getMonth() + 1)) {
      const period = yearMonth.toISOString().substring(0, 7);
      deploymentCounts.set(period, 0);
      uniqueLocations.set(period, new Set<string>());
    }

    let deployments = 0;
    this._boms.forEach(bom => bom.deployments.forEach((deployment) => {
      if (deployment.timestamp) {
        const timestamp = new Date(deployment.timestamp);

        if (timestamp >= since) {
          deployments++;
          const yearMonth = timestamp.toISOString().substring(0, 7)
          const count = deploymentCounts.get(yearMonth) || 0;
          deploymentCounts.set(yearMonth, count + 1);

          const locationSet = uniqueLocations.get(yearMonth);
          if (locationSet) {
            locationSet.add(deployment.location);
          }
        }
      }
    }));

    const sortedDeployments = Array.from(
        deploymentCounts,
        ([monthYear, count]) => ({
          monthYear,
          count
        })).sort((a, b) => {
      return new Date(a.monthYear).getTime() - new Date(b.monthYear).getTime();
    });

    this.chartDeployments.data.labels = sortedDeployments.map(deployment => deployment.monthYear);
    this.chartDeployments.data.datasets = [
      {
        label: 'Unique deployment locations',
        data: Array.from(uniqueLocations.values()).map((locationSet) => locationSet.size),
        borderWidth: 1,
        fill: true,
        pointStyle: false,
        borderColor: Charts.documentStyle(),
      },
      {
        label: 'Average deployments',
        data: Array(sortedDeployments.length).fill(deployments / sortedDeployments.length),
        borderWidth: 1,
        fill: false,
        pointStyle: false,
        borderColor: Charts.documentStyle(),
        borderDash: [5, 5]
      },
      {
        label: 'Deployments',
        data: sortedDeployments.map(deployment => deployment.count),
        borderWidth: 1,
        fill: true,
        pointStyle: false,
        borderColor: Charts.documentStyle(),
        backgroundColor: Charts.documentStyleWithDefaultOpacity(),
      }];

    if (this.chartDeploymentsRef?.chart) {
      this.chartDeploymentsRef.chart.update();
    }
  }

  private initCharts(): void {
    if (this.activeIndex === 0) {
      this.initSummarizationChart();
    } else if (this.activeIndex === 1) {
      this.initTechnologiesChart();
    } else if (this.activeIndex === 2) {
      this.initDeploymentsChart();
    }
  }
}