import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Page, Pageables} from '../../shared';
import {OverviewTopGroups} from '../overview-top-groups';
import {OverviewGroup} from "../overview";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/tags';

@Injectable()
export class TagService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewGroup>> {
    return this.http.get<Page<OverviewGroup>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getTopGroups(topN: number): Observable<OverviewTopGroups> {
    return this.http.get<OverviewTopGroups>(`/${endpoint}/top/${topN}`);
  }
}
