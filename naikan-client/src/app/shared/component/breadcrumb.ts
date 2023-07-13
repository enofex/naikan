import {
  ChangeDetectionStrategy,
  Component,
  ContentChild,
  Input,
  TemplateRef,
  ViewEncapsulation
} from '@angular/core';
import {NgIf, NgTemplateOutlet} from '@angular/common';
import {BreadcrumbModule} from "primeng/breadcrumb";
import {MenuItem} from "primeng/api";

@Component({
  selector: 'naikan-breadcrumb',
  template: `
    <div class="flex justify-content-between flex-wrap  mb-3 naikan-breadcrumb">
      <div class="flex align-items-center justify-content-center">
        <p-breadcrumb styleClass="max-w-full border-none" [model]="items"
                      [home]="home"></p-breadcrumb>
      </div>
      <div class="flex align-items-center justify-content-center pr-4" *ngIf="rightTemplate">
        <div class="p-datatable-header">
          <ng-container *ngTemplateOutlet="rightTemplate"></ng-container>
        </div>
      </div>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    BreadcrumbModule,
    NgIf,
    NgTemplateOutlet,
  ],
})
export class Breadcrumb {
  @ContentChild('rightTemplate') rightTemplate: TemplateRef<any>;

  @Input() items: MenuItem[];

  home: MenuItem = {icon: 'pi pi-home', routerLink: '/projects'};
}


