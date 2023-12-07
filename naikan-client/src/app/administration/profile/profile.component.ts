import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Breadcrumb, Principal, Url, User} from "@naikan/shared";

import {TagModule} from "primeng/tag";

@Component({
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [RouterLink, Breadcrumb, Url, TagModule]
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