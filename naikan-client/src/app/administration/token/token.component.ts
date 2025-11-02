import {Component, signal, ViewChild, WritableSignal} from '@angular/core';
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {MenuModule} from "primeng/menu";
import {DatePipe} from "@angular/common";
import {ConfirmationService} from "primeng/api";
import {Token} from "./token";
import {AdministrationTokenService} from "./token.service";
import {Breadcrumb, DateTimePipe, Errors, Page, Search, Url} from "@naikan/shared";
import {ButtonModule} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {TagModule} from "primeng/tag";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {finalize} from "rxjs";
import {Tooltip} from "primeng/tooltip";
import {TextareaModule} from 'primeng/textarea';
import {IftaLabelModule} from 'primeng/iftalabel';
import {Message} from "primeng/message";

@Component({
    templateUrl: './token.component.html',
  imports: [TableModule, MenuModule, Url, Breadcrumb, DateTimePipe, ButtonModule, Search, ConfirmDialogModule, TagModule, DialogModule, FormsModule, Tooltip, TextareaModule, IftaLabelModule, Message],
    providers: [ConfirmationService, AdministrationTokenService, DatePipe]
})
export class TokenComponent {

  @ViewChild('tableAdministrationTokens', {static: true})
  tableAdministrationTokens: Table;
  page: Page<Token>;
  description: string;
  newTokenDialog = false;
  messages: WritableSignal<any[]> = signal([]);

  constructor(
      private readonly confirmationService: ConfirmationService,
      private readonly administrationTokenService: AdministrationTokenService) {
  }

  loadTokens(event?: TableLazyLoadEvent): void {
    this.administrationTokenService
    .getTokens(event)
    .subscribe(data => this.page = data);
  }

  isDateWithinFiveMinutes(date: Date): boolean {
    return date
        ? Math.floor((new Date().getTime() - new Date(date).getTime()) / 1000 / 60) <= 5
        : false;
  }

  confirmDelete(id: string): void {
    this.confirmationService.confirm({
      message: 'Do you want to delete this token permanently?',
      header: 'Delete token confirmation',
      icon: 'pi pi-info-circle',
      rejectButtonStyleClass: 'p-button-outlined',
      accept: () => {
        this.administrationTokenService
        .deleteTokenById(id)
        .subscribe({
          next: (): void => {
            this.loadTokens(this.tableAdministrationTokens.createLazyLoadMetadata());

            Errors.toSuccessMessage(this.messages, 'Token deleted');
          },
          error: (): void => {
            Errors.toErrorMessage(this.messages, 'An error occurred in deleting the token.');
          }
        })
      }
    });
  }

  openNewTokenDialog(): void {
    this.newTokenDialog = true;
  }

  hide(): void {
    this.newTokenDialog = false;
  }

  save(): void {
    this.administrationTokenService
    .saveToken(this.description)
    .pipe(finalize(() => this.hide()))
    .subscribe({
      next: (): void => {
        this.loadTokens(this.tableAdministrationTokens.createLazyLoadMetadata());

        Errors.toSuccessMessage(this.messages, 'Token added');
      },
      error: (): void => {
        Errors.toErrorMessage(this.messages, 'An error occurred in adding a new token.');
      }
    })
  }
}