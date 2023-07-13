import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Page, Pageables} from "../../shared";
import {User} from "./user";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'administration/users';

@Injectable()
export class AdministrationUserService {

  constructor(private readonly http: HttpClient) {
  }

  getUsers(event?: TableLazyLoadEvent): Observable<Page<User>> {
    return this.http.get<Page<User>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  updateUserAuthorities(id: string, authorities: string[]): Observable<any> {
    return this.http.patch(`/${endpoint}/${id}`, authorities);
  }

  deleteUser(id: string): Observable<any> {
    return this.http.delete(`/${endpoint}/${id}`);
  }
}