import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Bom, Page, Pageables, Principal, User} from '../shared';
import {finalize, Observable} from 'rxjs';
import {FilterMatchMode} from 'primeng/api';
import {ProjectFilter} from "./project-filter";
import {FilterMetadata} from "primeng/api/filtermetadata";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'projects';

@Injectable()
export class ProjectService {

  user: User;

  constructor(private readonly http: HttpClient,
              private readonly principal: Principal) {
  }

  getBoms(event?: TableLazyLoadEvent, favorites: boolean = false): Observable<Page<Bom>> {
    if (event) {
      (<FilterMetadata[]>event.filters['_id']) = [];
    }

    if (favorites) {
      this.principal.identity()
      .pipe(finalize(() => this.addFavoritesFilter(event)))
      .subscribe({
        next: user => {
          this.user = user;
        }
      });
    }

    return this.http.get<Page<Bom>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getBom(id: string): Observable<Bom> {
    return this.http.get<Bom>(`/${endpoint}/${id}`);
  }

  getProjectFilter(): Observable<ProjectFilter> {
    return this.http.get<ProjectFilter>(`/${endpoint}/filter`);
  }

  updateUserFavorites(favorites: string[]): Observable<any> {
    return this.http.patch(`/${endpoint}/favorites`, favorites);
  }

  private addFavoritesFilter(event: TableLazyLoadEvent): void {
    if (event && this.user && this.user.favorites) {
      this.user.favorites.forEach(favorite => {
        (<FilterMetadata[]>event.filters['_id']).push({
          value: favorite,
          matchMode: FilterMatchMode.EQUALS,
          operator: "or"
        })
      })
    }
  }
}
