import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {LoginService} from './login.service';
import {ButtonModule} from 'primeng/button';
import {PasswordModule} from 'primeng/password';
import {InputTextModule} from 'primeng/inputtext';
import {Errors} from "@naikan/shared";
import {Message} from "primeng/message";

@Component({
  selector: 'login-page',
  templateUrl: './login.component.html',
  imports: [FormsModule, ReactiveFormsModule, InputTextModule, PasswordModule, ButtonModule, Message],
  providers: [LoginService]
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  messages: WritableSignal<any[]> = signal([]);

  constructor(
      private readonly loginService: LoginService,
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
        if (err.error?.detail) {
          Errors.toErrorMessage(this.messages, err.error.detail);
        }
      }
    });
  }
}
