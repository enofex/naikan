import {Injectable} from "@angular/core";

@Injectable()
export class ProjectFilters {

  public static hasFilters(filters): boolean {
    for (const key in filters) {
      if (key === "value") {
        const value = filters[key];
        if (value !== null && value !== undefined) {
          return true;
        }
      }

      if (typeof filters[key] === "object") {
        if (this.hasFilters(filters[key])) {
          return true;
        }
      }
    }

    return false;
  }

  public static hasFiltersForKeys(filters: any, targetKeys: string[]): boolean {
    if (typeof filters !== 'object' || filters === null) {
      return false;
    }

    for (const key in filters) {
      for (const target of targetKeys) {
        if (key.startsWith(target) && filters[key]?.value !== null && filters[key]?.value !== undefined) {
          return true;
        }
      }

      if (typeof filters[key] === 'object' && ProjectFilters.hasFiltersForKeys(filters[key], targetKeys)) {
        return true;
      }
    }

    return false;
  }
}