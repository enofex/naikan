import {Injectable, OnDestroy} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Params,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import {Observable, of, Subject} from 'rxjs';
import {map, switchMap, takeUntil} from 'rxjs/operators';
import {Principal} from "./principal.service";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate, OnDestroy {

  private readonly destroy = new Subject<void>();

  private guardedPath: string;
  private guardedParams: Params;

  constructor(
      private readonly router: Router,
      private readonly authService: AuthService,
      private readonly principal: Principal
  ) {
    this.authService.onLogout.pipe(takeUntil(this.destroy)).subscribe(() => {
      this.guardedPath = undefined;
      this.guardedParams = {};
    });
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
    this.guardedPath = state.url.split('?')[0];
    this.guardedParams = route.queryParams;
    return this.checkPermissions(route.data["authorities"]);
  }

  private checkPermissions(authorities: string[]): Observable<boolean | UrlTree> {
    return this.principal.identity(false)
    .pipe(switchMap((identity) => {
      if (!identity) {
        return of(this.router.createUrlTree(['/login']));
      }
      if (this.isEmpty(authorities)) {
        return of(true);
      }
      return this.principal.hasAnyAuthority(authorities)
      .pipe(map(allowed => allowed ? true : this.router.createUrlTree(['/error/403'])));
    }));
  }

  private isEmpty(authorities: string[]): boolean {
    return !authorities || authorities.length === 0;
  }

  ngOnDestroy(): void {
    this.destroy.complete();
  }
}
