import {Component, Input, OnInit} from "@angular/core";
import {Bom, Principal, User} from "../shared";
import {ProjectService} from "./project.service";

@Component({template: ''})
export abstract class AbstractProjectView implements OnInit {

  user: User;
  @Input() bom: Bom;

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

  tooltipProject(bom: Bom): string {
    let tooltip = "";

    if (bom) {
      let found = false;

      if (bom.organization?.name) {
        tooltip += bom.organization?.name;
        found = true;
      }

      if (bom.organization?.department) {
        if (found) {
          tooltip += ", ";
        }
        tooltip += bom.organization?.department;
        found = true;
      }

      if (bom.project?.description) {
        if (found) {
          tooltip += "<br><br>";
        }

        tooltip += bom.project?.description;
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
    return this.user.favorites && this.user.favorites.includes(id);
  }
}