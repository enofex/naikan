import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Clipboard} from '@angular/cdk/clipboard';
import {finalize} from 'rxjs';
import {
  Branch,
  Breadcrumb,
  Charts,
  Commit,
  CommitId,
  DateTimePipe,
  Deployment,
  NaikanTags,
  Page,
  ProjectVersion,
  RepositoryTag,
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
import {DatePipe} from '@angular/common';
import {SplitButtonModule} from "primeng/splitbutton";
import {BomDetail} from "../bom-detail";
import {CountsPerItems} from "../counts-per-items";
import {MessagesModule} from "primeng/messages";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {ProgressBarModule} from "primeng/progressbar";

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
  imports: [Breadcrumb, TabViewModule, Url, ProjectVersion, NaikanTags, ButtonModule, TooltipModule, TableModule, SharedModule, Search, TagModule, ChartModule, DatePipe, DateTimePipe, SplitButtonModule, CommitId, MessagesModule, ProgressSpinnerModule, ProgressBarModule],
  providers: [ProjectService, DatePipe],
  styleUrls: ['.//project-detail.component.scss']
})
export class ProjectDetailComponent implements OnInit {

  protected readonly Object = Object;

  id: string;
  activeView: number;
  bomDetail: BomDetail;
  deploymentsPage: Page<Deployment>;
  deploymentsPerMonth: CountsPerItems;
  versionsPage: Page<GroupedDeploymentsPerVersion>;
  latestVersionPerEnvironment: LatestVersionPerEnvironment[];
  expandedVersionRows = {};
  commitsPage: Page<Commit>;
  commitsPerMonth: CountsPerItems;
  branchesPage: Page<Branch>;
  repositoryTagsPage: Page<RepositoryTag>;
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
        }
      }
    },
    data: {} as any
  };

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

  constructor(private readonly route: ActivatedRoute,
              private readonly projectService: ProjectService,
              private readonly clipboard: Clipboard) {
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.loadBomDetail(this.id);
    this.loadDeploymentsPerMonth();
    this.loadLatestVersionPerEnvironment();
    this.loadCommitsPerMonth();
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

  loadCommits(event?: TableLazyLoadEvent): void {
    this.projectService
    .getProjectCommitsById(this.id, event)
    .subscribe(data => this.commitsPage = data);
  }

  loadRepositoryTags(event?: TableLazyLoadEvent): void {
    this.projectService
    .getProjectRepositoryTagsById(this.id, event)
    .subscribe(data => this.repositoryTagsPage = data);
  }

  loadRepositoryBranches(event?: TableLazyLoadEvent): void {
    this.projectService
    .getProjectRepositoryBranchesById(this.id, event)
    .subscribe(data => this.branchesPage = data);
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
    }))
    .subscribe(data => this.deploymentsPerMonth = data);
  }

  private loadLatestVersionPerEnvironment(): void {
    this.projectService
    .getLatestVersionPerEnvironmentById(this.id)
    .subscribe(data => this.latestVersionPerEnvironment = data);
  }

  private loadCommitsPerMonth(): void {
    this.projectService
    .getCommitsPerMonthById(this.id)
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
    }))
    .subscribe(data => this.commitsPerMonth = data);
  }
}
