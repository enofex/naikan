import {Component} from '@angular/core';
import {EnvironmentService} from './environment.service';
import {Breadcrumb, Search} from "@naikan/shared";
import {SharedModule} from 'primeng/api';
import {LayoutService} from '@naikan/layout/app.layout.service';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe} from '@angular/common';
import {OverviewGroup} from "../overview";

@Component({
    templateUrl: './environment.component.html',
    imports: [
        Breadcrumb,
        ChartModule,
        TableModule,
        SharedModule,
        Search,
        ButtonModule,
        TooltipModule,
        TagModule,
        OverviewProjectTable
    ],
    providers: [EnvironmentService, DatePipe]
})
export class EnvironmentComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly environmentService: EnvironmentService, layoutService: LayoutService) {
    super(layoutService);
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.environmentService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.environmentService.getTopGroups(this.topN)
    .subscribe(data => {
      this.initChartData(data, "Environments");
    });
  }
}
