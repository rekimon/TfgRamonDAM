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
  selector: 'app-registro',
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
    <div class="registro-container">
      <mat-card class="registro-card">
        <mat-card-header>
          <div class="logo-section">
            <mat-icon class="logo-icon">eco</mat-icon>
            <h1>AgroGestión</h1>
            <p>Solicitar acceso a la plataforma</p>
          </div>
        </mat-card-header>

        <mat-card-content>

          @if (exito()) {
            <div class="exito-alert">
              <mat-icon>check_circle</mat-icon>
              <div>
                <strong>Solicitud enviada</strong>
                <p>Tu cuenta esta pendiente de aprobacion por un administrador.</p>
              </div>
            </div>
            <button mat-raised-button color="primary"
                    routerLink="/login" class="full-width">
              Volver al login
            </button>
          } @else {
            <form [formGroup]="registroForm" (ngSubmit)="onSubmit()">

              <div class="form-row">
                <mat-form-field appearance="outline">
                  <mat-label>Nombre</mat-label>
                  <input matInput formControlName="nombre">
                  @if (registroForm.get('nombre')?.hasError('required') &&
                       registroForm.get('nombre')?.touched) {
                    <mat-error>Obligatorio</mat-error>
                  }
                </mat-form-field>

                <mat-form-field appearance="outline">
                  <mat-label>Apellidos</mat-label>
                  <input matInput formControlName="apellidos">
                  @if (registroForm.get('apellidos')?.hasError('required') &&
                       registroForm.get('apellidos')?.touched) {
                    <mat-error>Obligatorio</mat-error>
                  }
                </mat-form-field>
              </div>

              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Email</mat-label>
                <input matInput type="email" formControlName="email">
                <mat-icon matPrefix>email</mat-icon>
                @if (registroForm.get('email')?.hasError('email')) {
                  <mat-error>Email invalido</mat-error>
                }
              </mat-form-field>

              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Telefono (opcional)</mat-label>
                <input matInput formControlName="telefono">
                <mat-icon matPrefix>phone</mat-icon>
              </mat-form-field>

              <mat-form-field appearance="outline" class="full-width">
                <mat-label>Contrasena</mat-label>
                <input matInput type="password" formControlName="password">
                <mat-icon matPrefix>lock</mat-icon>
                @if (registroForm.get('password')?.hasError('minlength')) {
                  <mat-error>Minimo 8 caracteres</mat-error>
                }
                @if (registroForm.get('password')?.hasError('pattern')) {
                  <mat-error>Debe incluir mayusculas, minusculas y numeros</mat-error>
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
                      [disabled]="registroForm.invalid || cargando()">
                @if (cargando()) {
                  <mat-spinner diameter="20"></mat-spinner>
                } @else {
                  Solicitar acceso
                }
              </button>

            </form>
          }
        </mat-card-content>

        <mat-card-footer>
          <p class="login-link">
            ¿Ya tienes cuenta?
            <a routerLink="/login">Iniciar sesion</a>
          </p>
        </mat-card-footer>
      </mat-card>
    </div>
  `,
  styles: [`
    .registro-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 50%, #43a047 100%);
      padding: 16px;
    }

    .registro-card {
      width: 100%;
      max-width: 480px;
      border-radius: 16px;
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

      p { margin: 0; color: #666; font-size: 14px; }
    }

    mat-card-content { padding: 16px 24px; }

    .form-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 16px;
    }

    .full-width { width: 100%; }

    .submit-btn {
      margin-top: 8px;
      height: 48px;
      font-size: 16px;
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

    .exito-alert {
      display: flex;
      align-items: flex-start;
      gap: 12px;
      padding: 16px;
      background: #e8f5e9;
      border-radius: 8px;
      color: #1b5e20;
      margin-bottom: 16px;

      mat-icon { color: #2e7d32; margin-top: 2px; }
      p { margin: 4px 0 0; font-size: 14px; }
    }

    mat-card-footer {
      padding: 16px 24px;
      text-align: center;

      .login-link {
        margin: 0;
        color: #666;
        font-size: 14px;

        a {
          color: #2e7d32;
          font-weight: 600;
          text-decoration: none;
        }
      }
    }
  `]
})
export class Registro {

  registroForm: FormGroup;
  cargando = signal(false);
  exito = signal(false);
  errorMensaje = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: [''],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/)
      ]]
    });
  }

  onSubmit(): void {
    if (this.registroForm.invalid) return;

    this.cargando.set(true);
    this.errorMensaje.set('');

    this.authService.registro(this.registroForm.value).subscribe({
      next: () => {
        this.cargando.set(false);
        this.exito.set(true);
      },
      error: (err) => {
        this.cargando.set(false);
        this.errorMensaje.set(
          err.error?.mensaje ?? 'Error al registrarse'
        );
      }
    });
  }
}