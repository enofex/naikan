import {Injectable} from '@angular/core';

@Injectable()
export class Charts {

  public static documentPrimaryStyle(): string {
    return getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500');
  }

  public static documentPrimaryStyleWithDefaultOpacity(): string {
    return getComputedStyle(document.documentElement).getPropertyValue('--p-primary-500') + "15";
  }

  public static documentGreenStyle(): string {
    return getComputedStyle(document.documentElement).getPropertyValue('--p-green-500');
  }

  public static documentGreenStyleWithDefaultOpacity(): string {
    return getComputedStyle(document.documentElement).getPropertyValue('--p-green-500') + "15";
  }
}