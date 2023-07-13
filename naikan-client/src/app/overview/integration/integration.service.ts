import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Integration, Page, Pageables} from '../../shared';
import {OverviewGroup} from "../overview";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/integrations';

@Injectable()
export class IntegrationService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewGroup<Integration>>> {
    return this.http.get<Page<OverviewGroup<Integration>>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }
}
