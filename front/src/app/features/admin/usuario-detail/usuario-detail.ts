import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { MatTabsModule } from '@angular/material/tabs';
import { UsuarioService } from '../../../core/services/usuario';
import { Usuario } from '../../../core/models/usuario';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { ApiResponse } from '../../../core/models/api-response';
import { RolPipe } from '../../../core/pipes/rol-pipe';

@Component({
  selector: 'app-usuario-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatDividerModule,
    MatTabsModule,
    RolPipe
  ],
  template: `
    <h2 mat-dialog-title>
      <mat-icon>manage_accounts</mat-icon>
      Gestionar Usuario
    </h2>

    <mat-dialog-content>
      <div class="usuario-info">
        <mat-icon class="avatar">account_circle</mat-icon>
        <div>
          <h3>{{ usuario.nombre }} {{ usuario.apellidos }}</h3>
          <p>{{ usuario.email }}</p>
          <div class="badges">
            <span class="badge"
                  [class]="'badge-estado-' + usuario.estado.toLowerCase()">
              {{ usuario.estado }}
            </span>
            @if (usuario.rol) {
              <span class="badge badge-rol">
                {{ usuario.rol | rol }}
              </span>
            }
          </div>
        </div>
      </div>

      <mat-divider></mat-divider>

      <mat-tab-group>

        <!-- TAB EDITAR DATOS -->
        <mat-tab label="Editar datos">
          <div class="tab-content">
            <form [formGroup]="editForm" class="form-grid">

              <mat-form-field appearance="outline">
                <mat-label>Nombre</mat-label>
                <input matInput formControlName="nombre">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Apellidos</mat-label>
                <input matInput formControlName="apellidos">
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Telefono</mat-label>
                <input matInput formControlName="telefono">
                <mat-icon matPrefix>phone</mat-icon>
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Rol</mat-label>
                <mat-select formControlName="rol">
                  <mat-option value="ROLE_OWNER">Jefe</mat-option>
                  <mat-option value="ROLE_WORKER">Trabajador</mat-option>
                  <mat-option value="ROLE_ADMIN">Administrador</mat-option>
                </mat-select>
              </mat-form-field>

              <mat-form-field appearance="outline">
                <mat-label>Estado</mat-label>
                <mat-select formControlName="estado">
                  <mat-option value="ACTIVO">Activo</mat-option>
                  <mat-option value="INACTIVO">Inactivo</mat-option>
                  <mat-option value="PENDIENTE">Pendiente</mat-option>
                  <mat-option value="RECHAZADO">Rechazado</mat-option>
                </mat-select>
              </mat-form-field>

            </form>

            <button mat-raised-button color="primary"
                    [disabled]="editForm.invalid || cargando()"
                    (click)="guardarCambios()">
              <mat-icon>save</mat-icon>
              Guardar cambios
            </button>
          </div>
        </mat-tab>

        <!-- TAB ACCIONES -->
        <mat-tab label="Acciones">
          <div class="tab-content acciones">

            @if (usuario.estado === 'PENDIENTE') {
              <div class="accion-card pendiente">
                <div>
                  <strong>Aprobar usuario</strong>
                  <p>Activa la cuenta y asigna un rol</p>
                </div>
                <div class="accion-btns">
                  <button mat-raised-button color="primary"
                          [disabled]="cargando()"
                          (click)="aprobar('ROLE_OWNER')">
                    <mat-icon>person</mat-icon>
                    Como Jefe
                  </button>
                  <button mat-stroked-button color="primary"
                          [disabled]="cargando()"
                          (click)="aprobar('ROLE_WORKER')">
                    <mat-icon>engineering</mat-icon>
                    Como Trabajador
                  </button>
                </div>
              </div>
              <mat-divider></mat-divider>
              <button mat-stroked-button color="warn"
                      [disabled]="cargando()"
                      (click)="rechazar()">
                <mat-icon>cancel</mat-icon>
                Rechazar solicitud
              </button>
            }

            @if (usuario.estado === 'ACTIVO') {
              <div class="accion-card">
                <div>
                  <strong>Desactivar cuenta</strong>
                  <p>El usuario no podra iniciar sesion</p>
                </div>
                <button mat-stroked-button color="warn"
                        [disabled]="cargando()"
                        (click)="desactivar()">
                  <mat-icon>block</mat-icon>
                  Desactivar
                </button>
              </div>
            }

            @if (usuario.estado === 'INACTIVO' ||
                 usuario.estado === 'RECHAZADO') {
              <div class="accion-card">
                <div>
                  <strong>Reactivar cuenta</strong>
                  <p>Permite al usuario volver a iniciar sesion</p>
                </div>
                <button mat-raised-button color="primary"
                        [disabled]="cargando()"
                        (click)="activar()">
                  <mat-icon>check_circle</mat-icon>
                  Reactivar
                </button>
              </div>
            }

          </div>
        </mat-tab>

      </mat-tab-group>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">Cerrar</button>
    </mat-dialog-actions>
  `,
  styles: [`
    h2 { display: flex; align-items: center; gap: 8px; color: #1b5e20; }

    mat-dialog-content { min-width: 480px; }

    .usuario-info {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px 0;

      .avatar {
        font-size: 56px;
        width: 56px;
        height: 56px;
        color: #999;
      }

      h3 { margin: 0; font-size: 18px; }
      p { margin: 4px 0; color: #666; font-size: 14px; }

      .badges {
        display: flex;
        gap: 8px;
        margin-top: 4px;
      }
    }

    .tab-content {
      padding: 16px 0;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .form-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0 16px;
      margin-bottom: 16px;
    }

    .acciones { gap: 16px; }

    .accion-card {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
      background: #f5f5f5;
      border-radius: 8px;
      gap: 16px;

      &.pendiente { background: #fff9c4; }

      strong { display: block; font-size: 14px; }
      p { margin: 4px 0 0; font-size: 13px; color: #666; }

      .accion-btns {
        display: flex;
        gap: 8px;
        flex-shrink: 0;
      }
    }

    .badge {
      font-size: 12px;
      padding: 3px 10px;
      border-radius: 12px;
      font-weight: 600;
    }

    .badge-rol { background: #e8f5e9; color: #1b5e20; }
    .badge-estado-pendiente { background: #fff9c4; color: #f57f17; }
    .badge-estado-activo { background: #dcedc8; color: #558b2f; }
    .badge-estado-inactivo { background: #f5f5f5; color: #999; }
    .badge-estado-rechazado { background: #ffcdd2; color: #c62828; }
  `]
})
export class UsuarioDetail implements OnInit {

  cargando = signal(false);
  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<UsuarioDetail>,
    @Inject(MAT_DIALOG_DATA) public usuario: Usuario
  ) {
    this.editForm = this.fb.group({
      nombre: [usuario.nombre, Validators.required],
      apellidos: [usuario.apellidos, Validators.required],
      telefono: [usuario.telefono ?? ''],
      rol: [usuario.rol ?? 'ROLE_WORKER'],
      estado: [usuario.estado]
    });
  }

  ngOnInit(): void {}

  guardarCambios(): void {
    if (this.editForm.invalid) return;
    this.cargando.set(true);

    const { nombre, apellidos, telefono, rol, estado } =
        this.editForm.value;

    this.http.put<ApiResponse<Usuario>>(
      `${environment.apiUrl}/usuarios/${this.usuario.id}/admin`,
      { nombre, apellidos, telefono, rol, estado }
    ).subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open('Usuario actualizado', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: err => {
        this.cargando.set(false);
        this.snackBar.open(
          err.error?.mensaje ?? 'Error al actualizar',
          'Cerrar', { duration: 3000 });
      }
    });
  }

  aprobar(rol: string): void {
    this.cargando.set(true);
    this.usuarioService.aprobar(this.usuario.id, { rol: rol as any })
      .subscribe({
        next: () => {
          this.snackBar.open('Usuario aprobado', 'Cerrar',
            { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: err => {
          this.cargando.set(false);
          this.snackBar.open(
            err.error?.mensaje ?? 'Error',
            'Cerrar', { duration: 3000 });
        }
      });
  }

  rechazar(): void {
    this.cargando.set(true);
    this.usuarioService.rechazar(this.usuario.id).subscribe({
      next: () => {
        this.snackBar.open('Solicitud rechazada', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      }
    });
  }

  desactivar(): void {
    this.cargando.set(true);
    this.usuarioService.desactivar(this.usuario.id).subscribe({
      next: () => {
        this.snackBar.open('Usuario desactivado', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      }
    });
  }

  activar(): void {
    this.cargando.set(true);
    this.usuarioService.activar(this.usuario.id).subscribe({
      next: () => {
        this.snackBar.open('Usuario activado', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      }
    });
  }
}