import {Component, OnInit} from '@angular/core';
import {AppMenuitemComponent} from './app.menuitem.component';
import {NgFor, NgIf} from '@angular/common';

@Component({
  selector: 'app-menu',
  templateUrl: './app.menu.component.html',
  standalone: true,
  imports: [NgFor, NgIf, AppMenuitemComponent]
})
export class AppMenuComponent implements OnInit {

  model: any[] = [];

  ngOnInit(): void {
    this.model = [
      {
        label: 'Home',
        items: [
          {label: 'Projects', icon: 'pi pi-fw pi-home', routerLink: ['/projects']}
        ]
      },
      {
        label: 'Overviews',
        items: [
          {
            label: 'Environments',
            icon: 'pi pi-fw pi-box',
            routerLink: ['/overview/environments']
          },
          {label: 'Teams', icon: 'pi pi-fw pi-users', routerLink: ['/overview/teams']},
          {label: 'Developers', icon: 'pi pi-fw pi-user', routerLink: ['/overview/developers']},
          {label: 'Contacts', icon: 'pi pi-fw pi-envelope', routerLink: ['/overview/contacts']},
          {label: 'Integrations', icon: 'pi pi-fw pi-link', routerLink: ['/overview/integrations']},
          {label: 'Technologies', icon: 'pi pi-fw pi-code', routerLink: ['/overview/technologies']},
          {
            label: 'Deployments',
            icon: 'pi pi-fw pi-cloud-upload',
            routerLink: ['/overview/deployments']
          }
        ]
      }
    ];
  }
}
