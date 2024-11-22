import {Component} from '@angular/core';
import {LayoutService} from './app.layout.service';
import {environment} from 'src/environments/environment';

@Component({
  selector: 'app-footer',
  templateUrl: './app.footer.component.html'
})
export class AppFooterComponent {

  protected readonly environment = environment;

  constructor(public readonly layoutService: LayoutService) {
  }
}
