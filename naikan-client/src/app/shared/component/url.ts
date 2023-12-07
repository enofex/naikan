import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';

import {Urls} from '../util/urls';

@Component({
  selector: 'naikan-url',
  template: `
      @switch (Urls.isValid(url) && 'isValid') {
          @case ('isValid') {
              <a href="{{url}}" target="_blank">{{ url }}</a>
          }
          @default {
              {{ url }}
          }
      }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true
})
export class Url {
  protected readonly Urls = Urls;

  @Input() url: string;
}


