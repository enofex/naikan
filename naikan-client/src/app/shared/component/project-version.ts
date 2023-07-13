import {ChangeDetectionStrategy, Component, Input, ViewEncapsulation} from '@angular/core';
import {NgIf} from '@angular/common';
import {Project} from '../model/bom';
import {TagModule} from 'primeng/tag';
import {TooltipModule} from 'primeng/tooltip';

@Component({
  selector: 'naikan-project-version',
  template: `
    <p-tag *ngIf="project && project.version"
           severity="success"
           class="{{ class }}"
           value="{{ project?.version }}"
           tooltipPosition="top"
           [escape]="false"
           pTooltip="{{ tooltip() }}">
    </p-tag>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    NgIf,
    TagModule,
    TooltipModule,
  ],
})
export class ProjectVersion {
  @Input() project: Project;
  @Input() class: string;

  tooltip(): string {
    let tooltip;

    if (this.project?.groupId) {
      tooltip = "Group: " + this.project.groupId + "<br>"
    }

    if (this.project?.artifactId) {
      tooltip = tooltip + "Artifact: " + this.project.artifactId + "<br>"
    }

    if (this.project?.packaging) {
      tooltip = tooltip + "Packaging: " + this.project.packaging
    }

    return tooltip;
  }
}


