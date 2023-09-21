import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Clipboard} from '@angular/cdk/clipboard';
import {finalize} from 'rxjs';
import {
  Breadcrumb,
  Charts,
  DateTimePipe,
  Deployment,
  NaikanTags,
  Page,
  ProjectVersion,
  Search,
  Url
} from '@naikan/shared';
import {ProjectService} from '../project.service';
import {MenuItem, SharedModule} from "primeng/api";
import {ChartModule} from 'primeng/chart';
import {TagModule} from 'primeng/tag';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TabViewModule} from 'primeng/tabview';
import {DatePipe, NgFor, NgIf} from '@angular/common';
import {SplitButtonModule} from "primeng/splitbutton";
import {BomDetail} from "../bom-detail";
import {DeploymentsPerMonth} from "../deployments-per-month";

export interface GroupedDeploymentsPerVersion {
  version: string;
  count: number;
  deployments: Deployment[];
}

export interface LatestVersionPerEnvironment {
  environment: string;
  deployment: Deployment;
}

@Component({
  templateUrl: './project-detail.component.html',
  standalone: true,
  imports: [NgIf, Breadcrumb, TabViewModule, Url, ProjectVersion, NaikanTags, ButtonModule, TooltipModule, TableModule, SharedModule, Search, NgFor, TagModule, ChartModule, DatePipe, DateTimePipe, SplitButtonModule],
  providers: [ProjectService, DatePipe]
})
export class ProjectDetailComponent implements OnInit {

  protected readonly Object = Object;

  id: string;
  bomDetail: BomDetail;
  deploymentsPage: Page<Deployment>;
  deploymentsPerMonth: DeploymentsPerMonth;
  versionsPage: Page<GroupedDeploymentsPerVersion>;
  latestVersionPerEnvironment: LatestVersionPerEnvironment[];
  expandedVersionRows = {};
  items: MenuItem[];
  exportItems: MenuItem[] = [
    {
      label: 'XLSX',
      command: () => {
        this.exportXlsx();
      }
    },
    {
      label: 'JSON',
      command: () => {
        this.exportJson();
      }
    }
  ];

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

  constructor(private readonly route: ActivatedRoute,
              private readonly projectService: ProjectService,
              private readonly clipboard: Clipboard) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.loadBomDetail(this.id);
    this.loadDeploymentsPerMonth();
    this.loadLatestVersionPerEnvironment();
  }

  loadDeployments(event?: TableLazyLoadEvent): void {
    this.projectService
    .getProjectDeploymentsById(this.id, event)
    .subscribe(data => this.deploymentsPage = data);
  }

  loadGroupedDeploymentsPerVersion(event?: TableLazyLoadEvent): void {
    this.projectService
    .getGroupedDeploymentsPerVersionById(this.id, event)
    .subscribe(data => this.versionsPage = data);
  }

  copyToClipboard(value: HTMLElement): void {
    const text: string = value.textContent || '';
    this.clipboard.copy(text);
  }

  expandVersions(): void {
    if (this.expandedVersionRows && Object.keys(this.expandedVersionRows).length) {
      this.expandedVersionRows = {};
    } else {
      for (const version of this.versionsPage?.content) {
        this.expandedVersionRows[version.version] = true;
      }
    }
  }

  exportXlsx(): void {
    this.projectService.exportXlsxById(this.id);
  }

  exportJson(): void {
    this.projectService.exportJsonById(this.id);
  }

  private loadBomDetail(id: string): void {
    this.projectService.getBomDetailById(id)
    .pipe(finalize(() => {
      this.items = [{label: this.bomDetail.project.name}];
    }))
    .subscribe(data => {
      this.bomDetail = data
    });
  }

  private loadDeploymentsPerMonth(): void {
    this.projectService
    .getDeploymentsPerMonthById(this.id)
    .pipe(finalize(() => {
      const sum = this.deploymentsPerMonth.counts.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
      const average = sum / this.deploymentsPerMonth.counts.length;

      this.chartDeployments.data.labels = this.deploymentsPerMonth.months;
      this.chartDeployments.data.datasets = [
        {
          label: 'Average deployments',
          data: Array(this.deploymentsPerMonth.months.length).fill(average),
          borderWidth: 1,
          fill: false,
          pointStyle: false,
          borderColor: Charts.documentStyle(),
          borderDash: [5, 5]
        },
        {
          label: 'Deployments',
          data: this.deploymentsPerMonth.counts,
          borderWidth: 1,
          fill: true,
          pointStyle: false,
          borderColor: Charts.documentStyle(),
          backgroundColor: Charts.documentStyleWithDefaultOpacity(),
        }];
    }))
    .subscribe(data => this.deploymentsPerMonth = data);
  }

  private loadLatestVersionPerEnvironment(): void {
    this.projectService
    .getLatestVersionPerEnvironmentById(this.id)
    .subscribe(data => this.latestVersionPerEnvironment = data);
  }
}
