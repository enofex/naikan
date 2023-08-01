import {Component, ViewChild} from '@angular/core';
import {RouterLink} from '@angular/router';
import {AdministrationProjectService} from "./project.service";
import {Breadcrumb, DateTimePipe, Page, Search, Url} from "@naikan/shared";
import {Project} from "./project";
import {ConfirmationService, MessageService,} from "primeng/api";
import {DatePipe} from "@angular/common";
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {TooltipModule} from "primeng/tooltip";
import {ButtonModule} from "primeng/button";
import {MessageModule} from "primeng/message";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {MessagesModule} from "primeng/messages";

@Component({
  templateUrl: './project.component.html',
  standalone: true,
  imports: [RouterLink, Breadcrumb, DatePipe, TableModule, TooltipModule, Url, ButtonModule, Search, MessageModule, ConfirmDialogModule, ToastModule, MessagesModule, DateTimePipe],
  providers: [ConfirmationService, MessageService, AdministrationProjectService, DatePipe]
})
export class ProjectComponent {

  @ViewChild('tableAdministrationProjects', {static: true}) tableAdministrationProjects: Table;
  page: Page<Project>;

  constructor(
      private readonly confirmationService: ConfirmationService,
      private readonly messageService: MessageService,
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
        this.messageService.clear();

        this.administrationProjectService
        .deleteProject(id)
        .subscribe({
          next: () => {
            this.loadProjects(this.tableAdministrationProjects.createLazyLoadMetadata());

            this.messageService.add({
              severity: 'success',
              detail: 'Project deleted'
            })
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              detail: 'An error occurred in deleting the project.'
            })
          }
        })
      }
    });
  }
}