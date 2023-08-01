import {inject} from '@angular/core';
import {HttpErrorResponse, HttpInterceptorFn, HttpStatusCode} from '@angular/common/http';
import {throwError} from 'rxjs';
import {Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import {Principal} from "@naikan/shared";

export const statusInterceptor: HttpInterceptorFn = (req, next) => {

  let principal = inject(Principal);
  let router = inject(Router);

  return next(req).pipe(
      catchError((error: any) => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === HttpStatusCode.Unauthorized) {
            principal.authenticate(null);
            router.navigate(['login']);
          } else if (principal.isAuthenticated()) {
            router.navigate([`error/${error.status}`]);
          }
        }

        return throwError(() => error);
      }));
}
