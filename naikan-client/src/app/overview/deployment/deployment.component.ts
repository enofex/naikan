import {Component} from '@angular/core';
import {LayoutService} from "@naikan/layout/app.layout.service";
import {DeploymentService} from './deployment.service';
import {SharedModule} from "primeng/api";
import {OverviewDeployment} from "./overview-deployment";
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {Breadcrumb, DateTimePipe, ProjectUrlIcon, Search, Url} from "@naikan/shared";
import {TagModule} from 'primeng/tag';
import {RouterLink} from '@angular/router';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe} from '@angular/common';

@Component({
  templateUrl: './deployment.component.html',
  standalone: true,
  imports: [
    Breadcrumb,
    ChartModule,
    TableModule,
    SharedModule,
    Search,
    ButtonModule,
    TooltipModule,
    RouterLink,
    ProjectUrlIcon,
    Url,
    TagModule,
    DatePipe,
    DateTimePipe
  ],
  providers: [DeploymentService, DatePipe]
})
export class DeploymentComponent extends AbstractOverviewComponent<OverviewDeployment> {

  constructor(private readonly deploymentService: DeploymentService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.deploymentService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.deploymentService.getTopGroups(this.topN)
    .subscribe(data => {
      this.initChartData(data, "Projects", "Deployments");
    });
  }
}
