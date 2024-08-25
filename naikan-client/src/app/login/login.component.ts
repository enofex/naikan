import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {LoginService} from './login.service';
import {LayoutService} from "@naikan/layout/app.layout.service";
import {ButtonModule} from 'primeng/button';
import {PasswordModule} from 'primeng/password';
import {InputTextModule} from 'primeng/inputtext';
import {JsonPipe} from "@angular/common";
import {MessagesModule} from "primeng/messages";
import {MessageService} from "primeng/api";

@Component({
  selector: 'login-page',
  templateUrl: './login.component.html',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, InputTextModule, PasswordModule, ButtonModule, JsonPipe, MessagesModule],
  providers: [MessageService, LoginService]
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  constructor(
      public readonly layoutService: LayoutService,
      private readonly loginService: LoginService,
      private readonly messageService: MessageService,
      private readonly router: Router,
      private readonly fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      'username': [null, Validators.required],
      'password': [null, Validators.required]
    });
  }

  onLogin(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loginService.login(this.loginForm.value)
    .subscribe({
      error: (err) => {
        this.messageService.clear();

        if (err.error?.detail) {
          this.messageService.add({
            severity: 'error',
            detail: err.error.detail
          });
        }
      }
    });
  }
}
