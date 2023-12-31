import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';
import {AuthorityDirective} from "@naikan/shared";

@Component({
  templateUrl: './administration.component.html',
  standalone: true,
  imports: [RouterLink, AuthorityDirective]
})
export class AdministrationComponent {
}