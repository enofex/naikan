import {importProvidersFrom} from '@angular/core';
import {AppComponent} from './app/app.component';
import {APP_ROUTES} from './app/app.routes';
import {bootstrapApplication, BrowserModule} from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideAnimations} from "@angular/platform-browser/animations";
import {statusInterceptor} from "./app/shared/interceptor/status.interceptor";
import {environmentInterceptor} from "./app/shared/interceptor/environment.interceptor";
import {PreloadAllModules, provideRouter, withPreloading} from "@angular/router";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import { providePrimeNG } from 'primeng/config';
import {NaikanPreset} from "./naikan";


bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(BrowserModule),
    provideHttpClient(
        withInterceptors([statusInterceptor, environmentInterceptor])
    ),
    provideAnimations(),
    provideAnimationsAsync(),
    provideRouter(
        APP_ROUTES,
        withPreloading(PreloadAllModules)
    ),
    providePrimeNG({
      ripple: false,
      theme: {
        preset: NaikanPreset,
        options: {
          cssLayer: {
            name: 'primeng',
            order: 'tailwind-base, primeng, tailwind-utilities, naikan'
          }
        }
      }
    })
  ]
})
.catch(err => console.error(err));
