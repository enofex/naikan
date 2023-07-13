import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';

@Component({
  selector: 'naikan-tags',
  template: `
    <span *ngIf="tags" tooltipPosition="top" pTooltip="{{ tags }}">
      <ng-container *ngFor="let tag of tags.slice(0, tagsToShow());">
        <p-tag severity="info" value="{{tag}}" class="mr-1"></p-tag>
      </ng-container>
      <ng-container *ngIf="tags.length > tagsToShow()">
        <p-tag severity="info" value="..." class="mr-1"></p-tag>
      </ng-container>
    </span>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgIf,
    TooltipModule,
    NgFor,
    TagModule,
  ],
})
export class NaikanTags {

  private static readonly TAGS_TO_SHOW = 5;

  @Input() tags: string[];

  tagsToShow(): number {
    return NaikanTags.TAGS_TO_SHOW
  }
}

