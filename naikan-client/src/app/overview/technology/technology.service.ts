import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Page, Pageables} from "@naikan/shared";
import {OverviewTopGroups} from "../overview-top-groups";
import {TableLazyLoadEvent} from "primeng/table";
import {TechnologyGroup} from "./overview";

const endpoint = 'overview/technologies';

@Injectable()
export class TechnologyService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<TechnologyGroup>> {
    return this.http.get<Page<TechnologyGroup>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getTopGroups(topN: number): Observable<OverviewTopGroups> {
    return this.http.get<OverviewTopGroups>(`/${endpoint}/top/${topN}`);
  }
}
