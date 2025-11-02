import {Injectable, WritableSignal} from '@angular/core';

@Injectable()
export class Errors {

  static toSuccessMessage(messages: WritableSignal<any[]>, err: string, key?: string) {
    Errors.toMessage(messages, err, 'success', key);
  }

  static toErrorMessage(messages: WritableSignal<any[]>, err: string, key?: string) {
    Errors.toMessage(messages, err, 'error', key);
  }

  static toMessage(messages: WritableSignal<any[]>, err: string, severity: string, key?: string) {
    if (messages && err) {
      messages.set([]);

      messages.set([
        {
          key: key,
          severity: severity,
          content: err
        },
      ]);
    }
  }
}
