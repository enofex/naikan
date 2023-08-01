import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Token} from "./token";
import {Page, Pageables} from "@naikan/shared";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'administration/tokens';

@Injectable()
export class AdministrationTokenService {

  constructor(private readonly http: HttpClient) {
  }

  getTokens(event?: TableLazyLoadEvent): Observable<Page<Token>> {
    return this.http.get<Page<Token>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  saveToken(description: string): Observable<any> {
    return this.http.post(`/${endpoint}`, description);
  }

  deleteToken(id: string): Observable<any> {
    return this.http.delete(`/${endpoint}/${id}`);
  }
}