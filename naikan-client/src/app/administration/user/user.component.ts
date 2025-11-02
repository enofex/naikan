import {Component, signal, ViewChild, WritableSignal} from '@angular/core';
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {MenuModule} from "primeng/menu";

import {ConfirmationService} from "primeng/api";
import {Breadcrumb, Errors, Page, Search} from "@naikan/shared";
import {ButtonModule} from "primeng/button";
import {AdministrationUserService} from "./user.service";
import {User} from "./user";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {MultiSelectChangeEvent, MultiSelectModule} from "primeng/multiselect";
import {FormsModule} from "@angular/forms";
import {Message} from "primeng/message";

interface Authority {
  label: string;
  value: string;
}

@Component({
    templateUrl: './user.component.html',
  imports: [TableModule, MenuModule, Breadcrumb, ButtonModule, Search, ConfirmDialogModule, FormsModule, MultiSelectModule, Message],
    providers: [ConfirmationService, AdministrationUserService]
})
export class UserComponent {

  @ViewChild('tableAdministrationUsers', {static: true})
  tableAdministrationUsers: Table;
  page: Page<User>;
  authorities: Authority[] = [
    {label: 'Admin', value: 'ROLE_ADMIN'}
  ];
  messages: WritableSignal<any[]> = signal([]);

  constructor(
      private readonly confirmationService: ConfirmationService,
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
        this.administrationUserService
        .deleteUserById(id)
        .subscribe({
          next: (): void => {
            this.loadUsers(this.tableAdministrationUsers.createLazyLoadMetadata());

            Errors.toSuccessMessage(this.messages, 'User deleted');
          },
          error: (): void => {
            Errors.toErrorMessage(this.messages, 'An error occurred in deleting the user.');
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