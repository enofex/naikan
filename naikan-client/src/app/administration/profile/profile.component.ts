import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Breadcrumb, Principal, Url, User} from "@naikan/shared";
import {NgForOf, NgIf} from "@angular/common";
import {TagModule} from "primeng/tag";

@Component({
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [RouterLink, Breadcrumb, NgIf, Url, NgForOf, TagModule]
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