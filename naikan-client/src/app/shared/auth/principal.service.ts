import {Injectable} from '@angular/core';
import {Observable, of, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from "./user";
import {catchError, map} from "rxjs/operators";

@Injectable({
  providedIn: 'root',
})
export class Principal {

  private userIdentity: User;
  private authenticated = false;

  private readonly authenticationState = new Subject<User>();

  constructor(private readonly http: HttpClient) {
  }

  authenticate(identity: User): void {
    this.userIdentity = identity;
    this.authenticated = !!identity;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[]): Observable<boolean> {
    return this.isAuthenticated()
        ? this.identity().pipe(map(() => this.hasAnyAuthorityDirect(authorities)))
        : of(false);
  }

  hasAnyAuthorityDirect(authorities: string[]): boolean {
    if (!this.userIdentity?.authorities) {
      return false;
    }
    for (const authority of authorities) {
      if (this.hasAuthority(authority)) {
        return true;
      }
    }
    return false;
  }

  private hasAuthority(authority: string): boolean {
    return this.userIdentity.authorities.indexOf(authority) !== -1;
  }

  identity(force = false): Observable<User> {
    if (force === true) {
      this.userIdentity = undefined;
    }

    if (this.userIdentity) {
      return of(this.userIdentity);
    }
    return this.http.get<User>('/authenticated')
    .pipe(
        map((response: User) => {
          this.userIdentity = response;
          this.authenticated = !!this.userIdentity;
          this.authenticationState.next(this.userIdentity);
          return this.userIdentity;
        }),
        catchError(() => {
          this.userIdentity = undefined;
          this.authenticated = false;
          this.authenticationState.next(this.userIdentity);
          return of(undefined);
        }),
    );
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  getAuthenticationState(): Observable<User> {
    return this.authenticationState.asObservable();
  }
}
