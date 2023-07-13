import {importProvidersFrom} from '@angular/core';
import {AppComponent} from './app/app.component';
import {APP_ROUTES} from './app/app-routing.module';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideAnimations} from "@angular/platform-browser/animations";
import {statusInterceptor} from "./app/shared/interceptor/status.interceptor";
import {environmentInterceptor} from "./app/shared/interceptor/environment.interceptor";
import {PreloadAllModules, provideRouter, withPreloading} from "@angular/router";

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(BrowserModule),
    provideHttpClient(
        withInterceptors([statusInterceptor, environmentInterceptor])
    ),
    provideAnimations(),
    provideRouter(
        APP_ROUTES,
        withPreloading(PreloadAllModules)
    ),
  ]
})
.catch(err => console.error(err));
