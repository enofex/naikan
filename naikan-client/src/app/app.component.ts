import {Component} from '@angular/core';
import {PrimeNGConfig} from 'primeng/api';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'naikan-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [RouterOutlet]
})
export class AppComponent {

  constructor(private primengConfig: PrimeNGConfig) {
    primengConfig.ripple = false;
  }
}
