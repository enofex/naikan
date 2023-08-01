import {Component} from '@angular/core';
import {TagService} from './tag.service';
import {Breadcrumb, Search, Url} from "@naikan/shared";
import {SharedModule} from 'primeng/api';
import {LayoutService} from '@naikan/layout/app.layout.service';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {ChartModule} from 'primeng/chart';
import {DatePipe, NgIf} from '@angular/common';
import {OverviewGroup} from "../overview";

@Component({
  templateUrl: './tag.component.html',
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
    Url,
    TagModule,
    OverviewProjectTable,
  ],
  providers: [TagService, DatePipe]
})
export class TagComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly tagService: TagService, layoutService: LayoutService) {
    super(layoutService);
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.tagService.getOverviews(event)
    .subscribe(data => this.page = data);
  }

  override initChart(): void {
    this.tagService.getTopGroups(this.topN)
    .subscribe(data => {
      this.initChartData(data, "Tags");
    });
  }

}
