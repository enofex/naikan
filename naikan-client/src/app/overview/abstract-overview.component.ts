import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {Charts, Page} from "../shared";
import {LayoutService} from "../layout/app.layout.service";
import {OverviewTopGroups} from "./overview-top-groups";

@Component({template: ''})
export abstract class AbstractOverviewComponent<T> implements OnDestroy, OnInit {

  protected readonly Object = Object;

  page: Page<T>;
  subscription!: Subscription;
  expandedRows = {};
  topN = 10;

  chartData: any;
  chartOptions = {
    responsive: false,
    maintainAspectRatio: false,
    animation: false,
    aspectRatio: 0.8,
    barThickness: 20,
    barPercentage: 0.6,
    plugins: {
      legend: {
        display: false
      },
      title: {
        display: true
      }
    },
    scale: {
      ticks: {
        precision: 0
      }
    },
    indexAxis: 'y'
  };

  protected constructor(private readonly layoutService: LayoutService) {
    this.subscription = this.layoutService.configUpdate$.subscribe(() => {
      this.initChart();
    });
  }

  expand(): void {
    if (this.expandedRows && Object.keys(this.expandedRows).length) {
      this.expandedRows = {};
    } else {
      for (const group of this.page?.content) {
        this.expandedRows[group['uuid']] = true;

      }
    }
  }

  protected decreaseTopN(): void {
    if (this.topN > 1) {
      this.topN--;
    }
    this.initChart();
  }

  protected increaseTopN(): void {
    if (this.topN < 50) {
      this.topN++;
    }
    this.initChart();
  }

  ngOnInit(): void {
    this.initChart();
  }

  initChartData(data: OverviewTopGroups, title: string, label: string = "Projects") {
    if (data) {
      const documentStyle = Charts.documentStyle();
      this.topN = data.names?.length;

      this.chartData = {
        labels: data.names,
        datasets: [
          {
            label: label,
            data: data.counts,
            backgroundColor: documentStyle,
            borderColor: documentStyle
          }
        ]
      };

      Object.assign(this.chartOptions.plugins.title, {'text': `Top ${this.topN} ${title}`});
    }
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  protected initChart(): void {
  }
}