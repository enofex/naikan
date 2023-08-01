import {Injectable} from '@angular/core';
import {AuthService} from "@naikan/shared";
import {from, Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {Router} from "@angular/router";

@Injectable()
export class LoginService {

  constructor(
      private readonly authService: AuthService,
      private readonly router: Router,
  ) {
  }

  login(credentials): Observable<boolean> {
    return this.authService.login(credentials).pipe(
        switchMap(({path, params}) =>
            from(this.router.navigate([
                  path ? `/${path}`
                      : '/projects'],
                {queryParams: params}))));
  }
}
