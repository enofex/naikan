import {Component, Input, ViewEncapsulation} from '@angular/core';

import {Table, TableModule} from "primeng/table";
import {AccordionModule} from "primeng/accordion";
import {FilterMatchMode, FilterMetadata, SelectItem} from "primeng/api";
import {SidebarModule} from "primeng/sidebar";
import {ButtonModule} from "primeng/button";
import {MultiSelectModule} from "primeng/multiselect";
import {CalendarModule} from "primeng/calendar";
import {InputNumberModule} from "primeng/inputnumber";
import {FormsModule} from "@angular/forms";
import {ProjectService} from "./project.service";
import {TooltipModule} from "primeng/tooltip";
import {TagModule} from "primeng/tag";
import {ProjectFilters} from "./project-filters";

export interface FilterItem {
  name: string;
  count: number;
}

export interface ProjectFilter {
  tags?: FilterItem[];
  packaging?: FilterItem[];
  groupIds?: FilterItem[];
  organizations?: FilterItem[];
  organizationDepartments?: FilterItem[];
  environments?: FilterItem[];
  environmentLocations?: FilterItem[];
  environmentTags?: FilterItem[];
  teams?: FilterItem[];
  developers?: FilterItem[];
  developerOrganizations?: FilterItem[];
  developerDepartments?: FilterItem[];
  developerRoles?: FilterItem[];
  contacts?: FilterItem[];
  contactRoles?: FilterItem[];
  technologies?: FilterItem[];
  technologyVersions?: FilterItem[];
  technologyTags?: FilterItem[];
  licenses?: FilterItem[];
  integrations?: FilterItem[];
  integrationTags?: FilterItem[];
  deployments?: FilterItem[];
}

@Component({
  selector: 'naikan-project-filter-header',
  template: `
    <div class="flex align-items-center {{hasFilters ? 'has-filters' : ''}}">
      <span class="vertical-align-middle text-900 font-medium uppercase">{{header}}</span>
    </div>
  `,
  styleUrls: ['./project-filter.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
})
export class ProjectFilterHeader {
  @Input() header: string;
  @Input() hasFilters: boolean;
}

@Component({
  selector: 'naikan-project-filter-multiselect',
  template: `
    <div class="field grid">
      <div class="col-12">
        <p-columnFilter field="{{field}}"
          tooltipPosition="top"
          pTooltip="{{label}}"
          matchMode="equals"
          [showClearButton]="true"
          [showMatchModes]="false"
          [showOperator]="false"
          [showMenu]="false">
          <ng-template pTemplate="filter" let-value let-filter="filterCallback">
            <p-multiSelect placeholder="{{label}}"
              [options]="filterNames"
              optionLabel="name"
              optionValue="name"
              [ngModel]="value"
              (onChange)="filter($event.value)">
              <ng-template let-item pTemplate="item">
                <div class="flex align-items-center gap-2">
                  @if (!tag) {
                    <div>{{item.name }}</div>
                  }
                  @if (tag) {
                    <p-tag severity="info" value="{{item.name}}" class="mr-1"></p-tag>
                  }
                  <div class="text-400">({{item.count }})</div>
                </div>
              </ng-template>
            </p-multiSelect>
          </ng-template>
        </p-columnFilter>
      </div>
    </div>
    `,
  styleUrls: ['./project-filter.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TableModule,
    TooltipModule,
    MultiSelectModule,
    FormsModule,
    TagModule
  ],
})
export class ProjectFilterMultiSelect {
  @Input() label: string;
  @Input() field: string;
  @Input() filterNames: FilterItem[];
  @Input() tag: boolean;
}


@Component({
  selector: 'naikan-project-filter-date',
  template: `
    <div class="field grid">
      <div class="col-12">
        <p-columnFilter type="date"
                        field="{{field}}"
                        tooltipPosition="top"
                        pTooltip="{{label}}"
                        matchMode="dateIs">
          <ng-template pTemplate="filter" let-value let-filter="filterCallback">
            <p-calendar placeholder="{{label}}"
                        [ngModel]="value"
                        (onSelect)="filter($event)"></p-calendar>
          </ng-template>
        </p-columnFilter>
      </div>
    </div>
  `,
  styleUrls: ['./project-filter.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TableModule,
    TooltipModule,
    CalendarModule,
    FormsModule,
  ],
})
export class ProjectFilterDate {
  @Input() label: string;
  @Input() field: string;
}


@Component({
  selector: 'naikan-project-filter-numeric',
  template: `
    <div class="field grid">
      <div class="col-12">
        <p-columnFilter type="numeric"
                        field="{{field}}"
                        tooltipPosition="top"
                        pTooltip="{{label}}"
                        matchMode="is"
                        [matchModeOptions]="numericMatchModeOptions">
          <ng-template pTemplate="filter" let-value let-filter="filterCallback">
            <p-inputNumber placeholder="{{label}}"
                           [ngModel]="value"
                           [showButtons]="true"
                           [min]="0"
                           (onInput)="filter($event.value)">
            </p-inputNumber>
          </ng-template>
        </p-columnFilter>
      </div>
    </div>
  `,
  styleUrls: ['./project-filter.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TableModule,
    TooltipModule,
    InputNumberModule,
    FormsModule,
  ],
})
export class ProjectFilterNumeric {
  @Input() label: string;
  @Input() field: string;

  numericMatchModeOptions: SelectItem[] = [
    {
      label: 'Equals',
      value: FilterMatchMode.IS
    },
    {
      label: 'Not Equals',
      value: FilterMatchMode.IS_NOT
    },
    {
      label: 'Less than',
      value: FilterMatchMode.LESS_THAN
    },
    {
      label: 'Less than or equal to ',
      value: FilterMatchMode.LESS_THAN_OR_EQUAL_TO
    },
    {
      label: 'Greater than',
      value: FilterMatchMode.GREATER_THAN
    },
    {
      label: 'Greater than or equal to ',
      value: FilterMatchMode.GREATER_THAN_OR_EQUAL_TO
    }
  ];
}


@Component({
  selector: 'naikan-project-filter',
  template: `
    <p-sidebar [(visible)]="filterVisible"
               position="right"
               styleClass="w-8 md:w-6 lg:w-4"
               class="sidebar-filter">

      <ng-template pTemplate="header">
        <div class="grid w-full mt-1">
          <div class="col-5 text-xl text-900 font-medium uppercase mt-2">
            Filter
          </div>
          <div class="col-6 text-900 text-right font-medium uppercase">
            <p-button (click)="restFilters()"
                      label="Clear all filters"
                      styleClass="p-button-link">
            </p-button>
          </div>
        </div>
        <br>
      </ng-template>

      <ng-template pTemplate="content">
        <p-accordion>
          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Project"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['timestamp', 'tags', 'project'])"
              >
              </naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-date field="timestamp"
                                        label="Last updated">
            </naikan-project-filter-date>

            <naikan-project-filter-multiselect field="project.packaging"
                                               label="Packaging"
                                               [filterNames]="projectFilter?.packaging">
            </naikan-project-filter-multiselect>
            <naikan-project-filter-multiselect field="project.groupId"
                                               label="Group"
                                               [filterNames]="projectFilter?.groupIds">
            </naikan-project-filter-multiselect>
            <naikan-project-filter-multiselect field="tags"
                                               label="Tags"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.tags">
            </naikan-project-filter-multiselect>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Organization"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['organization'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="organization.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.organizations">
            </naikan-project-filter-multiselect>
            <naikan-project-filter-multiselect field="organization.department"
                                               label="Department"
                                               [filterNames]="projectFilter?.organizationDepartments">
            </naikan-project-filter-multiselect>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Environment"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['environments'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="environments.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.environments">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="environments.location"
                                               label="Location"
                                               [filterNames]="projectFilter?.environmentLocations">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="environments.tags"
                                               label="Tags"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.environmentTags">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="environmentsCount"
                                           label="Environments">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Team"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['teams'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="teams.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.teams">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="teamsCount"
                                           label="Teams">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Developer"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['developers'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="developers.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.developers">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="developers.organization"
                                               label="Organization"
                                               [filterNames]="projectFilter?.developerOrganizations">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="developers.department"
                                               label="Department"
                                               [filterNames]="projectFilter?.developerDepartments">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="developers.roles"
                                               label="Roles"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.developerRoles">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="developersCount"
                                           label="Developers">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Contact"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['contacts'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="contacts.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.contacts">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="contacts.roles"
                                               label="Roles"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.contactRoles">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="contactsCount"
                                           label="Contacts">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Integration"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['integrations'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="integrations.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.integrations">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="integrations.tags"
                                               label="Tags"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.integrationTags">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="integrationsCount"
                                           label="Integrations">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Technology"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['technologies'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="technologies.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.technologies">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="technologies.version"
                                               label="Version"
                                               [filterNames]="projectFilter?.technologyVersions">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-multiselect field="technologies.tags"
                                               label="Tags"
                                               [tag]="true"
                                               [filterNames]="projectFilter?.technologyTags">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="technologiesCount"
                                           label="Technologies">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="Deployments"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['deployments'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="deployments.location"
                                               label="Location"
                                               [filterNames]="projectFilter?.deployments">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="deploymentsCount"
                                           label="Deployments">
            </naikan-project-filter-numeric>
          </p-accordionTab>

          <p-accordionTab>
            <ng-template pTemplate="header">
              <naikan-project-filter-header 
                  header="License"
                  [hasFilters]="ProjectFilters.hasFiltersForKeys(table.filters, ['licenses'])">
              ></naikan-project-filter-header>
            </ng-template>

            <naikan-project-filter-multiselect field="licenses.name"
                                               label="Name"
                                               [filterNames]="projectFilter?.licenses">
            </naikan-project-filter-multiselect>

            <naikan-project-filter-numeric field="licensesCount"
                                           label="Licenses">
            </naikan-project-filter-numeric>
          </p-accordionTab>
        </p-accordion>
      </ng-template>
    </p-sidebar>
  `,
  styleUrls: ['./project-filter.scss'],
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    SidebarModule,
    TableModule,
    ButtonModule,
    AccordionModule,
    ProjectFilterHeader,
    ProjectFilterDate,
    ProjectFilterMultiSelect,
    ProjectFilterNumeric
  ],
})
export class ProjectFilter {
  @Input() table!: Table;

  filterVisible: boolean;
  projectFilter: ProjectFilter;

  constructor(private readonly projectService: ProjectService) {
  }

  show(): void {
    this.filterVisible = true;
    this.projectService.getProjectFilter()
    .subscribe(data => this.projectFilter = data);
  }

  restFilters(): void {
    Object.keys(this.table.filters).forEach(key => {
      if (key !== 'global' && this.table.filters[key]) {
        const filters: FilterMetadata[] = Array.isArray(this.table.filters[key])
            ? <FilterMetadata[]>this.table.filters[key]
            : [<FilterMetadata>this.table.filters[key]]

        filters.forEach(filter => {
          filter.value = null;
        })
      }
    });

    this.table.onLazyLoad.emit(this.table.createLazyLoadMetadata())
  }

  protected readonly ProjectFilters = ProjectFilters;
}



































