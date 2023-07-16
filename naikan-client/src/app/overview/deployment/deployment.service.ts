import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Page, Pageables} from "../../shared";
import {OverviewDeployment} from "./overview-deployment";
import {OverviewTopGroups} from "../overview-top-groups";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/deployments';

@Injectable()
export class DeploymentService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewDeployment>> {
    return this.http.get<Page<OverviewDeployment>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getTopGroups(topN: number): Observable<OverviewTopGroups> {
    return this.http.get<OverviewTopGroups>(`/${endpoint}/top/${topN}`);
  }
}
