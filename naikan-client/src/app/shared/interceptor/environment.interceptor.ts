import {HttpInterceptorFn} from '@angular/common/http';
import {environment} from "../../../environments/environment";

export const environmentInterceptor: HttpInterceptorFn = (req, next) => {

  const URL = `${environment.apiUrl}${req.url}`;

  req = req.clone({
    url: URL,
    withCredentials: true,
  });

  return next(req);
}
