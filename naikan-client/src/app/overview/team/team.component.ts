import {Component} from '@angular/core';
import {LayoutService} from "@naikan/layout/app.layout.service";
import {TeamService} from './team.service';
import {Breadcrumb, Search} from "@naikan/shared";
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {SharedModule} from "primeng/api";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe, NgIf} from '@angular/common';
import {OverviewGroup} from "../overview";

@Component({
  templateUrl: './team.component.html',
  standalone: true,
  imports: [
    Breadcrumb,
    NgIf,
    ChartModule,
    TableModule,
    SharedModule,
    Search,
    ButtonModule,
    TooltipModule,
    TagModule,
    OverviewProjectTable,
  ],
  providers: [TeamService, DatePipe]
})
export class TeamComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly teamService: TeamService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.teamService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.teamService.getTopGroups(this.topN)
    .subscribe(data => {
      this.initChartData(data, "Teams");
    });
  }
}
