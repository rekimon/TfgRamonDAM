import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AuthService } from '../../../core/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  template: `
    <div class="login-container">
      <mat-card class="login-card">
        <mat-card-header>
          <div class="logo-section">
            <mat-icon class="logo-icon">eco</mat-icon>
            <h1>AgroGestión</h1>
            <p>Gestión inteligente de cultivos</p>
          </div>
        </mat-card-header>

        <mat-card-content>
          <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Email</mat-label>
              <input matInput type="email" formControlName="email"
                     placeholder="admin@agrogestion.com">
              <mat-icon matPrefix>email</mat-icon>
              @if (loginForm.get('email')?.hasError('required') &&
                   loginForm.get('email')?.touched) {
                <mat-error>El email es obligatorio</mat-error>
              }
              @if (loginForm.get('email')?.hasError('email')) {
                <mat-error>Formato de email invalido</mat-error>
              }
            </mat-form-field>

            <mat-form-field appearance="outline" class="full-width">
              <mat-label>Contrasena</mat-label>
              <input matInput [type]="ocultarPassword() ? 'password' : 'text'"
                     formControlName="password">
              <mat-icon matPrefix>lock</mat-icon>
              <button mat-icon-button matSuffix type="button"
                      (click)="ocultarPassword.set(!ocultarPassword())">
                <mat-icon>
                  {{ ocultarPassword() ? 'visibility_off' : 'visibility' }}
                </mat-icon>
              </button>
              @if (loginForm.get('password')?.hasError('required') &&
                   loginForm.get('password')?.touched) {
                <mat-error>La contrasena es obligatoria</mat-error>
              }
            </mat-form-field>

            @if (errorMensaje()) {
              <div class="error-alert">
                <mat-icon>error</mat-icon>
                <span>{{ errorMensaje() }}</span>
              </div>
            }

            <button mat-raised-button color="primary"
                    type="submit" class="full-width submit-btn"
                    [disabled]="loginForm.invalid || cargando()">
              @if (cargando()) {
                <mat-spinner diameter="20"></mat-spinner>
              } @else {
                <mat-icon>login</mat-icon>
                Iniciar sesion
              }
            </button>

          </form>
        </mat-card-content>

        <mat-card-footer>
          <p class="registro-link">
            ¿No tienes cuenta?
            <a routerLink="/registro">Solicitar acceso</a>
          </p>
        </mat-card-footer>
      </mat-card>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 50%, #43a047 100%);
      padding: 16px;
    }

    .login-card {
      width: 100%;
      max-width: 420px;
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(0,0,0,0.3);
    }

    .logo-section {
      width: 100%;
      text-align: center;
      padding: 24px 0 8px;

      .logo-icon {
        font-size: 48px;
        width: 48px;
        height: 48px;
        color: #2e7d32;
      }

      h1 {
        margin: 8px 0 4px;
        font-size: 28px;
        font-weight: 700;
        color: #1b5e20;
      }

      p {
        margin: 0;
        color: #666;
        font-size: 14px;
      }
    }

    mat-card-content {
      padding: 16px 24px;
    }

    .full-width {
      width: 100%;
    }

    .submit-btn {
      margin-top: 8px;
      height: 48px;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    .error-alert {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px;
      background: #ffebee;
      border-radius: 8px;
      color: #c62828;
      margin-bottom: 16px;
      font-size: 14px;
    }

    mat-card-footer {
      padding: 16px 24px;
      text-align: center;

      .registro-link {
        margin: 0;
        color: #666;
        font-size: 14px;

        a {
          color: #2e7d32;
          font-weight: 600;
          text-decoration: none;

          &:hover {
            text-decoration: underline;
          }
        }
      }
    }
  `]
})
export class Login {

  loginForm: FormGroup;
  cargando = signal(false);
  ocultarPassword = signal(true);
  errorMensaje = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.cargando.set(true);
    this.errorMensaje.set('');

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorMensaje.set(
          err.error?.mensaje ?? 'Error al iniciar sesion'
        );
      }
    });
  }
}