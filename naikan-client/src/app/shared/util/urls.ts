import {Injectable} from '@angular/core';

@Injectable()
export class Urls {

  public static isValid(url: string): boolean {
    if (!url) {
      return false;
    }

    try {
      return Boolean(new URL(url));
    } catch (e) {
      return false;
    }
  }

}