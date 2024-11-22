import {Component, OnInit} from '@angular/core';
import {Breadcrumb, Principal, User} from "@naikan/shared";

import {TagModule} from "primeng/tag";

@Component({
    templateUrl: './profile.component.html',
    imports: [Breadcrumb, TagModule]
})
export class ProfileComponent implements OnInit {

  user: User;

  constructor(private readonly principal: Principal) {
  }

  ngOnInit(): void {
    this.principal.identity()
    .subscribe({
      next: user => {
        this.user = user;
      }
    });
  }
}