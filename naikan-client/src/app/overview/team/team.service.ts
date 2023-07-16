import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Page, Pageables, Team} from '../../shared';
import {Observable} from 'rxjs';
import {OverviewTopGroups} from '../overview-top-groups';
import {OverviewGroup} from "../overview";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/teams';

@Injectable()
export class TeamService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewGroup<Team>>> {
    return this.http.get<Page<OverviewGroup<Team>>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  getTopGroups(topN: number): Observable<OverviewTopGroups> {
    return this.http.get<OverviewTopGroups>(`/${endpoint}/top/${topN}`);
  }
}
