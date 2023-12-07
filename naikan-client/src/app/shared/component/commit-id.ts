import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';

import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';

@Component({
  selector: 'naikan-commit-id',
  template: `
    <span class="text-mono" tooltipPosition="top" pTooltip="{{ commitId }}">{{shortened()}}</span>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    TagModule,
    TooltipModule
  ],
})
export class CommitId {
  @Input() commitId: string

  shortened(): string {
    return this.commitId ? this.commitId.substring(0, 7) : "";
  }
}


