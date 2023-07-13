import {Component} from '@angular/core';
import {DeveloperService} from './developer.service';
import {SharedModule} from 'primeng/api';
import {Breadcrumb, Developer, Search} from '../../shared';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {LayoutService} from "../../layout/app.layout.service";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {DatePipe, NgIf} from '@angular/common';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {OverviewGroup} from "../overview";

@Component({
  templateUrl: './developer.component.html',
  standalone: true,
  imports: [
    Breadcrumb,
    TableModule,
    SharedModule,
    Search,
    ButtonModule,
    NgIf,
    TooltipModule,
    TagModule,
    OverviewProjectTable,
  ],
  providers: [DeveloperService, DatePipe]
})
export class DeveloperComponent extends AbstractOverviewComponent<OverviewGroup<Developer>> {

  constructor(private readonly developerService: DeveloperService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.developerService.getOverviews(event)
    .subscribe(data => this.page = data);
  }
}
