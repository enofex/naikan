import {Component, OnDestroy, OnInit} from "@angular/core";
import {Subscription} from "rxjs";
import {Page} from "../shared";
import {LayoutService} from "../layout/app.layout.service";

@Component({template: ''})
export abstract class AbstractOverviewComponent<T> implements OnDestroy, OnInit {

  protected readonly Object = Object;

  page: Page<T>;
  subscription!: Subscription;
  expandedRows = {};
  topN = 10;

  chartData: any;
  chartOptions = {
    responsive: true,
    maintainAspectRatio: true,
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
    animation: {
      duration: 0
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

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  protected initChart(): void {
  }
}