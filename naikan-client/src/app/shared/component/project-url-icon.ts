import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';
import {NgIf, NgSwitch, NgSwitchCase, NgSwitchDefault} from '@angular/common';
import {TooltipModule} from 'primeng/tooltip';
import {Urls} from "../util/urls";

@Component({
  selector: 'naikan-project-url-icon',
  template: `
    <ng-container *ngIf="url" [ngSwitch]="Urls.isValid(url) && 'isValid'">
      <ng-container *ngSwitchCase="'isValid'">
        <a href="{{ url }}" target="_blank">
          <i class="pi pi-external-link text-gray-400 ml-3 text-sm"
             tooltipPosition="top"
             pTooltip="{{ url }}">
          </i>
        </a>
      </ng-container>
      <ng-container *ngSwitchDefault>
        <i class="pi pi-external-link text-gray-400 ml-3 text-sm"
           tooltipPosition="top"
           pTooltip="{{ url }}">
        </i>
      </ng-container>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgIf,
    NgSwitch,
    NgSwitchCase,
    TooltipModule,
    NgSwitchDefault,
  ],
})
export class ProjectUrlIcon {
  @Input() url: string;

  protected readonly Urls = Urls;
}


