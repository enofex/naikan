import {Component, signal, ViewChild, WritableSignal} from '@angular/core';
import {RouterLink} from '@angular/router';
import {AdministrationProjectService} from "./project.service";
import {Breadcrumb, DateTimePipe, Errors, Page, Search, Url} from "@naikan/shared";
import {Project} from "./project";
import {ConfirmationService, MessageService,} from "primeng/api";
import {DatePipe} from "@angular/common";
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {TooltipModule} from "primeng/tooltip";
import {ButtonModule} from "primeng/button";
import {MessageModule} from "primeng/message";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";

@Component({
    templateUrl: './project.component.html',
    imports: [RouterLink, Breadcrumb, TableModule, TooltipModule, Url, ButtonModule, Search, MessageModule, ConfirmDialogModule, ToastModule, DateTimePipe],
    providers: [ConfirmationService, MessageService, AdministrationProjectService, DatePipe]
})
export class ProjectComponent {

  @ViewChild('tableAdministrationProjects', {static: true}) tableAdministrationProjects: Table;
  page: Page<Project>;
  messages: WritableSignal<any[]> = signal([]);

  constructor(
      private readonly confirmationService: ConfirmationService,
      private readonly administrationProjectService: AdministrationProjectService) {
  }

  loadProjects(event?: TableLazyLoadEvent): void {
    this.administrationProjectService
    .getProjects(event)
    .subscribe(data => this.page = data);
  }

  confirmDelete(id: string): void {
    this.confirmationService.confirm({
      message: 'Do you want to delete this project permanently?',
      header: 'Delete project confirmation',
      icon: 'pi pi-info-circle',
      rejectButtonStyleClass: 'p-button-outlined',
      accept: () => {
        this.administrationProjectService
        .deleteById(id)
        .subscribe({
          next: (): void => {
            this.loadProjects(this.tableAdministrationProjects.createLazyLoadMetadata());

            Errors.toSuccessMessage(this.messages, 'Project deleted');
          },
          error: (): void => {
            Errors.toErrorMessage(this.messages, 'An error occurred in deleting the project.');
          }
        })
      }
    });
  }
}