import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {BlockUIModule} from "primeng/blockui";
import {BlockUIService, NaikanColorsPlugin} from "@naikan/shared";
import {AsyncPipe} from "@angular/common";
import {Chart} from "chart.js";


@Component({
    selector: 'naikan-root',
    templateUrl: './app.component.html',
    imports: [RouterOutlet, BlockUIModule, AsyncPipe],
    providers: [BlockUIService]
})
export class AppComponent {

  constructor(public blockUIService: BlockUIService) {
    Chart.defaults.animation = false;
    Chart.defaults.responsive = false;
    Chart.register(NaikanColorsPlugin)
  }
}