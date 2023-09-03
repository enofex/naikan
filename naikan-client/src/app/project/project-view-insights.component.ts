import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnDestroy,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {NgClass, NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {
  Bom,
  DateTimePipe,
  Deployment,
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
import {TabViewModule} from "primeng/tabview";
import {Subscription} from "rxjs";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {DeploymentsChart} from "./deployments-chart";

@Component({template: ''})
export abstract class AbstractInsightChart implements OnDestroy {

  private _boms: Bom[];
  subscription!: Subscription;

  @Input()
  set boms(boms: Bom[]) {
    this._boms = boms;
    this.initChart();
  }

  protected constructor(readonly layoutService: LayoutService) {
    this.subscription = this.layoutService.configUpdate$.subscribe(() => {
      this.initChart();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  allBoms(): Bom[] {
    return this._boms;
  }

  abstract initChart(): void;
}

@Component({
  selector: 'naikan-project-view-insights-summarization-chart',
  template: `
    <p-chart type="bar" height="100%" width="100%"
             #chartSummarizationRef
             [data]="chartSummarization.data"
             [options]="chartSummarization.options">
    </p-chart>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
    ChartModule,
  ],
})
export class SummarizationChart extends AbstractInsightChart {

  @ViewChild('chartSummarizationRef') chartSummarizationRef: UIChart;

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

  constructor(override readonly layoutService: LayoutService) {
    super(layoutService);
  }

  override initChart(): void {
    if (!this.allBoms()) {
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

    this.chartSummarization.data.labels = this.allBoms().map(bom => bom.project.name);
    this.chartSummarization.data.datasets = dataProperties.map(propData => ({
      label: propData.label,
      data: this.allBoms().map(bom => bom[propData.prop] ? bom[propData.prop].length : 0)
    }));

    if (this.chartSummarizationRef?.chart) {
      this.chartSummarizationRef.chart.update();
    }
  }
}

@Component({
  selector: 'naikan-project-view-insights-technologies-chart',
  template: `
    <p-chart type="bar" height="100%" width="100%"
             #chartTechnologiesRef
             [data]="chartTechnologies.data"
             [options]="chartTechnologies.options">
    </p-chart>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
    ChartModule,
  ],
})
export class TechnologiesChart extends AbstractInsightChart {

  @ViewChild('chartTechnologiesRef') chartTechnologiesRef: UIChart;

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

  constructor(override readonly layoutService: LayoutService) {
    super(layoutService);
  }

  override initChart(): void {
    if (!this.allBoms()) {
      return;
    }

    const technologyCounts = new Map<string, number>();

    this.allBoms().forEach(bom => bom.technologies.forEach((tech) => {
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
}


@Component({
  selector: 'naikan-project-view-insights-deployments-summarization-chart',
  template: `
    <p-chart type="bar" height="100%" width="100%"
             #chartDeploymentsSummarizationRef
             [data]="chartDeploymentsSummarization.data"
             [options]="chartDeploymentsSummarization.options">
    </p-chart>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
    ChartModule,
  ],
})
export class DeploymentsSummarizationChart extends AbstractInsightChart {

  @ViewChild('chartDeploymentsSummarizationRef') chartDeploymentsSummarizationRef: UIChart;

  chartDeploymentsSummarization = {
    options: {
      plugins: {
        legend: {
          display: true
        },
        title: {
          text: '',
          display: true
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

  constructor(override readonly layoutService: LayoutService) {
    super(layoutService);
  }

  override initChart(): void {
    if (!this.allBoms()) {
      return;
    }

    const months = 24;
    const dataProperties = [
      {label: "Unique environments", prop: "environment"},
      {label: "Unique locations", prop: "location"},
      {label: "Unique versions", prop: "version"},
    ];

    const currentDate = new Date();
    const twentyFourMonthsAgo = new Date();
    twentyFourMonthsAgo.setMonth(currentDate.getMonth() - months);

    this.chartDeploymentsSummarization.options.plugins.title.text = `Last ${months} months`;
    this.chartDeploymentsSummarization.data.labels = this.allBoms().map(bom => bom.project.name);
    this.chartDeploymentsSummarization.data.datasets = dataProperties.map(propData => ({
      label: propData.label,
      data: this.allBoms().map((bom) => {
        const filteredDeployments = bom.deployments.filter(deployment => {
          const deploymentTimestamp = new Date(deployment.timestamp);
          return deploymentTimestamp >= twentyFourMonthsAgo && deploymentTimestamp <= currentDate;
        });
        return new Set(filteredDeployments.map(deployment => deployment[propData.prop])).size;
      })
    }));

    if (this.chartDeploymentsSummarizationRef?.chart) {
      this.chartDeploymentsSummarizationRef.chart.update();
    }
  }
}

@Component({
  selector: '.naikan-project-view-insights-header',
  template: `
    <p-tabView styleClass="ml-2">

      <p-tabPanel header="Summarization">
        <div class="chart-panel">
          <naikan-project-view-insights-summarization-chart [boms]="allBoms()">
          </naikan-project-view-insights-summarization-chart>
        </div>
      </p-tabPanel>

      <p-tabPanel header="Technologies">
        <div class="chart-panel">
          <naikan-project-view-insights-technologies-chart [boms]="allBoms()">
          </naikan-project-view-insights-technologies-chart>
        </div>
      </p-tabPanel>

      <p-tabPanel header="Deployments">
        <div class="chart-panel">
          <naikan-deployments-chart
              [months]="24"
              [deployments]="allDeployments()">
          </naikan-deployments-chart>
        </div>

        <div class="chart-panel mt-8">
          <naikan-project-view-insights-deployments-summarization-chart [boms]="allBoms()">
          </naikan-project-view-insights-deployments-summarization-chart>
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
    TabViewModule,
    DeploymentsChart,
    SummarizationChart,
    TechnologiesChart,
    DeploymentsSummarizationChart
  ],
})
export class ProjectViewInsightsHeader extends AbstractProjectView {

  private _boms: Bom[];

  @Input()
  set boms(boms: Bom[]) {
    this._boms = boms;
  }

  constructor(projectService: ProjectService, principal: Principal) {
    super(projectService, principal)
  }

  allBoms(): Bom[] {
    if (!this._boms) {
      return [];
    }

    return this._boms;
  }

  allDeployments(): Deployment[] {
    if (!this._boms) {
      return [];
    }

    return this._boms.flatMap((bom) =>
        bom.deployments.map((deployment) => deployment)
    );
  }
}