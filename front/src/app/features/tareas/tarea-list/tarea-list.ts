import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { TareaService } from '../../../core/services/tarea';
import { ParcelaService } from '../../../core/services/parcela';
import { AuthService } from '../../../core/services/auth';
import { Tarea } from '../../../core/models/tarea';
import { Parcela } from '../../../core/models/parcela';
import { TareaForm } from '../tarea-form/tarea-form';

@Component({
  selector: 'app-tarea-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule
  ],
  template: `
    <div class="page-container">

      <div class="page-header">
        <div>
          <h1>Tareas</h1>
          <p>Planificacion y seguimiento de tareas agricolas</p>
        </div>
        @if (authService.isOwner() || authService.isAdmin()) {
          <button mat-raised-button color="primary"
                  (click)="abrirFormulario()"
                  [disabled]="!parcelaCtrl.value">
            <mat-icon>add</mat-icon>
            Nueva tarea
          </button>
        }
      </div>

      <div class="filtros">
        <mat-form-field appearance="outline">
          <mat-label>Parcela</mat-label>
          <mat-select [formControl]="parcelaCtrl"
                      (selectionChange)="cargar()">
            <mat-option value="">Todas</mat-option>
            @for (p of parcelas(); track p.id) {
              <mat-option [value]="p.id">{{ p.nombre }}</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Estado</mat-label>
          <mat-select [formControl]="estadoCtrl"
                      (selectionChange)="cargar()">
            <mat-option value="">Todos</mat-option>
            <mat-option value="PENDIENTE">Pendiente</mat-option>
            <mat-option value="EN_PROGRESO">En progreso</mat-option>
            <mat-option value="COMPLETADA">Completada</mat-option>
            <mat-option value="CANCELADA">Cancelada</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Prioridad</mat-label>
          <mat-select [formControl]="prioridadCtrl"
                      (selectionChange)="cargar()">
            <mat-option value="">Todas</mat-option>
            <mat-option value="URGENTE">Urgente</mat-option>
            <mat-option value="ALTA">Alta</mat-option>
            <mat-option value="MEDIA">Media</mat-option>
            <mat-option value="BAJA">Baja</mat-option>
          </mat-select>
        </mat-form-field>
      </div>

      @if (cargando()) {
        <div class="loading">
          <mat-spinner diameter="48"></mat-spinner>
        </div>
      } @else if (tareas().length === 0) {
        <div class="empty-state">
          <mat-icon>task_alt</mat-icon>
          <h3>Sin tareas</h3>
          <p>No hay tareas con los filtros seleccionados</p>
        </div>
      } @else {
        <div class="tareas-list">
          @for (tarea of tareas(); track tarea.id) {
            <mat-card class="tarea-card">
              <mat-card-content>
                <div class="tarea-row">
                  <div class="tarea-tipo-icon">
                    <mat-icon>{{ iconoTipo(tarea.tipo) }}</mat-icon>
                  </div>

                  <div class="tarea-info">
                    <div class="tarea-titulo">
                      <strong>{{ tarea.titulo }}</strong>
                      <span class="badge"
                            [class]="'badge-' + tarea.prioridad.toLowerCase()">
                        {{ tarea.prioridad }}
                      </span>
                      <span class="badge"
                            [class]="'badge-estado-' + tarea.estado.toLowerCase()">
                        {{ tarea.estado }}
                      </span>
                    </div>
                    <p class="tarea-desc">{{ tarea.descripcion }}</p>
                    <small>
                      <mat-icon>terrain</mat-icon>
                      {{ tarea.parcelaNombre }}
                      · {{ tarea.tipo }}
                      · Prevista: {{ tarea.fechaPrevista | date:'dd/MM/yyyy' }}
                      @if (tarea.asignadoANombre) {
                        · {{ tarea.asignadoANombre }}
                      }
                    </small>
                  </div>

                  <div class="tarea-acciones">
                    @if (authService.isOwner() || authService.isAdmin()) {
                      <button mat-icon-button title="Editar y asignar"
                              (click)="editarTarea(tarea)">
                        <mat-icon>edit</mat-icon>
                      </button>
                      <button mat-icon-button color="warn"
                              title="Eliminar tarea"
                              (click)="eliminarTarea(tarea)">
                        <mat-icon>delete</mat-icon>
                      </button>
                    }
                    @if (tarea.estado === 'PENDIENTE') {
                      <button mat-stroked-button color="primary"
                              (click)="cambiarEstado(tarea, 'EN_PROGRESO')">
                        <mat-icon>play_arrow</mat-icon>
                        Iniciar
                      </button>
                    }
                    @if (tarea.estado === 'EN_PROGRESO') {
                      <button mat-raised-button color="primary"
                              (click)="cambiarEstado(tarea, 'COMPLETADA')">
                        <mat-icon>done</mat-icon>
                        Completar
                      </button>
                    }
                  </div>
                </div>
              </mat-card-content>
            </mat-card>
          }
        </div>
      }
    </div>
  `,
  styles: [`
    .page-container { max-width: 1100px; margin: 0 auto; }

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

      mat-form-field { min-width: 160px; }
    }

    .loading {
      display: flex;
      justify-content: center;
      padding: 64px;
    }

    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 64px;
      color: #999;
      gap: 12px;

      mat-icon {
        font-size: 64px;
        width: 64px;
        height: 64px;
        opacity: 0.3;
      }

      h3 { margin: 0; color: #555; }
      p { margin: 0; }
    }

    .tareas-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .tarea-card { border-radius: 12px; }

    .tarea-row {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }

    .tarea-tipo-icon {
      mat-icon {
        font-size: 32px;
        width: 32px;
        height: 32px;
        color: #2e7d32;
      }
    }

    .tarea-info {
      flex: 1;

      .tarea-titulo {
        display: flex;
        align-items: center;
        gap: 8px;
        flex-wrap: wrap;
        margin-bottom: 4px;

        strong { font-size: 15px; }
      }

      .tarea-desc {
        margin: 4px 0;
        font-size: 14px;
        color: #444;
      }

      small {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: #888;

        mat-icon { font-size: 12px; width: 12px; height: 12px; }
      }
    }

    .tarea-acciones {
      display: flex;
      align-items: center;
      gap: 8px;
      flex-wrap: wrap;
    }

    .badge {
      font-size: 11px;
      padding: 2px 8px;
      border-radius: 12px;
      font-weight: 600;
    }

    .badge-urgente { background: #ffcdd2; color: #c62828; }
    .badge-alta { background: #ffe0b2; color: #e65100; }
    .badge-media { background: #fff9c4; color: #f57f17; }
    .badge-baja { background: #dcedc8; color: #558b2f; }
    .badge-estado-pendiente { background: #e3f2fd; color: #1565c0; }
    .badge-estado-en_progreso { background: #fff9c4; color: #f57f17; }
    .badge-estado-completada { background: #dcedc8; color: #558b2f; }
    .badge-estado-cancelada { background: #f5f5f5; color: #999; }
  `]
})
export class TareaList implements OnInit {

  tareas = signal<Tarea[]>([]);
  parcelas = signal<Parcela[]>([]);
  cargando = signal(false);

  parcelaCtrl = new FormControl('');
  estadoCtrl = new FormControl('');
  prioridadCtrl = new FormControl('');

  constructor(
    public authService: AuthService,
    private tareaService: TareaService,
    private parcelaService: ParcelaService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.parcelaService.listar().subscribe({
      next: res => {
        if (res.data) this.parcelas.set(res.data.content);
      }
    });
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    const parcelaId = this.parcelaCtrl.value
      ? Number(this.parcelaCtrl.value) : undefined;
    const estado = this.estadoCtrl.value || undefined;
    const prioridad = this.prioridadCtrl.value || undefined;

    this.tareaService.listar(parcelaId, estado, prioridad)
      .subscribe({
        next: res => {
          if (res.data) this.tareas.set(res.data.content);
          this.cargando.set(false);
        },
        error: () => this.cargando.set(false)
      });
  }

  abrirFormulario(): void {
    const parcelaId = this.parcelaCtrl.value;

    if (!parcelaId) {
      this.snackBar.open(
        'Selecciona una parcela antes de crear una tarea',
        'Cerrar', { duration: 3000 });
      return;
    }

    const ref = this.dialog.open(TareaForm, {
      width: '520px',
      data: { parcelaId: Number(parcelaId) }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargar();
    });
  }

  editarTarea(tarea: Tarea): void {
    const ref = this.dialog.open(TareaForm, {
      width: '520px',
      data: {
        parcelaId: tarea.parcelaId,
        tarea: tarea
      }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargar();
    });
  }

  eliminarTarea(tarea: Tarea): void {
    if (!confirm(`¿Eliminar la tarea "${tarea.titulo}"?`)) return;

    this.tareaService.eliminar(tarea.id).subscribe({
      next: () => {
        this.snackBar.open('Tarea eliminada', 'Cerrar',
          { duration: 3000 });
        this.cargar();
      },
      error: err => {
        this.snackBar.open(
          err.error?.mensaje ?? 'Error al eliminar',
          'Cerrar', { duration: 3000 });
      }
    });
  }

  cambiarEstado(tarea: Tarea, estado: string): void {
    this.tareaService.actualizar(tarea.id, { estado: estado as any })
      .subscribe({
        next: () => {
          this.snackBar.open(`Tarea ${estado.toLowerCase()}`,
            'Cerrar', { duration: 3000 });
          this.cargar();
        },
        error: err => {
          this.snackBar.open(
            err.error?.mensaje ?? 'Error al actualizar tarea',
            'Cerrar', { duration: 3000 });
        }
      });
  }

  iconoTipo(tipo: string): string {
    const iconos: Record<string, string> = {
      RIEGO: 'water_drop',
      FERTILIZACION: 'science',
      PODA: 'content_cut',
      RECOLECCION: 'agriculture',
      MANTENIMIENTO: 'build',
      OTRO: 'more_horiz'
    };
    return iconos[tipo] ?? 'task';
  }
}