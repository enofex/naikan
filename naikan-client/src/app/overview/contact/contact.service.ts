import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Contact, Page, Pageables} from '../../shared';
import {OverviewGroup} from "../overview";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'overview/contacts';

@Injectable()
export class ContactService {

  constructor(private readonly http: HttpClient) {
  }

  getOverviews(event?: TableLazyLoadEvent): Observable<Page<OverviewGroup<Contact>>> {
    return this.http.get<Page<OverviewGroup<Contact>>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }
}
