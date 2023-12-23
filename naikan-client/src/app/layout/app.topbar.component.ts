import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {LayoutService} from './app.layout.service';
import {AuthService, Principal, User} from "@naikan/shared";
import {MenuModule} from 'primeng/menu';
import {TooltipModule} from 'primeng/tooltip';
import {ButtonModule} from 'primeng/button';
import {NgClass} from '@angular/common';
import {RouterLink} from '@angular/router';
import {finalize} from "rxjs";

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  standalone: true,
  imports: [RouterLink, NgClass, ButtonModule, TooltipModule, MenuModule]
})
export class AppTopBarComponent implements OnInit {

  user: User;
  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(public readonly layoutService: LayoutService,
              private readonly authService: AuthService,
              private readonly principal: Principal) {
  }

  ngOnInit(): void {
    this.principal.identity()
    .pipe(finalize(() => this.initItems()))
    .subscribe({
      next: user => {
        this.user = user;
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }

  private initItems(): void {
    this.items = [
      {
        label: this.user.username,
        routerLink: '/administration/profile'
      },
      {
        separator: true
      },
      {
        label: 'Administration',
        routerLink: '/administration'
      },
      {
        separator: true
      },
      {
        label: 'Logout',
        command: (): void => {
          this.logout();
        }
      }
    ];
  }
}
