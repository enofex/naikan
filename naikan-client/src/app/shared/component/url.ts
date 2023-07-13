import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';
import {NgSwitch, NgSwitchCase, NgSwitchDefault} from '@angular/common';
import {Urls} from '../util/urls';

@Component({
  selector: 'naikan-url',
  template: `
    <ng-container [ngSwitch]="Urls.isValid(url) && 'isValid'">
      <ng-container *ngSwitchCase="'isValid'">
        <a href="{{url}}" target="_blank">{{ url}}</a>
      </ng-container>
      <ng-container *ngSwitchDefault>
        {{url}}
      </ng-container>
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
  ],
})
export class Url {
  protected readonly Urls = Urls;

  @Input() url: string;
}


