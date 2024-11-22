import {Directive, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef} from '@angular/core';

import {Subscription} from 'rxjs';
import {map} from 'rxjs/operators';
import {Principal} from "./principal.service";

@Directive({
  selector: '[naikanHasAnyAuthority]'
})
export class AuthorityDirective implements OnInit, OnDestroy {

  private authorities: string[];
  private subscription: Subscription;

  constructor(
      private readonly principal: Principal,
      protected readonly templateRef: TemplateRef<any>,
      protected readonly viewContainerRef: ViewContainerRef
  ) {
  }

  @Input('naikanHasAnyAuthority')
  set hasAnyAuthority(value: string | string[]) {
    this.authorities = typeof value === 'string' ? [value] : value;
    this.principal
    .hasAnyAuthority(this.authorities)
    .subscribe(allow => this.updateView(allow));
  }

  private updateView(allow: boolean): void {
    this.viewContainerRef.clear();
    if (allow) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    }
  }

  ngOnInit(): void {
    this.subscription = this.principal.getAuthenticationState()
    .pipe(map(() => this.principal.hasAnyAuthorityDirect(this.authorities)))
    .subscribe((a) => this.updateView(a));
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
