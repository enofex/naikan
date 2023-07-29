import {Component, OnInit, ViewChild} from '@angular/core';
import {MenuItem, SelectItem, SharedModule} from 'primeng/api';
import {ProjectService} from './project.service';
import {Bom, Page, Search} from '../shared';
import {ProjectViewOverviewBody} from './project-view-overview.component';
import {
  ProjectViewCondensedBody,
  ProjectViewCondensedHeader
} from './project-view-condensed.component';
import {AsyncPipe, DatePipe, NgClass, NgIf} from '@angular/common';
import {DropdownModule} from 'primeng/dropdown';
import {ButtonModule} from 'primeng/button';
import {ProjectFilter} from './project-filter';
import {Table, TableLazyLoadEvent, TableModule} from 'primeng/table';
import {InputSwitchModule, InputSwitchOnChangeEvent} from "primeng/inputswitch";
import {FormsModule} from "@angular/forms";
import {TooltipModule} from "primeng/tooltip";
import {ProjectFilters} from "./project-filters";
import {BlockUIModule} from "primeng/blockui";
import {TabMenuModule} from "primeng/tabmenu";

@Component({
  templateUrl: './project.component.html',
  standalone: true,
  imports: [TableModule, SharedModule, ProjectFilter, Search, ButtonModule, InputSwitchModule, DropdownModule, NgClass, ProjectViewOverviewBody, ProjectViewCondensedBody, ProjectViewCondensedHeader, InputSwitchModule, FormsModule, TooltipModule, AsyncPipe, BlockUIModule, NgIf, TabMenuModule],
  providers: [ProjectService, DatePipe]
})
export class ProjectComponent implements OnInit {

  protected readonly ProjectFilters = ProjectFilters;

  private readonly FAVORITES_KEY = "project-favorites";

  @ViewChild('projectsTable', {static: true}) projectsTable: Table;
  page: Page<Bom>;
  sortOptions: SelectItem[];
  sortField: string;
  sortOrder: number;
  favorites: boolean;
  viewMenuItems: MenuItem[] | undefined;
  viewMenuActiveItem: MenuItem | undefined;

  constructor(private readonly projectService: ProjectService) {
  }

  ngOnInit(): void {
    this.favorites = localStorage.getItem(this.FAVORITES_KEY) === 'true';
    this.initTableSort();
    this.initViewMenuItems();
  }

  onSortChange(event): void {
    this.sortField = event.value;
  }

  toggleSortOrder(): void {
    this.sortOrder = this.sortOrder === -1 ? 1 : -1;
  }

  toggleFavorites(event: InputSwitchOnChangeEvent): void {
    localStorage.setItem(this.FAVORITES_KEY, String(event.checked));
    this.loadProjects(this.projectsTable.createLazyLoadMetadata());
  }

  loadProjects(event?: TableLazyLoadEvent): void {
    this.projectService.getBoms(event, this.favorites)
    .subscribe(data => this.page = data);
  }

  exportAll(): void {
    this.projectService.exportAll(this.projectsTable.createLazyLoadMetadata());
  }

  onViewMenuActiveItemChange(event: MenuItem): void {
    this.viewMenuActiveItem = event;
  }

  private initTableSort(): void {
    this.sortOptions = [
      {label: 'Name ', value: 'project.name'},
      {label: 'Last updated', value: 'timestamp'},
      {label: 'Group', value: 'project.groupId'},
      {label: 'Artifact', value: 'project.artifactId'},
      {label: 'Packaging', value: 'project.packaging'},
      {label: 'Version', value: 'project.version'},
      {label: 'Environments', value: 'environmentsCount'},
      {label: 'Teams', value: 'teamsCount'},
      {label: 'Developers', value: 'developersCount'},
      {label: 'Contacts', value: 'contactsCount'},
      {label: 'Integrations', value: 'integrationsCount'},
      {label: 'Technologies', value: 'technologiesCount'},
      {label: 'Deployments', value: 'deploymentsCount'}
    ];
    this.sortField = this.sortOptions[0].value;
    this.sortOrder = 1;
  }

  private initViewMenuItems(): void {
    this.viewMenuItems = [
      {
        id: "0",
        label: 'Overview'
      },
      {
        id: "1",
        label: 'Condensed',
      },
    ];
    this.viewMenuActiveItem = this.viewMenuItems[0];
  }
}
