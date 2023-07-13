import {Injectable} from '@angular/core';

@Injectable()
export class Charts {

  public static documentStyle(): string {
    return getComputedStyle(document.documentElement).getPropertyValue('--primary-500');
  }

}