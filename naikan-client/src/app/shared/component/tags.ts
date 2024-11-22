import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';

import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';

@Component({
    selector: 'naikan-tags',
    template: `
    @if (tags) {
      <span tooltipPosition="top" pTooltip="{{ tags }}">
        @for (tag of tags.slice(0, tagsToShow()); track tag) {
          <p-tag severity="info" value="{{tag}}" class="mr-1"></p-tag>
        }
        @if (tags.length > tagsToShow()) {
          <p-tag severity="info" value="..." class="mr-1"></p-tag>
        }
      </span>
    }
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None,
    imports: [
        TooltipModule,
        TagModule
    ]
})
export class NaikanTags {

  private static readonly TAGS_TO_SHOW = 5;

  @Input() tags: string[];

  tagsToShow(): number {
    return NaikanTags.TAGS_TO_SHOW
  }
}

