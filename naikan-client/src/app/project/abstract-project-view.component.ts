import {Component, Input, OnInit} from "@angular/core";
import {Principal, User} from "@naikan/shared";
import {ProjectService} from "./project.service";
import {BomOverview} from "./bom-overview";

@Component({template: ''})
export abstract class AbstractProjectView implements OnInit {

  user: User;
  @Input() bomOverview: BomOverview;

  protected constructor(
      private readonly projectService: ProjectService,
      private readonly principal: Principal) {
  }

  ngOnInit(): void {
    this.principal.identity()
    .subscribe({
      next: user => {
        this.user = user;
      }
    });
  }

  tooltipProject(bomOverview: BomOverview): string {
    let tooltip = "";

    if (bomOverview) {
      let found = false;

      if (bomOverview.organization?.name) {
        tooltip += bomOverview.organization?.name;
        found = true;
      }

      if (bomOverview.organization?.department) {
        if (found) {
          tooltip += ", ";
        }
        tooltip += bomOverview.organization?.department;
        found = true;
      }

      if (bomOverview.project?.description) {
        if (found) {
          tooltip += "<br><br>";
        }

        tooltip += bomOverview.project?.description;
      }
    }

    return tooltip;
  }

  tooltipNames(objects: any[]): string {
    let tooltip = "";

    if (objects) {
      let found = 0;

      for (let i = 0; i < objects.length; i++) {
        const name = objects[i]['name'];

        if (name) {
          found++;
          tooltip += name;

          if (found === 10) {
            tooltip += " ...";
            break;
          }

          if (i < objects.length - 1) {
            tooltip += ", ";
          }
        }
      }
    }

    return tooltip;
  }

  onFavoriteToggle(id: string): void {
    if (this.user.favorites) {
      this.user.favorites = this.user.favorites.includes(id)
          ? this.user.favorites.filter(item => item !== id)
          : [...this.user.favorites, id];
    } else {
      this.user.favorites = [id];
    }

    this.projectService
    .updateUserFavorites(this.user.favorites)
    .subscribe();
  }

  isFavorite(id: string): boolean {
    return this.user.favorites?.includes(id);
  }
}