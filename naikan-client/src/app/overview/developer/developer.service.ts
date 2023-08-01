import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Page, Pageables} from "@naikan/shared";
import {OverviewGroup} from "../overview";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/developers';

@Injectable()
export class DeveloperService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewGroup>> {
    return this.http.get<Page<OverviewGroup>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }
}
