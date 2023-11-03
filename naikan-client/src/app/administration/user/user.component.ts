import {Component, ViewChild} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {MenuModule} from "primeng/menu";
import {NgIf} from "@angular/common";
import {ConfirmationService, MessageService} from "primeng/api";
import {Breadcrumb, Page, Search, Url} from "@naikan/shared";
import {ButtonModule} from "primeng/button";
import {MessagesModule} from "primeng/messages";
import {AdministrationUserService} from "./user.service";
import {User} from "./user";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {MultiSelectChangeEvent, MultiSelectModule} from "primeng/multiselect";
import {FormsModule} from "@angular/forms";

interface Authority {
  label: string;
  value: string;
}

@Component({
  templateUrl: './user.component.html',
  standalone: true,
  imports: [RouterLink, TableModule, MenuModule, Url, Breadcrumb, NgIf, ButtonModule, Search, ConfirmDialogModule, MessagesModule, FormsModule, MultiSelectModule],
  providers: [ConfirmationService, MessageService, AdministrationUserService]
})
export class UserComponent {

  @ViewChild('tableAdministrationUsers', {static: true})
  tableAdministrationUsers: Table;
  page: Page<User>;
  authorities: Authority[] = [
    {label: 'Admin', value: 'ROLE_ADMIN'}
  ];

  constructor(
      private readonly confirmationService: ConfirmationService,
      private readonly messageService: MessageService,
      private readonly administrationUserService: AdministrationUserService) {
  }

  loadUsers(event?: TableLazyLoadEvent): void {
    this.administrationUserService
    .getUsers(event)
    .subscribe(data => this.page = data);
  }

  confirmDelete(id: string): void {
    this.confirmationService.confirm({
      message: 'Do you want to delete this user permanently?',
      header: 'Delete user confirmation',
      icon: 'pi pi-info-circle',
      rejectButtonStyleClass: 'p-button-outlined',
      accept: () => {
        this.messageService.clear();

        this.administrationUserService
        .deleteUserById(id)
        .subscribe({
          next: () => {
            this.loadUsers(this.tableAdministrationUsers.createLazyLoadMetadata());

            this.messageService.add({
              severity: 'success',
              detail: 'User deleted'
            })
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              detail: 'An error occurred in deleting the user.'
            })
          }
        })
      }
    });
  }

  onAuthoritiesChange(id: string, event: MultiSelectChangeEvent): void {
    this.administrationUserService
    .updateUserAuthoritiesById(id, event.value)
    .subscribe();
  }
}