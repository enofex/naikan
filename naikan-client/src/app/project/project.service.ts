import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {
  BlockUIService, Branch,
  Commit,
  Deployment,
  Page,
  Pageables,
  Principal,
  RepositoryTag,
  User
} from "@naikan/shared";
import {finalize, Observable, Subscription} from 'rxjs';
import {FilterMatchMode} from 'primeng/api';
import {ProjectFilter} from "./project-filter";
import {FilterMetadata} from "primeng/api/filtermetadata";
import {TableLazyLoadEvent} from "primeng/table";
import {saveAs} from 'file-saver';
import {
  GroupedDeploymentsPerVersion,
  LatestVersionPerEnvironment
} from "./detail/project-detail.component";
import {BomDetail} from "./bom-detail";
import {BomOverview} from "./bom-overview";
import {CountsPerItems} from "./counts-per-items";
import {
  SuppressExtractedTextChunksWebpackPlugin
} from "@angular-devkit/build-angular/src/tools/webpack/plugins";

const endpoint = 'projects';

@Injectable()
export class ProjectService {

  user: User;

  constructor(private readonly http: HttpClient,
              private readonly principal: Principal,
              private readonly blockUIService: BlockUIService) {
  }

  getBoms(event?: TableLazyLoadEvent, favorites: boolean = false): Observable<Page<BomOverview>> {
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

    return this.http.get<Page<BomOverview>>(`/${endpoint}`, {
      params: Pageables.toPageRequestHttpParams(event)
    });
  }

  getBomDetailById(id: string): Observable<BomDetail> {
    return this.http.get<BomDetail>(`/${endpoint}/${id}`);
  }

  getProjectFilter(): Observable<ProjectFilter> {
    return this.http.get<ProjectFilter>(`/${endpoint}/filter`);
  }

  updateUserFavorites(favorites: string[]): Observable<any> {
    return this.http.patch(`/${endpoint}/favorites`, favorites);
  }

  getProjectDeploymentsById(id: string, event?: TableLazyLoadEvent): Observable<Page<Deployment>> {
    return this.http.get<Page<Deployment>>(`/${endpoint}/${id}/deployments`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getDeploymentsPerMonthById(id: string): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/${id}/deployments/months`);
  }

  getDeploymentsPerMonth(event?: TableLazyLoadEvent): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/deployments/months`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getDeploymentsPerProject(event?: TableLazyLoadEvent): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/deployments/projects`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getGroupedDeploymentsPerVersionById(id: string, event?: TableLazyLoadEvent): Observable<Page<GroupedDeploymentsPerVersion>> {
    return this.http.get<Page<GroupedDeploymentsPerVersion>>(`/${endpoint}/${id}/versions/grouped`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getLatestVersionPerEnvironmentById(id: string): Observable<LatestVersionPerEnvironment[]> {
    return this.http.get<LatestVersionPerEnvironment[]>(`/${endpoint}/${id}/versions/environments`);
  }


  getProjectCommitsById(id: string, event?: TableLazyLoadEvent): Observable<Page<Commit>> {
    return this.http.get<Page<Commit>>(`/${endpoint}/${id}/commits`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getCommitsPerMonth(event?: TableLazyLoadEvent): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/commits/months`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getCommitsPerMonthById(id: string): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/${id}/commits/months`);
  }

  getCommitsPerProject(event?: TableLazyLoadEvent): Observable<CountsPerItems> {
    return this.http.get<CountsPerItems>(`/${endpoint}/commits/projects`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getProjectRepositoryTagsById(id: string, event?: TableLazyLoadEvent): Observable<Page<RepositoryTag>> {
    return this.http.get<Page<RepositoryTag>>(`/${endpoint}/${id}/repository/tags`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getProjectRepositoryBranchesById(id: string, event?: TableLazyLoadEvent): Observable<Page<Branch>> {
    return this.http.get<Page<Branch>>(`/${endpoint}/${id}/repository/branches`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  exportXlsxById(id: string) {
    return this.http.get(`/${endpoint}/${id}?xlsx`, {
      responseType: 'blob'
    })
    .pipe(this.blockUIService.blockUntil())
    .subscribe(res => saveAs(res, `${id}.xlsx`, {autoBom: false}));
  }

  exportJsonById(id: string) {
    return this.http.get(`/${endpoint}/${id}`, {
      responseType: 'blob'
    })
    .pipe(this.blockUIService.blockUntil())
    .subscribe(res => saveAs(res, `${id}.json`, {autoBom: false}));
  }

  exportAll(event?: TableLazyLoadEvent): Subscription {
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
