import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';

import {TooltipModule} from 'primeng/tooltip';
import {Urls} from "../util/urls";

@Component({
  selector: 'naikan-project-url-icon',
  template: `
      @if (url) {
          @switch (Urls.isValid(url) && 'isValid') {
              @case ('isValid') {
                  <a href="{{ url }}" target="_blank">
                      <i class="pi pi-external-link text-gray-400 ml-3 text-sm"
                         tooltipPosition="top"
                         pTooltip="{{ url }}">
                      </i>
                  </a>
              }
              @default {
                  <i class="pi pi-external-link text-gray-400 ml-3 text-sm"
                     tooltipPosition="top"
                     pTooltip="{{ url }}">
                  </i>
              }
          }
      }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TooltipModule
  ],
})
export class ProjectUrlIcon {
  @Input() url: string;

  protected readonly Urls = Urls;
}


