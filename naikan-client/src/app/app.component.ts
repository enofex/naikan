import {Component} from '@angular/core';
import {PrimeNGConfig} from 'primeng/api';
import {RouterOutlet} from '@angular/router';
import {BlockUIModule} from "primeng/blockui";
import {BlockUIService} from "@naikan/shared";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'naikan-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [RouterOutlet, BlockUIModule, AsyncPipe],
  providers: [BlockUIService]
})
export class AppComponent {

  constructor(private primengConfig: PrimeNGConfig, public blockUIService: BlockUIService) {
    primengConfig.ripple = false;
  }
}
