import {Component} from '@angular/core';
import {ContactService} from './contact.service';
import {Breadcrumb, Search} from "@naikan/shared";
import {SharedModule} from 'primeng/api';
import {AbstractOverviewComponent} from "../abstract-overview.component";
import {LayoutService} from "@naikan/layout/app.layout.service";
import {OverviewProjectTable} from '../overview-project-table';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';
import {DatePipe, NgIf} from '@angular/common';
import {ButtonModule} from 'primeng/button';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {OverviewGroup} from "../overview";

@Component({
  templateUrl: './contact.component.html',
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
  providers: [ContactService, DatePipe]
})
export class ContactComponent extends AbstractOverviewComponent<OverviewGroup> {

  constructor(private readonly contactService: ContactService, layoutService: LayoutService) {
    super(layoutService)
  }

  loadOverviews(event?: TableLazyLoadEvent): void {
    this.contactService.getOverviews(event)
    .subscribe(data => this.page = data);
  }
}
