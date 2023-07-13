import {Component, ElementRef} from '@angular/core';
import {AppMenuComponent} from './app.menu.component';

@Component({
  selector: 'app-sidebar',
  templateUrl: './app.sidebar.component.html',
  standalone: true,
  imports: [AppMenuComponent]
})
export class AppSidebarComponent {
  constructor(public readonly el: ElementRef) {
  }
}

