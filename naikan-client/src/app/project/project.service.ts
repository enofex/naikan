import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BlockUIService, Bom, Page, Pageables, Principal, User} from '../shared';
import {finalize, Observable} from 'rxjs';
import {FilterMatchMode} from 'primeng/api';
import {ProjectFilter} from "./project-filter";
import {FilterMetadata} from "primeng/api/filtermetadata";
import {TableLazyLoadEvent} from "primeng/table";
import {saveAs} from 'file-saver';

const endpoint = 'projects';

@Injectable()
export class ProjectService {

  user: User;

  constructor(private readonly http: HttpClient,
              private readonly principal: Principal,
              private readonly blockUIService: BlockUIService) {
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

    return this.http.get<Page<Bom>>(`/${endpoint}`, {
      params: Pageables.toPageRequestHttpParams(event)
    });
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

  exportXsxl(id: string) {
    return this.http.get(`/${endpoint}/${id}?xlsx`, {
      responseType: 'blob'
    })
    .pipe(this.blockUIService.blockUntil())
    .subscribe(res => saveAs(res, `${id}.xlsx`, {autoBom: false}));
  }

  exportJson(id: string) {
    return this.http.get(`/${endpoint}/${id}`, {
      responseType: 'blob'
    })
    .pipe(this.blockUIService.blockUntil())
    .subscribe(res => saveAs(res, `${id}.json`, {autoBom: false}));
  }

  exportAll(event?: TableLazyLoadEvent) {
    return this.http.get(`/${endpoint}?xlsx`, {
      responseType: 'blob',
      params: Pageables.toAllPagesRequestHttpParams(event)
    })
    .pipe(this.blockUIService.blockUntil())
    .subscribe(res => saveAs(res, `${endpoint}.xlsx`, {autoBom: false}));
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
