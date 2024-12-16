import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnDestroy,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {TooltipModule} from 'primeng/tooltip';
import {
  Charts,
  Principal
} from "@naikan/shared";
import {ChartModule, UIChart} from "primeng/chart";
import {ProjectService} from "./project.service";
import {AbstractProjectView} from "./abstract-project-view.component";
import {SharedModule} from "primeng/api";
import {TabsModule} from "primeng/tabs";
import {finalize, Subscription} from "rxjs";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {BomOverview} from "./bom-overview";
import {Table} from "primeng/table";
import {CountsPerItems} from "./counts-per-items";

@Component({
    template: ''
})
export abstract class AbstractInsightChart implements OnDestroy {

  private _bomOverviews: BomOverview[];
  subscription!: Subscription;
  @Input() table!: Table;

  @Input()
  set bomOverviews(bomOverviews: BomOverview[]) {
    this._bomOverviews = bomOverviews;
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

  allOverviewBoms(): BomOverview[] {
    return this._bomOverviews;
  }

  abstract initChart(): void;
}

@Component({
    selector: 'naikan-project-view-insights-summarization-chart',
    template: `
    <p-chart type="bar"
             height="100%"
             width="100%"
             #chartSummarizationRef
             [data]="chartSummarization.data"
             [options]="chartSummarization.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
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
    if (!this.allOverviewBoms()) {
      return;
    }

    const dataProperties = [
      {label: "Environments", prop: "environmentNames"},
      {label: "Teams", prop: "teamNames"},
      {label: "Developers", prop: "developerNames"},
      {label: "Contacts", prop: "contactNames"},
      {label: "Integrations", prop: "integrationNames"},
      {label: "Technologies", prop: "technologyNames"},
      {label: "Deployments", prop: "deploymentsCount"},
      {label: "Tags", prop: "tags"},
      {label: "Commits", prop: "commitsCount"}
    ];

    this.chartSummarization.data.labels = this.allOverviewBoms().map(overviewBom => overviewBom.project.name);
    this.chartSummarization.data.datasets = dataProperties.map(propData => ({
      label: propData.label,
      data: this.allOverviewBoms().map(overviewBom => {
        if (Array.isArray(overviewBom[propData.prop])) {
          return overviewBom[propData.prop] ? overviewBom[propData.prop].length : 0;
        } else {
          return overviewBom[propData.prop];
        }
      })
    }));

    if (this.chartSummarizationRef?.chart) {
      this.chartSummarizationRef.chart.update();
    }
  }
}

@Component({
    selector: 'naikan-project-view-insights-technologies-chart',
    template: `
    <p-chart type="bar"
             height="100%"
             width="100%"
             #chartTechnologiesRef
             [data]="chartTechnologies.data"
             [options]="chartTechnologies.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
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
    if (!this.allOverviewBoms()) {
      return;
    }

    const technologyCounts = new Map<string, number>();

    this.allOverviewBoms().forEach(bom => bom.technologyNames?.forEach((tech) => {
      const count = technologyCounts.get(tech) || 0;
      technologyCounts.set(tech, count + 1);
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
    selector: 'naikan-project-view-insights-deployments-chart',
    template: `
    <p-chart #chartDeploymentsRef
             type="line"
             height="100%"
             width="100%"
             [data]="chartDeployments.data"
             [options]="chartDeployments.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
})
export class DeploymentsChart extends AbstractInsightChart {

  @ViewChild('chartDeploymentsRef') chartDeploymentsRef: UIChart;
  deploymentsPerMonth: CountsPerItems;

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
          display: false,
          text: ''
        }
      }
    },
    data: {} as any
  };

  constructor(override readonly layoutService: LayoutService, private readonly projectService: ProjectService) {
    super(layoutService);
  }

  override initChart(): void {
    this.projectService
    .getDeploymentsPerMonth(this.table.createLazyLoadMetadata())
    .pipe(finalize(() => {
      const sum = this.deploymentsPerMonth.counts.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
      const average = sum / this.deploymentsPerMonth.counts.length;

      this.chartDeployments.data.labels = this.deploymentsPerMonth.names;
      this.chartDeployments.data.datasets = [
        {
          label: 'Average deployments',
          data: Array(this.deploymentsPerMonth.names.length).fill(average),
          borderWidth: 1,
          fill: false,
          pointStyle: false,
          borderColor: Charts.documentPrimaryStyle(),
          borderDash: [5, 5]
        },
        {
          label: 'Deployments',
          data: this.deploymentsPerMonth.counts,
          borderWidth: 1,
          fill: true,
          pointStyle: true,
          borderColor: Charts.documentPrimaryStyle(),
          backgroundColor: Charts.documentPrimaryStyleWithDefaultOpacity(),
        }];

      if (this.chartDeploymentsRef?.chart) {
        this.chartDeploymentsRef.chart.update();
      }
    }))
    .subscribe(data => this.deploymentsPerMonth = data);
  }
}

@Component({
    selector: 'naikan-project-view-insights-deployments-summarization-chart',
    template: `
    <p-chart type="bar"
             height="100%"
             width="100%"
             #chartDeploymentsSummarizationRef
             [data]="chartDeploymentsSummarization.data"
             [options]="chartDeploymentsSummarization.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
})
export class DeploymentsSummarizationChart extends AbstractInsightChart {

  @ViewChild('chartDeploymentsSummarizationRef') chartDeploymentsSummarizationRef: UIChart;
  deploymentsPerProject: CountsPerItems;

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

  constructor(override readonly layoutService: LayoutService, private readonly projectService: ProjectService) {
    super(layoutService);
  }

  override initChart(): void {
    this.projectService
    .getDeploymentsPerProject(this.table.createLazyLoadMetadata())
    .pipe(finalize(() => {
      this.chartDeploymentsSummarization.data.labels = this.deploymentsPerProject.names;
      this.chartDeploymentsSummarization.data.datasets = [
        {
          label: 'Deployments',
          data: this.deploymentsPerProject.counts,
          fill: true,
          pointStyle: false,
          borderColor: Charts.documentPrimaryStyle(),
          backgroundColor: Charts.documentPrimaryStyle(),
        }];


      if (this.chartDeploymentsSummarizationRef?.chart) {
        this.chartDeploymentsSummarizationRef.chart.update();
      }
    }))
    .subscribe(data => this.deploymentsPerProject = data);

  }
}


@Component({
    selector: 'naikan-project-view-insights-commits-chart',
    template: `
    <p-chart #chartCommitsRef
             type="line"
             height="100%"
             width="100%"
             [data]="chartCommits.data"
             [options]="chartCommits.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
})
export class CommitsChart extends AbstractInsightChart {

  @ViewChild('chartCommitsRef') chartCommitsRef: UIChart;
  commitsPerMonth: CountsPerItems;

  chartCommits = {
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
          text: 'Contributions to default branch, excluding merge commits'
        }
      }
    },
    data: {} as any
  };

  constructor(override readonly layoutService: LayoutService, private readonly projectService: ProjectService) {
    super(layoutService);
  }

  override initChart(): void {
    this.projectService
    .getCommitsPerMonth(this.table.createLazyLoadMetadata())
    .pipe(finalize(() => {
      const sum = this.commitsPerMonth.counts.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
      const average = sum / this.commitsPerMonth.counts.length;

      this.chartCommits.data.labels = this.commitsPerMonth.names;
      this.chartCommits.data.datasets = [
        {
          label: 'Average commits',
          data: Array(this.commitsPerMonth.names.length).fill(average),
          borderWidth: 1,
          fill: false,
          pointStyle: false,
          borderColor: Charts.documentPrimaryStyle(),
          borderDash: [5, 5]
        },
        {
          label: 'Commits',
          data: this.commitsPerMonth.counts,
          borderWidth: 1,
          fill: true,
          pointStyle: true,
          borderColor: Charts.documentPrimaryStyle(),
          backgroundColor: Charts.documentPrimaryStyleWithDefaultOpacity(),
        }];

      if (this.chartCommitsRef?.chart) {
        this.chartCommitsRef.chart.update();
      }
    }))
    .subscribe(data => this.commitsPerMonth = data);
  }
}

@Component({
    selector: 'naikan-project-view-insights-commits-summarization-chart',
    template: `
    <p-chart type="bar"
             height="100%"
             width="100%"
             #chartCommitsSummarizationRef
             [data]="chartCommitsSummarization.data"
             [options]="chartCommitsSummarization.options">
    </p-chart>
  `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        ChartModule
    ]
})
export class CommitsSummarizationChart extends AbstractInsightChart {

  @ViewChild('chartCommitsSummarizationRef') chartCommitsSummarizationRef: UIChart;
  commitsPerProject: CountsPerItems;

  chartCommitsSummarization = {
    options: {
      plugins: {
        legend: {
          display: true
        },
        title: {
          display: true,
          text: 'Contributions to default branch, excluding merge commits'
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

  constructor(override readonly layoutService: LayoutService, private readonly projectService: ProjectService) {
    super(layoutService);
  }

  override initChart(): void {
    this.projectService
    .getCommitsPerProject(this.table.createLazyLoadMetadata())
    .pipe(finalize(() => {
      this.chartCommitsSummarization.data.labels = this.commitsPerProject.names;
      this.chartCommitsSummarization.data.datasets = [
        {
          label: 'Commits',
          data: this.commitsPerProject.counts,
          fill: true,
          pointStyle: false,
          borderColor: Charts.documentPrimaryStyle(),
          backgroundColor: Charts.documentPrimaryStyle(),
        }];


      if (this.chartCommitsSummarizationRef?.chart) {
        this.chartCommitsSummarizationRef.chart.update();
      }
    }))
    .subscribe(data => this.commitsPerProject = data);
  }
}


@Component({
    selector: '.naikan-project-view-insights-header',
    template: `
    <p-tabs value="0" styleClass="ml-2">
      <p-tablist>
        <p-tab value="0">Summarization</p-tab>
        <p-tab value="1">Technologies</p-tab>
        <p-tab value="2">Deployments</p-tab>
        <p-tab value="3">Commits</p-tab>
      </p-tablist>
      
      <p-tabpanels>
        <p-tabpanel value="0">
          <div class="chart-panel">
            <naikan-project-view-insights-summarization-chart [table]="table"
                                                              [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-summarization-chart>
          </div>
        </p-tabpanel>
        <p-tabpanel value="1">
          <div class="chart-panel">
            <naikan-project-view-insights-technologies-chart [table]="table"
                                                             [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-technologies-chart>
          </div>
        </p-tabpanel>
        <p-tabpanel value="2">
          <div class="chart-panel">
            <naikan-project-view-insights-deployments-chart [table]="table"
                                                            [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-deployments-chart>
          </div>

          <div class="chart-panel mt-20">
            <naikan-project-view-insights-deployments-summarization-chart [table]="table"
                                                                          [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-deployments-summarization-chart>
          </div>
        </p-tabpanel>
        <p-tabpanel value="3">
          <div class="chart-panel">
            <naikan-project-view-insights-commits-chart [table]="table"
                                                        [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-commits-chart>
          </div>

          <div class="chart-panel mt-20">
            <naikan-project-view-insights-commits-summarization-chart [table]="table"
                                                                      [bomOverviews]="allBomOverviews()">
            </naikan-project-view-insights-commits-summarization-chart>
          </div>
        </p-tabpanel>
      </p-tabpanels>
    </p-tabs>
  `,
    styles: ['.chart-panel {height: 50vh; position: relative; overflow-y: auto; overflow-x: auto; margin-top: 20px;}'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
  imports: [
    TooltipModule,
    ChartModule,
    SharedModule,
    TabsModule,
    CommitsSummarizationChart,
    CommitsChart,
    DeploymentsSummarizationChart,
    SummarizationChart,
    TechnologiesChart,
    DeploymentsChart,
  ]
})
export class ProjectViewInsightsHeader extends AbstractProjectView {

  @Input() table!: Table;
  private _bomOverviews: BomOverview[];

  @Input()
  set bomOverviews(bomOverviews: BomOverview[]) {
    this._bomOverviews = bomOverviews;
  }

  constructor(projectService: ProjectService, principal: Principal) {
    super(projectService, principal)
  }

  allBomOverviews(): BomOverview[] {
    if (!this._bomOverviews) {
      return [];
    }

    return this._bomOverviews;
  }
}