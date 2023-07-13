import {Injectable, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {Observable, Subject} from 'rxjs';
import {switchMap} from 'rxjs/operators';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Principal} from "./principal.service";

@Injectable({
  providedIn: 'root',
})
export class AuthService implements OnDestroy {

  private readonly logoutSubject = new Subject<void>();
  public readonly onLogout = this.logoutSubject.asObservable();

  constructor(
      private readonly router: Router,
      private readonly http: HttpClient,
      private readonly principal: Principal,
  ) {
  }

  login(credentials: { [key: string]: string }): Observable<any> {
    const options = {
      headers: new HttpHeaders()
      .set('Content-Type', 'application/x-www-form-urlencoded')
    };

    const data = Object.entries(credentials)
    .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
    .join('&');

    return this.http.post('/authenticate', data, options)
    .pipe(switchMap(() => this.principal.identity(true)));
  }

  logout(): void {
    this.http.post('/logout', null).subscribe();
    this.principal.authenticate(null);
    this.router.navigate(['login']);
    this.logoutSubject.next();
  }

  ngOnDestroy(): void {
    this.logoutSubject.complete();
  }
}
