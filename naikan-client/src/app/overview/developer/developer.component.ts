import {Component} from '@angular/core';
import {DeveloperService} from './developer.service';
import {SharedModule} from 'primeng/api';
import {Breadcrumb, Search} from "@naikan/shared";
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {DatePipe} from '@angular/common';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {OverviewGroup} from "../overview";

@Component({
    templateUrl: './developer.component.html',
    imports: [
        Breadcrumb,
        TableModule,
        SharedModule,
        Search,
        ButtonModule,
        TooltipModule,
        TagModule,
        OverviewProjectTable
    ],
    providers: [DeveloperService, DatePipe]
})
export class DeveloperComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly developerService: DeveloperService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.developerService.getOverviews(event)
    .subscribe(data => this.page = data);
  }
}
