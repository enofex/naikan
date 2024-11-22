import {Component, forwardRef, HostBinding, Input, OnDestroy, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive} from '@angular/router';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Subscription} from 'rxjs';
import {filter} from 'rxjs/operators';
import {MenuService} from './app.menu.service';
import {NgClass} from '@angular/common';

@Component({
    selector: '[app-menuitem]',
    template: `
    <ng-container>
        @if (root && item.visible !== false) {
            <div
                    class="layout-menuitem-root-text">{{ item.label }}
            </div>
        }
        @if ((!item.routerLink || item.items) && item.visible !== false) {
            <a [attr.href]="item.url"
               (click)="itemClick($event)"
               [ngClass]="item.class" [attr.target]="item.target" tabindex="0">
                <i [ngClass]="item.icon" class="layout-menuitem-icon"></i>
                <span class="layout-menuitem-text">{{ item.label }}</span>
                @if (item.items) {
                    <i class="pi pi-fw pi-angle-down layout-submenu-toggler"></i>
                }
            </a>
        }
        @if ((item.routerLink && !item.items) && item.visible !== false) {
            <a
                    (click)="itemClick($event)"
                    [ngClass]="item.class"
                    [routerLink]="item.routerLink" routerLinkActive="active-route"
                    [routerLinkActiveOptions]="item.routerLinkActiveOptions||{ paths: 'exact', queryParams: 'ignored', matrixParams: 'ignored', fragment: 'ignored' }"
                    [fragment]="item.fragment" [queryParamsHandling]="item.queryParamsHandling"
                    [preserveFragment]="item.preserveFragment"
                    [skipLocationChange]="item.skipLocationChange" [replaceUrl]="item.replaceUrl"
                    [state]="item.state"
                    [queryParams]="item.queryParams"
                    [attr.target]="item.target" tabindex="0">
                <i [ngClass]="item.icon" class="layout-menuitem-icon"></i>
                <span class="layout-menuitem-text">{{ item.label }}</span>
                @if (item.items) {
                    <i class="pi pi-fw pi-angle-down layout-submenu-toggler"></i>
                }
            </a>
        }
        @if (item.items && item.visible !== false) {
            <ul [@children]="submenuAnimation">
                @for (child of item.items;track child;let i = $index) {
                    <li app-menuitem [item]="child" [index]="i" [parentKey]="key"
                        [class]="child.badgeClass"></li>
                }
            </ul>
        }
    </ng-container>
  `,
    animations: [
        trigger('children', [
            state('collapsed', style({
                height: '0'
            })),
            state('expanded', style({
                height: '*'
            })),
            transition('collapsed <=> expanded', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ],
    imports: [NgClass, RouterLinkActive, RouterLink, forwardRef(() => AppMenuitemComponent)]
})
export class AppMenuitemComponent implements OnInit, OnDestroy {

  @Input() item: any;

  @Input() index!: number;

  @Input() @HostBinding('class.layout-root-menuitem') root!: boolean;

  @Input() parentKey!: string;

  active = false;

  menuSourceSubscription: Subscription;

  menuResetSubscription: Subscription;

  key: string = '';

  constructor(private readonly router: Router, private readonly menuService: MenuService) {
    this.menuSourceSubscription = this.menuService.menuSource$.subscribe(value => {
      Promise.resolve(null).then(() => {
        if (value.routeEvent) {
          this.active = (value.key === this.key || value.key.startsWith(this.key + '-'));
        } else {
          if (value.key !== this.key && !value.key.startsWith(this.key + '-')) {
            this.active = false;
          }
        }
      });
    });

    this.menuResetSubscription = this.menuService.resetSource$.subscribe(() => {
      this.active = false;
    });

    this.router.events.pipe(filter(event => event instanceof NavigationEnd))
    .subscribe(() => {
      if (this.item.routerLink) {
        this.updateActiveStateFromRoute();
      }
    });
  }

  ngOnInit(): void {
    this.key = this.parentKey ? this.parentKey + '-' + this.index : String(this.index);

    if (this.item.routerLink) {
      this.updateActiveStateFromRoute();
    }
  }

  updateActiveStateFromRoute(): void {
    let activeRoute = this.router.isActive(this.item.routerLink[0], {
      paths: 'exact',
      queryParams: 'ignored',
      matrixParams: 'ignored',
      fragment: 'ignored'
    });

    if (activeRoute) {
      this.menuService.onMenuStateChange({key: this.key, routeEvent: true});
    }
  }

  itemClick(event: Event): void {
    if (this.item.disabled) {
      event.preventDefault();
      return;
    }

    if (this.item.command) {
      this.item.command({originalEvent: event, item: this.item});
    }

    if (this.item.items) {
      this.active = !this.active;
    }

    this.menuService.onMenuStateChange({key: this.key});
  }

  get submenuAnimation(): string {
    return this.root ? 'expanded' : (this.active ? 'expanded' : 'collapsed');
  }

  @HostBinding('class.active-menuitem')
  get activeClass() {
    return this.active && !this.root;
  }

  ngOnDestroy(): void {
    if (this.menuSourceSubscription) {
      this.menuSourceSubscription.unsubscribe();
    }

    if (this.menuResetSubscription) {
      this.menuResetSubscription.unsubscribe();
    }
  }
}
