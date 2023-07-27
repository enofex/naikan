import {Component} from '@angular/core';
import {IntegrationService} from './integration.service';
import {Breadcrumb, Search, Url} from '../../shared';
import {SharedModule} from 'primeng/api';
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
  templateUrl: './integration.component.html',
  standalone: true,
  imports: [
    Breadcrumb,
    TableModule,
    SharedModule,
    Search,
    ButtonModule,
    NgIf,
    TooltipModule,
    Url,
    TagModule,
    OverviewProjectTable,
  ],
  providers: [IntegrationService, DatePipe]
})
export class IntegrationComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly integrationService: IntegrationService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.integrationService.getOverviews(event)
    .subscribe(data => this.page = data);
  }
}
