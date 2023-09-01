import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {NgSwitch, NgSwitchCase, NgSwitchDefault} from '@angular/common';
import {Charts, Deployment} from "@naikan/shared";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {Subscription} from "rxjs";
import {ChartModule, UIChart} from "primeng/chart";

@Component({
  selector: 'naikan-deployments-chart',
  template: `
    <p-chart #chartDeploymentsRef
             type="line"
             height="{{height}}"
             width="{{width}}"
             [data]="chartDeployments.data"
             [options]="chartDeployments.options">
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
export class DeploymentsChart implements OnDestroy, OnInit {

  private _deployments: Deployment[];

  @ViewChild('chartDeploymentsRef') chartDeploymentsRef: UIChart;

  @Input()
  set deployments(deployments: Deployment[]) {
    this._deployments = deployments;
    this.initDeploymentsChart();
  }

  @Input() months: number = 0;
  @Input() height: string = "100%";
  @Input() width: string = "100%";

  subscription!: Subscription;

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

  constructor(private readonly layoutService: LayoutService) {
    this.subscription = this.layoutService.configUpdate$.subscribe(() => {
      this.initDeploymentsChart();
    });
  }

  ngOnInit(): void {
    this.initDeploymentsChart()
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private initDeploymentsChart(): void {
    if (!this._deployments) {
      return;
    }

    this.chartDeployments.options.plugins.title.display = this.months > 0;
    this.chartDeployments.options.plugins.title.text = `Last ${this.months} months`;

    const deploymentCounts = new Map<string, number>();
    const uniqueLocations = new Map<string, Set<string>>();
    const today = new Date();
    const since = this.months > 0
        ? new Date(today.getFullYear(), today.getMonth() - this.months + 1, 1)
        : new Date(this._deployments[0].timestamp);
    const to = new Date(today.getFullYear(), today.getMonth() + 1, 1);

    for (let yearMonth = new Date(since); yearMonth <= to; yearMonth.setMonth(yearMonth.getMonth() + 1)) {
      const period = yearMonth.toISOString().substring(0, 7);
      deploymentCounts.set(period, 0);
      uniqueLocations.set(period, new Set<string>());
    }

    let deployments = 0;
    this._deployments.forEach((deployment) => {
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
    });

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
}
