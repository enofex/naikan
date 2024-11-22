import {DatePipe as AngularDatePipe} from '@angular/common';
import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'naikanDateTime'
})
export class DateTimePipe implements PipeTransform {

  constructor(private readonly datePipe: AngularDatePipe) {
  }

  transform(value: any): string | null {
    return this.datePipe.transform(value, 'medium');
  }
}
