import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { UsuarioService } from '../../../core/services/usuario';
import { Usuario } from '../../../core/models/usuario';
import { UsuarioDetail } from '../usuario-detail/usuario-detail';
import { RolPipe } from '../../../core/pipes/rol-pipe';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatTableModule,
    MatChipsModule,
    RolPipe
  ],
  template: `
    <div class="page-container">

      <div class="page-header">
        <div>
          <h1>Administracion de Usuarios</h1>
          <p>Gestion de accesos y roles de la plataforma</p>
        </div>
      </div>

      <div class="filtros">
        <mat-form-field appearance="outline">
          <mat-label>Buscar</mat-label>
          <input matInput [formControl]="busquedaCtrl"
                 placeholder="Nombre, email...">
          <mat-icon matPrefix>search</mat-icon>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Estado</mat-label>
          <mat-select [formControl]="estadoCtrl"
                      (selectionChange)="cargar()">
            <mat-option [value]="null">Todos</mat-option>
            <mat-option value="PENDIENTE">Pendiente</mat-option>
            <mat-option value="ACTIVO">Activo</mat-option>
            <mat-option value="INACTIVO">Inactivo</mat-option>
            <mat-option value="RECHAZADO">Rechazado</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Rol</mat-label>
          <mat-select [formControl]="rolCtrl"
                      (selectionChange)="cargar()">
            <mat-option [value]="null">Todos</mat-option>
            <mat-option value="ROLE_OWNER">Jefe</mat-option>
            <mat-option value="ROLE_WORKER">Trabajador</mat-option>
            <mat-option value="ROLE_ADMIN">Administrador</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      @if (pendientes().length > 0) {
        <mat-card class="pendientes-banner">
          <mat-card-content>
            <div class="banner-content">
              <mat-icon>pending_actions</mat-icon>
              <span>
                <strong>{{ pendientes().length }}</strong>
                solicitudes pendientes de aprobacion
              </span>
              <button mat-raised-button color="warn"
                      (click)="estadoCtrl.setValue('PENDIENTE'); cargar()">
                Ver pendientes
              </button>
            </div>
          </mat-card-content>
        </mat-card>
      }

      @if (cargando()) {
        <div class="loading">
          <mat-spinner diameter="48"></mat-spinner>
        </div>
      } @else {
        <table mat-table [dataSource]="usuarios()" class="usuarios-table">

          <ng-container matColumnDef="nombre">
            <th mat-header-cell *matHeaderCellDef>Nombre</th>
            <td mat-cell *matCellDef="let u">
              <div class="user-cell">
                <mat-icon class="user-icon">account_circle</mat-icon>
                <div>
                  <strong>{{ u.nombre }} {{ u.apellidos }}</strong>
                  <small>{{ u.email }}</small>
                </div>
              </div>
            </td>
          </ng-container>

          <ng-container matColumnDef="rol">
            <th mat-header-cell *matHeaderCellDef>Rol</th>
            <td mat-cell *matCellDef="let u">
              @if (u.rol) {
                <span class="badge badge-rol">
                  {{ u.rol | rol }}
                </span>
              } @else {
                <span class="badge badge-sin-rol">Sin rol</span>
              }
            </td>
          </ng-container>

          <ng-container matColumnDef="estado">
            <th mat-header-cell *matHeaderCellDef>Estado</th>
            <td mat-cell *matCellDef="let u">
              <span class="badge"
                    [class]="'badge-estado-' + u.estado.toLowerCase()">
                {{ u.estado }}
              </span>
            </td>
          </ng-container>

          <ng-container matColumnDef="acceso">
            <th mat-header-cell *matHeaderCellDef>Ultimo acceso</th>
            <td mat-cell *matCellDef="let u">
              {{ u.ultimoAcceso ? (u.ultimoAcceso | date:'dd/MM/yyyy HH:mm')
                                : 'Nunca' }}
            </td>
          </ng-container>

          <ng-container matColumnDef="acciones">
            <th mat-header-cell *matHeaderCellDef>Acciones</th>
            <td mat-cell *matCellDef="let u">
              <button mat-icon-button color="primary"
                      (click)="verDetalle(u)"
                      title="Gestionar usuario">
                <mat-icon>manage_accounts</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columnas"></tr>
          <tr mat-row *matRowDef="let row; columns: columnas;"
              class="usuario-row"></tr>
        </table>

        @if (usuarios().length === 0) {
          <div class="empty-state">
            <mat-icon>people</mat-icon>
            <p>No hay usuarios con los filtros seleccionados</p>
          </div>
        }
      }
    </div>
  `,
  styles: [`
    .page-container { max-width: 1200px; margin: 0 auto; }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 24px;

      h1 { margin: 0; font-size: 28px; font-weight: 700; color: #1b5e20; }
      p { margin: 4px 0 0; color: #666; }
    }

    .filtros {
      display: flex;
      gap: 16px;
      margin-bottom: 24px;
      flex-wrap: wrap;

      mat-form-field { min-width: 180px; }
    }

    .pendientes-banner {
      margin-bottom: 24px;
      border-radius: 12px;
      background: #fff3e0;
      border-left: 4px solid #e65100;

      .banner-content {
        display: flex;
        align-items: center;
        gap: 12px;

        mat-icon { color: #e65100; }
      }
    }

    .loading {
      display: flex;
      justify-content: center;
      padding: 64px;
    }

    .usuarios-table {
      width: 100%;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
    }

    .usuario-row:hover { background: #f5f5f5; }

    .user-cell {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 8px 0;

      .user-icon {
        font-size: 36px;
        width: 36px;
        height: 36px;
        color: #999;
      }

      strong { display: block; font-size: 14px; }
      small { font-size: 12px; color: #888; }
    }

    .empty-state {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      padding: 48px;
      color: #999;

      mat-icon {
        font-size: 32px;
        width: 32px;
        height: 32px;
        opacity: 0.3;
      }
    }

    .badge {
      font-size: 11px;
      padding: 3px 10px;
      border-radius: 12px;
      font-weight: 600;
    }

    .badge-rol { background: #e8f5e9; color: #1b5e20; }
    .badge-sin-rol { background: #f5f5f5; color: #999; }
    .badge-estado-pendiente { background: #fff9c4; color: #f57f17; }
    .badge-estado-activo { background: #dcedc8; color: #558b2f; }
    .badge-estado-inactivo { background: #f5f5f5; color: #999; }
    .badge-estado-rechazado { background: #ffcdd2; color: #c62828; }
  `]
})
export class UsuarioList implements OnInit {

  usuarios = signal<Usuario[]>([]);
  pendientes = signal<Usuario[]>([]);
  cargando = signal(true);
  columnas = ['nombre', 'rol', 'estado', 'acceso', 'acciones'];

  busquedaCtrl = new FormControl('');
  estadoCtrl = new FormControl<string | null>(null);
  rolCtrl = new FormControl<string | null>(null);

  constructor(
    private usuarioService: UsuarioService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cargarPendientes();

    this.busquedaCtrl.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(() => this.cargar());
  }

  cargar(): void {
    this.cargando.set(true);
    this.usuarioService.listar(
      this.estadoCtrl.value ?? undefined,
      this.rolCtrl.value ?? undefined,
      this.busquedaCtrl.value ?? undefined
    ).subscribe({
      next: res => {
        if (res.data) this.usuarios.set(res.data.content);
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });
  }

  cargarPendientes(): void {
    this.usuarioService.listar('PENDIENTE').subscribe({
      next: res => {
        if (res.data) this.pendientes.set(res.data.content);
      }
    });
  }

  verDetalle(usuario: Usuario): void {
    const ref = this.dialog.open(UsuarioDetail, {
      width: '480px',
      data: usuario
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) {
        this.cargar();
        this.cargarPendientes();
      }
    });
  }
}