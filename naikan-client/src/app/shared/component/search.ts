import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {Table} from "primeng/table";

@Component({
  selector: 'naikan-search',
  template: `
    <div class="p-inputgroup md:w-30rem">
      <button type="button" pButton icon="pi pi-search" styleClass="p-button-success"></button>

      <input #searchInput pInputText (input)="onGlobalFilter($event)" maxlength="200"
             type="text" placeholder="Search" class="w-full"/>

      <button type="button" pButton icon="pi pi-times" styleClass="p-button-success"
              tooltipPosition="top" pTooltip="Clear"
              (click)="clear()"></button>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: true,
  imports: [
    ButtonModule,
    InputTextModule,
    TooltipModule,
  ],
})
export class Search {
  @ViewChild('searchInput') searchInput: ElementRef<HTMLInputElement>;
  @Input() table!: Table;

  clear(): void {
    this.table.filterGlobal('', undefined);

    if (this.searchInput) {
      this.searchInput.nativeElement.value = '';
    }
  }

  onGlobalFilter(event: Event): void {
    if (event && event.target) {
      this.table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }
  }
}


