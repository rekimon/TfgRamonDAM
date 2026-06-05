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
import { MatChipsModule } from '@angular/material/chips';
import { AlertaService } from '../../../core/services/alerta';
import { ParcelaService } from '../../../core/services/parcela';
import { Alerta } from '../../../core/models/alerta';
import { Parcela } from '../../../core/models/parcela';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-alerta-list',
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
    MatChipsModule
  ],
  template: `
    <div class="page-container">

      <div class="page-header">
        <div>
          <h1>Alertas</h1>
          <p>Monitoriza las alertas de tus parcelas</p>
        </div>
      </div>

      <div class="filtros">
        <mat-form-field appearance="outline">
          <mat-label>Parcela</mat-label>
          <mat-select [formControl]="parcelaCtrl"
                      (selectionChange)="cargar()">
            <mat-option [value]="">Todas</mat-option>
            @for (p of parcelas(); track p.id) {
              <mat-option [value]="p.id">{{ p.nombre }}</mat-option>
            }
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Estado</mat-label>
          <mat-select [formControl]="estadoCtrl"
                      (selectionChange)="cargar()">
            <mat-option [value]="">Todos</mat-option>
            <mat-option value="ACTIVA">Activa</mat-option>
            <mat-option value="RECONOCIDA">Reconocida</mat-option>
            <mat-option value="RESUELTA">Resuelta</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Severidad</mat-label>
          <mat-select [formControl]="severidadCtrl"
                      (selectionChange)="cargar()">
            <mat-option [value]="null">Todas</mat-option>
            <mat-option value="CRITICA">Critica</mat-option>
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
} @else if (alertas().length === 0) {
  <div class="empty-state">
    <mat-icon class="ok">check_circle</mat-icon>
    <h3>Sin alertas</h3>
    <p>No hay alertas con los filtros seleccionados</p>
  </div>
      } @else {
        <div class="alertas-list">
          @for (alerta of alertas(); track alerta.id) {
            <mat-card class="alerta-card"
                      [class]="'alerta-' + alerta.severidad.toLowerCase()">
              <mat-card-content>
                <div class="alerta-row">
                  <div class="alerta-icon">
                    <mat-icon>
                      {{ alerta.severidad === 'CRITICA' ? 'emergency' :
                         alerta.severidad === 'ALTA' ? 'warning' :
                         'notifications' }}
                    </mat-icon>
                  </div>

                  <div class="alerta-info">
                    <div class="alerta-titulo">
                      <strong>{{ alerta.tipoAlerta }}</strong>
                      <span class="badge"
                            [class]="'badge-' + alerta.severidad.toLowerCase()">
                        {{ alerta.severidad }}
                      </span>
                      <span class="badge badge-origen">
                        {{ alerta.tipoOrigen }}
                      </span>
                      <span class="badge"
                            [class]="'badge-estado-' + alerta.estado.toLowerCase()">
                        {{ alerta.estado }}
                      </span>
                    </div>
                    <p class="alerta-mensaje">{{ alerta.mensaje }}</p>
                    <small>
                      <mat-icon>terrain</mat-icon>
                      {{ alerta.parcelaNombre }}
                      @if (alerta.cultivoNombre) {
                        · {{ alerta.cultivoNombre }}
                      }
                      · {{ alerta.fechaDisparo | date:'dd/MM/yyyy HH:mm' }}
                    </small>
                  </div>

                  <div class="alerta-acciones">
                    @if (alerta.estado === 'ACTIVA') {
                      <button mat-stroked-button
                              (click)="reconocer(alerta)">
                        <mat-icon>done</mat-icon>
                        Reconocer
                      </button>
                      <button mat-raised-button color="primary"
                              (click)="resolver(alerta)">
                        <mat-icon>done_all</mat-icon>
                        Resolver
                      </button>
                    }
                    @if (alerta.estado === 'RECONOCIDA') {
                      <button mat-raised-button color="primary"
                              (click)="resolver(alerta)">
                        <mat-icon>done_all</mat-icon>
                        Resolver
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
        &.ok { color: #2e7d32; opacity: 0.7; }
      }

      h3 { margin: 0; color: #555; }
      p { margin: 0; }
    }

    .alertas-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .alerta-card {
      border-radius: 12px;
      border-left: 5px solid;

      &.alerta-critica { border-color: #c62828; }
      &.alerta-alta { border-color: #e65100; }
      &.alerta-media { border-color: #f9a825; }
      &.alerta-baja { border-color: #558b2f; }
    }

    .alerta-row {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }

    .alerta-icon {
      mat-icon {
        font-size: 32px;
        width: 32px;
        height: 32px;
        color: #666;
      }
    }

    .alerta-info {
      flex: 1;

      .alerta-titulo {
        display: flex;
        align-items: center;
        gap: 8px;
        flex-wrap: wrap;
        margin-bottom: 4px;

        strong { font-size: 15px; }
      }

      .alerta-mensaje {
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

    .alerta-acciones {
      display: flex;
      flex-direction: column;
      gap: 8px;
      min-width: 120px;
    }

    .badge {
      font-size: 11px;
      padding: 2px 8px;
      border-radius: 12px;
      font-weight: 600;
    }

    .badge-critica { background: #ffcdd2; color: #c62828; }
    .badge-alta { background: #ffe0b2; color: #e65100; }
    .badge-media { background: #fff9c4; color: #f57f17; }
    .badge-baja { background: #dcedc8; color: #558b2f; }
    .badge-origen { background: #e3f2fd; color: #1565c0; }
    .badge-estado-activa { background: #ffcdd2; color: #c62828; }
    .badge-estado-reconocida { background: #fff9c4; color: #f57f17; }
    .badge-estado-resuelta { background: #dcedc8; color: #558b2f; }
  `]
})
export class AlertaList implements OnInit {

  alertas = signal<Alerta[]>([]);
  parcelas = signal<Parcela[]>([]);
  cargando = signal(false);

  parcelaCtrl = new FormControl<number | null>(null);
  estadoCtrl = new FormControl<string | null>(null);
  severidadCtrl = new FormControl<string | null>(null);

  constructor(
    private alertaService: AlertaService,
    private parcelaService: ParcelaService,
    private snackBar: MatSnackBar
  ) {}

ngOnInit(): void {
  this.parcelaService.listar().subscribe({
    next: res => {
      if (res.data) {
        this.parcelas.set(res.data.content);
        this.cargar();
      }
    }
  });
}

cargar(): void {
  const parcelaId = this.parcelaCtrl.value;
  this.cargando.set(true);
  const estado = this.estadoCtrl.value || undefined;
  const severidad = this.severidadCtrl.value || undefined;

  if (!parcelaId) {
    const parcelas = this.parcelas();
    if (parcelas.length === 0) {
      this.alertas.set([]);
      this.cargando.set(false);
      return;
    }

    forkJoin(
      parcelas.map(p =>
        this.alertaService.listarPorParcela(p.id, estado, severidad)
      )
    ).subscribe({
      next: resultados => {
        const todas = resultados.flatMap(r => r.data?.content ?? []);
        this.alertas.set(todas);
        this.cargando.set(false);
      },
      error: () => {
        this.alertas.set([]);
        this.cargando.set(false);
      }
    });
    return;
  }

  this.alertaService.listarPorParcela(parcelaId, estado, severidad)
    .subscribe({
      next: res => {
        if (res.data) this.alertas.set(res.data.content);
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });
}

  reconocer(alerta: Alerta): void {
    this.alertaService.reconocer(alerta.id).subscribe({
      next: () => {
        this.snackBar.open('Alerta reconocida', 'Cerrar',
          { duration: 3000 });
        this.cargar();
      }
    });
  }

  resolver(alerta: Alerta): void {
    this.alertaService.resolver(alerta.id).subscribe({
      next: () => {
        this.snackBar.open('Alerta resuelta', 'Cerrar',
          { duration: 3000 });
        this.cargar();
      }
    });
  }
}