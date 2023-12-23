import {Component, ViewChild} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Table, TableLazyLoadEvent, TableModule} from "primeng/table";
import {MenuModule} from "primeng/menu";
import {DatePipe} from "@angular/common";
import {ConfirmationService, MessageService} from "primeng/api";
import {Token} from "./token";
import {AdministrationTokenService} from "./token.service";
import {Breadcrumb, DateTimePipe, Page, Search, Url} from "@naikan/shared";
import {ButtonModule} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {MessagesModule} from "primeng/messages";
import {TagModule} from "primeng/tag";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {InputTextareaModule} from "primeng/inputtextarea";
import {finalize} from "rxjs";

@Component({
  templateUrl: './token.component.html',
  standalone: true,
  imports: [RouterLink, TableModule, MenuModule, DatePipe, Url, Breadcrumb, DateTimePipe, ButtonModule, Search, ConfirmDialogModule, MessagesModule, TagModule, DialogModule, FormsModule, InputTextareaModule],
  providers: [ConfirmationService, MessageService, AdministrationTokenService, DatePipe]
})
export class TokenComponent {

  @ViewChild('tableAdministrationTokens', {static: true})
  tableAdministrationTokens: Table;
  page: Page<Token>;
  description: string;
  newTokenDialog = false;

  constructor(
      private readonly confirmationService: ConfirmationService,
      private readonly messageService: MessageService,
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
        this.messageService.clear();

        this.administrationTokenService
        .deleteTokenById(id)
        .subscribe({
          next: (): void => {
            this.loadTokens(this.tableAdministrationTokens.createLazyLoadMetadata());

            this.messageService.add({
              severity: 'success',
              detail: 'Token deleted'
            })
          },
          error: (): void => {
            this.messageService.add({
              severity: 'error',
              detail: 'An error occurred in deleting the token.'
            })
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
        this.messageService.clear();
        this.loadTokens(this.tableAdministrationTokens.createLazyLoadMetadata());

        this.messageService.add({
          severity: 'success',
          detail: 'Token added'
        })
      },
      error: (): void => {
        this.messageService.add({
          severity: 'error',
          detail: 'An error occurred in adding a new token.'
        })
      }
    })
  }
}