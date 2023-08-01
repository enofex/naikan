import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Page, Pageables} from "@naikan/shared";
import {Project} from "./project";
import {TableLazyLoadEvent} from "primeng/table";

const endpoint = 'administration/projects';

@Injectable()
export class AdministrationProjectService {

  constructor(private readonly http: HttpClient) {
  }

  getProjects(event?: TableLazyLoadEvent): Observable<Page<Project>> {
    return this.http.get<Page<Project>>(`/${endpoint}`, {params: Pageables.toPageRequestHttpParams(event)});
  }

  deleteProject(id: string): Observable<any> {
    return this.http.delete(`/${endpoint}/${id}`);
  }
}