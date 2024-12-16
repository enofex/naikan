import {
  ChangeDetectionStrategy,
  Component,
  ContentChild,
  Input,
  TemplateRef,
  ViewEncapsulation
} from '@angular/core';
import {NgTemplateOutlet} from '@angular/common';
import {BreadcrumbModule} from "primeng/breadcrumb";
import {MenuItem} from "primeng/api";

@Component({
    selector: 'naikan-breadcrumb',
    template: `
    <div class="flex justify-between flex-wrap  mb-2 naikan-breadcrumb">
      <div class="flex items-center justify-center">
        <p-breadcrumb styleClass="max-w-full border-surface-0" [model]="items"
        [home]="home"></p-breadcrumb>
      </div>
      @if (rightTemplate) {
        <div class="flex items-center justify-center pr-12">
          <div class="p-datatable-header">
            <ng-container *ngTemplateOutlet="rightTemplate"></ng-container>
          </div>
        </div>
      }
    </div>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        BreadcrumbModule,
        NgTemplateOutlet
    ]
})
export class Breadcrumb {
  @ContentChild('rightTemplate') rightTemplate: TemplateRef<any>;

  @Input() items: MenuItem[];

  home: MenuItem = {icon: 'pi pi-home', routerLink: '/projects'};
}


