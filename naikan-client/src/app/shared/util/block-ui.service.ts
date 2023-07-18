import {Injectable} from '@angular/core';
import {BehaviorSubject, MonoTypeOperatorFunction, Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class BlockUIService {

  private readonly isBlockedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public readonly isBlocked: Observable<boolean> = this.isBlockedSubject.asObservable();

  public blockUntil<T>(): MonoTypeOperatorFunction<T> {
    this.block();

    return tap<T>({
      next: () => this.unblock(),
      error: () => this.unblock(),
      complete: () => this.unblock()
    });
  }

  private block(): void {
    this.isBlockedSubject.next(true);
  }

  private unblock(): void {
    this.isBlockedSubject.next(false);
  }
}

