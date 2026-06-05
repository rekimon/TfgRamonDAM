import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { MatTableModule } from '@angular/material/table';
import { ParcelaService } from '../../../core/services/parcela';
import { CultivoService } from '../../../core/services/cultivo';
import { CosechaService } from '../../../core/services/cosecha';
import { AlertaService } from '../../../core/services/alerta';
import { TareaService } from '../../../core/services/tarea';
import { SensorService } from '../../../core/services/sensor';
import { AuthService } from '../../../core/services/auth';
import { Parcela } from '../../../core/models/parcela';
import { Cultivo } from '../../../core/models/cultivo';
import { Cosecha } from '../../../core/models/cosecha';
import { Alerta } from '../../../core/models/alerta';
import { Tarea } from '../../../core/models/tarea';
import { SensorDatos } from '../../../core/models/sensor-datos';
import { CultivoForm } from '../../cultivos/cultivo-form/cultivo-form';
import { CosechaForm } from '../../cosechas/cosecha-form/cosecha-form';
import { TareaForm } from '../../tareas/tarea-form/tarea-form';
import { SensorForm } from '../../sensores/sensor-form/sensor-form';

@Component({
  selector: 'app-parcela-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatChipsModule,
    MatDividerModule,
    MatTableModule
  ],
  template: `
    <div class="page-container">

      @if (cargando()) {
        <div class="loading">
          <mat-spinner diameter="48"></mat-spinner>
        </div>
      } @else if (parcela()) {

        <div class="page-header">
          <div class="header-left">
            <button mat-icon-button (click)="router.navigate(['/parcelas'])">
              <mat-icon>arrow_back</mat-icon>
            </button>
            <div>
              <h1>{{ parcela()!.nombre }}</h1>
              <p>{{ parcela()!.municipio }}, {{ parcela()!.provincia }}
                 · {{ parcela()!.superficieHa }} ha</p>
            </div>
          </div>
          <div class="header-actions">
            @if (ultimaLectura()) {
              <div class="sensor-mini">
                <span>
                  <mat-icon>thermostat</mat-icon>
                  {{ ultimaLectura()!.temperatura }}°C
                </span>
                <span>
                  <mat-icon>water_drop</mat-icon>
                  {{ ultimaLectura()!.humedadSuelo }}%
                </span>
              </div>
            }
          
          </div>
        </div>

        <mat-tab-group>

          <!-- TAB CULTIVOS -->
          <mat-tab label="Cultivos">
            <div class="tab-content">
              <div class="tab-header">
                <h3>Cultivos activos</h3>
                @if (authService.isOwner() || authService.isAdmin()) {
                  <button mat-raised-button color="primary"
                          (click)="abrirCultivo()">
                    <mat-icon>add</mat-icon>
                    Nuevo cultivo
                  </button>
                }
              </div>

              @if (cultivos().length === 0) {
                <div class="empty-tab">
                  <mat-icon>grass</mat-icon>
                  <p>Sin cultivos registrados</p>
                </div>
              } @else {
                <div class="cultivos-grid">
                  @for (cultivo of cultivos(); track cultivo.id) {
                    <mat-card class="cultivo-card">
                      <mat-card-header>
                        <mat-icon mat-card-avatar class="cultivo-avatar">
                          grass
                        </mat-icon>
                        <mat-card-title>
                          {{ cultivo.nombrePersonalizado ??
                             cultivo.tipoCultivo.nombre }}
                        </mat-card-title>
                        <mat-card-subtitle>
                          {{ cultivo.tipoCultivo.nombre }}
                        </mat-card-subtitle>
                      </mat-card-header>

                      <mat-card-content>
                        <div class="cultivo-info">
                          <span>
                            <mat-icon>calendar_today</mat-icon>
                            Siembra: {{ cultivo.fechaSiembra | date:'dd/MM/yyyy' }}
                          </span>
                          @if (cultivo.fechaCosechaEstimada) {
                            <span>
                              <mat-icon>event</mat-icon>
                              Cosecha est.:
                              {{ cultivo.fechaCosechaEstimada | date:'dd/MM/yyyy' }}
                            </span>
                          }
                          <span class="estado-badge"
                                [class]="'estado-' + cultivo.estado.toLowerCase()">
                            {{ cultivo.estado }}
                          </span>
                        </div>
                      </mat-card-content>

                      <mat-card-actions>
                        @if (authService.isOwner() || authService.isAdmin()) {
                          <button mat-button color="primary"
                                  (click)="abrirCosecha(cultivo.id)">
                            <mat-icon>agriculture</mat-icon>
                            Registrar cosecha
                          </button>
                        }
                        @if (cultivo.estado === 'ACTIVO') {
                          <button mat-button color="accent"
                                  (click)="cambiarEstadoCultivo(cultivo, 'LISTO_COSECHA')">
                            <mat-icon>done</mat-icon>
                            Listo para cosecha
                          </button>
                        }
                        @if (cultivo.estado === 'LISTO_COSECHA') {
                          <div class="badge-listo">
                            <mat-icon>check_circle</mat-icon>
                            Listo para cosechar
                          </div>
                        }
                      </mat-card-actions>
                    </mat-card>
                  }
                </div>
              }
            </div>
          </mat-tab>

          <!-- TAB ALERTAS -->
          <mat-tab label="Alertas">
            <div class="tab-content">
              <div class="tab-header">
                <h3>Alertas de la parcela</h3>
              </div>

              @if (alertas().length === 0) {
                <div class="empty-tab">
                  <mat-icon>check_circle</mat-icon>
                  <p>Sin alertas activas</p>
                </div>
              } @else {
                <div class="alertas-list">
                  @for (alerta of alertas(); track alerta.id) {
                    <div class="alerta-row"
                         [class]="'alerta-' + alerta.severidad.toLowerCase()">
                      <div class="alerta-info">
                        <strong>{{ alerta.tipoAlerta }}</strong>
                        <span>{{ alerta.mensaje }}</span>
                        <small>
                          {{ alerta.fechaDisparo | date:'dd/MM/yyyy HH:mm' }}
                          · {{ alerta.tipoOrigen }}
                        </small>
                      </div>
                      <div class="alerta-actions">
                        <span class="badge"
                              [class]="'badge-' + alerta.severidad.toLowerCase()">
                          {{ alerta.severidad }}
                        </span>
                        @if (alerta.estado === 'ACTIVA') {
                          <button mat-icon-button
                                  (click)="reconocerAlerta(alerta)"
                                  title="Reconocer">
                            <mat-icon>done</mat-icon>
                          </button>
                          <button mat-icon-button color="primary"
                                  (click)="resolverAlerta(alerta)"
                                  title="Resolver">
                            <mat-icon>done_all</mat-icon>
                          </button>
                        }
                      </div>
                    </div>
                  }
                </div>
              }
            </div>
          </mat-tab>

          <!-- TAB TAREAS -->
          <mat-tab label="Tareas">
            <div class="tab-content">
              <div class="tab-header">
                <h3>Tareas de la parcela</h3>
                @if (authService.isOwner() || authService.isAdmin()) {
                  <button mat-raised-button color="primary"
                          (click)="abrirTarea()">
                    <mat-icon>add</mat-icon>
                    Nueva tarea
                  </button>
                }
              </div>

              @if (tareas().length === 0) {
                <div class="empty-tab">
                  <mat-icon>task_alt</mat-icon>
                  <p>Sin tareas registradas</p>
                </div>
              } @else {
                <div class="tareas-list">
                  @for (tarea of tareas(); track tarea.id) {
                    <div class="tarea-row">
                      <div class="tarea-info">
                        <strong>{{ tarea.titulo }}</strong>
                        <span>{{ tarea.tipo }} · {{ tarea.descripcion }}</span>
                        <small>
                          Prevista: {{ tarea.fechaPrevista | date:'dd/MM/yyyy' }}
                          @if (tarea.asignadoANombre) {
                            · Asignada a: {{ tarea.asignadoANombre }}
                          }
                        </small>
                      </div>
                      <div class="tarea-badges">
                        <span class="badge"
                              [class]="'badge-' + tarea.prioridad.toLowerCase()">
                          {{ tarea.prioridad }}
                        </span>
                        <span class="badge badge-estado">
                          {{ tarea.estado }}
                        </span>
                      </div>
                    </div>
                  }
                </div>
              }
            </div>
          </mat-tab>

          <!-- TAB COSECHAS -->
          <mat-tab label="Cosechas">
            <div class="tab-content">
              <div class="tab-header">
                <h3>Historial de cosechas</h3>
              </div>

              @if (cosechas().length === 0) {
                <div class="empty-tab">
                  <mat-icon>agriculture</mat-icon>
                  <p>Sin cosechas registradas</p>
                </div>
              } @else {
                <table mat-table [dataSource]="cosechas()"
                       class="cosecha-table">

                  <ng-container matColumnDef="fecha">
                    <th mat-header-cell *matHeaderCellDef>Fecha</th>
                    <td mat-cell *matCellDef="let c">
                      {{ c.fechaCosecha | date:'dd/MM/yyyy' }}
                    </td>
                  </ng-container>

                  <ng-container matColumnDef="cultivo">
                    <th mat-header-cell *matHeaderCellDef>Cultivo</th>
                    <td mat-cell *matCellDef="let c">{{ c.cultivoNombre }}</td>
                  </ng-container>

                  <ng-container matColumnDef="kg">
                    <th mat-header-cell *matHeaderCellDef>Kg</th>
                    <td mat-cell *matCellDef="let c">{{ c.kgObtenidos }}</td>
                  </ng-container>

                  <ng-container matColumnDef="precio">
                    <th mat-header-cell *matHeaderCellDef>€/kg</th>
                    <td mat-cell *matCellDef="let c">{{ c.precioPorKg }}</td>
                  </ng-container>

                  <ng-container matColumnDef="ingreso">
                    <th mat-header-cell *matHeaderCellDef>Ingreso total</th>
                    <td mat-cell *matCellDef="let c">
                      {{ c.ingresoTotal | currency:'EUR' }}
                    </td>
                  </ng-container>

                  <ng-container matColumnDef="calidad">
                    <th mat-header-cell *matHeaderCellDef>Calidad</th>
                    <td mat-cell *matCellDef="let c">{{ c.calidad }}</td>
                  </ng-container>

                  <ng-container matColumnDef="acciones">
                    <th mat-header-cell *matHeaderCellDef>PDF</th>
                    <td mat-cell *matCellDef="let c">
                      <button mat-icon-button color="primary"
                              (click)="exportarPdf(c.id)"
                              title="Exportar a PDF">
                        <mat-icon>picture_as_pdf</mat-icon>
                      </button>
                    </td>
                  </ng-container>

                  <tr mat-header-row *matHeaderRowDef="colsCosecha"></tr>
                  <tr mat-row
                      *matRowDef="let row; columns: colsCosecha;"></tr>

                </table>
              }
            </div>
          </mat-tab>

        </mat-tab-group>
      }
    </div>
  `,
  styles: [`
    .page-container { max-width: 1200px; margin: 0 auto; }

    .loading {
      display: flex;
      justify-content: center;
      padding: 64px;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      .header-left {
        display: flex;
        align-items: center;
        gap: 8px;

        h1 {
          margin: 0;
          font-size: 24px;
          font-weight: 700;
          color: #1b5e20;
        }

        p { margin: 4px 0 0; color: #666; font-size: 14px; }
      }

      .header-actions {
        display: flex;
        align-items: center;
        gap: 16px;
      }

      .sensor-mini {
        display: flex;
        gap: 12px;
        font-size: 14px;
        color: #555;

        span {
          display: flex;
          align-items: center;
          gap: 4px;

          mat-icon { font-size: 16px; width: 16px; height: 16px; }
        }
      }
    }

    .tab-content { padding: 24px 0; }

    .tab-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 { margin: 0; color: #1b5e20; }
    }

    .empty-tab {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 48px;
      color: #999;
      gap: 8px;

      mat-icon {
        font-size: 48px;
        width: 48px;
        height: 48px;
        opacity: 0.3;
      }
    }

    .cultivos-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 16px;
    }

    .cultivo-card {
      border-radius: 12px;

      .cultivo-avatar {
        background: #e8f5e9;
        color: #2e7d32;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
      }
    }

    .cultivo-info {
      display: flex;
      flex-direction: column;
      gap: 6px;
      margin-top: 8px;
      font-size: 13px;

      span {
        display: flex;
        align-items: center;
        gap: 6px;
        color: #555;

        mat-icon { font-size: 14px; width: 14px; height: 14px; }
      }
    }

    .estado-badge {
      display: inline-block;
      padding: 2px 10px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;
      width: fit-content;
    }

    .estado-activo { background: #e8f5e9; color: #1b5e20; }
    .estado-finalizado { background: #e3f2fd; color: #1565c0; }
    .estado-perdido { background: #ffebee; color: #c62828; }
    .estado-en_espera { background: #fff3e0; color: #e65100; }
    .estado-listo_cosecha { background: #f3e5f5; color: #6a1b9a; }

    .badge-listo {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 4px 12px;
      background: #e8f5e9;
      color: #1b5e20;
      border-radius: 12px;
      font-size: 13px;
      font-weight: 600;

      mat-icon {
        font-size: 16px;
        width: 16px;
        height: 16px;
        color: #2e7d32;
      }
    }

    .alertas-list, .tareas-list {
      display: flex;
      flex-direction: column;
      gap: 8px;
    }

    .alerta-row, .tarea-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      border-radius: 8px;
      border-left: 4px solid;

      .alerta-info, .tarea-info {
        display: flex;
        flex-direction: column;
        gap: 2px;

        strong { font-size: 14px; }
        span { font-size: 13px; color: #555; }
        small { font-size: 11px; color: #999; }
      }

      .alerta-actions, .tarea-badges {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }

    .alerta-critica { border-color: #c62828; background: #fff8f8; }
    .alerta-alta { border-color: #e65100; background: #fff3e0; }
    .alerta-media { border-color: #f9a825; background: #fffde7; }
    .alerta-baja { border-color: #558b2f; background: #f9fbe7; }

    .tarea-row { border-color: #2e7d32; background: #f9fbe7; }

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
    .badge-urgente { background: #ffcdd2; color: #c62828; }
    .badge-estado { background: #e3f2fd; color: #1565c0; }

    .cosecha-table { width: 100%; }
  `]
})
export class ParcelaDetail implements OnInit {

  parcela = signal<Parcela | null>(null);
  cultivos = signal<Cultivo[]>([]);
  alertas = signal<Alerta[]>([]);
  tareas = signal<Tarea[]>([]);
  cosechas = signal<Cosecha[]>([]);
  ultimaLectura = signal<SensorDatos | null>(null);
  cargando = signal(true);

  colsCosecha = ['fecha', 'cultivo', 'kg', 'precio',
                 'ingreso', 'calidad', 'acciones'];

  constructor(
    public authService: AuthService,
    public router: Router,
    private route: ActivatedRoute,
    private parcelaService: ParcelaService,
    private cultivoService: CultivoService,
    private cosechaService: CosechaService,
    private alertaService: AlertaService,
    private tareaService: TareaService,
    private sensorService: SensorService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarParcela(id);
  }

  cargarParcela(id: number): void {
    this.parcelaService.obtener(id).subscribe({
      next: res => {
        if (res.data) {
          this.parcela.set(res.data);
          this.cargarDatos(id);
        }
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.router.navigate(['/parcelas']);
      }
    });
  }

  cargarDatos(parcelaId: number): void {
    this.cultivoService.listarPorParcela(parcelaId).subscribe({
      next: res => { if (res.data) this.cultivos.set(res.data.content); },
      error: err => console.error('Error cultivos:', err)
    });

    this.alertaService.listarPorParcela(parcelaId).subscribe({
      next: res => { if (res.data) this.alertas.set(res.data.content); },
      error: err => console.error('Error alertas:', err)
    });

    this.tareaService.listar(parcelaId).subscribe({
      next: res => { if (res.data) this.tareas.set(res.data.content); },
      error: err => console.error('Error tareas:', err)
    });

    this.cosechaService.listarPorParcela(parcelaId).subscribe({
      next: res => { if (res.data) this.cosechas.set(res.data.content); },
      error: err => console.error('Error cosechas:', err)
    });

    this.sensorService.obtenerUltimo(parcelaId).subscribe({
      next: res => { if (res.data) this.ultimaLectura.set(res.data); },
      error: () => {}
    });
  }

  abrirCultivo(): void {
    const ref = this.dialog.open(CultivoForm, {
      width: '520px',
      data: { parcelaId: this.parcela()!.id }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargarDatos(this.parcela()!.id);
    });
  }

  abrirCosecha(cultivoId: number): void {
    const ref = this.dialog.open(CosechaForm, {
      width: '460px',
      data: { cultivoId }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargarDatos(this.parcela()!.id);
    });
  }

  abrirTarea(): void {
    const ref = this.dialog.open(TareaForm, {
      width: '520px',
      data: { parcelaId: this.parcela()!.id }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargarDatos(this.parcela()!.id);
    });
  }

  abrirSensor(): void {
    const ref = this.dialog.open(SensorForm, {
      width: '480px',
      data: { parcelaId: this.parcela()!.id }
    });
    ref.afterClosed().subscribe(ok => {
      if (ok) this.cargarDatos(this.parcela()!.id);
    });
  }

  reconocerAlerta(alerta: Alerta): void {
    this.alertaService.reconocer(alerta.id).subscribe({
      next: () => {
        this.snackBar.open('Alerta reconocida', 'Cerrar',
          { duration: 3000 });
        this.cargarDatos(this.parcela()!.id);
      }
    });
  }

  resolverAlerta(alerta: Alerta): void {
    this.alertaService.resolver(alerta.id).subscribe({
      next: () => {
        this.snackBar.open('Alerta resuelta', 'Cerrar',
          { duration: 3000 });
        this.cargarDatos(this.parcela()!.id);
      }
    });
  }

  cambiarEstadoCultivo(cultivo: Cultivo, estado: string): void {
    if (!confirm(`¿Marcar "${cultivo.nombrePersonalizado ??
        cultivo.tipoCultivo.nombre}" como listo para cosecha?`)) return;

    this.cultivoService.actualizar(cultivo.id, { estado: estado as any })
      .subscribe({
        next: () => {
          this.snackBar.open('Estado actualizado correctamente',
            'Cerrar', { duration: 3000 });
          this.cargarDatos(this.parcela()!.id);
        },
        error: err => {
          this.snackBar.open(
            err.error?.mensaje ?? 'Error al actualizar estado',
            'Cerrar', { duration: 3000 });
        }
      });
  }

  exportarPdf(cosechaId: number): void {
    const token = this.authService.getToken();
    const url =
      `http://localhost:8081/api/v1/cosechas/${cosechaId}/pdf`;

    fetch(url, {
      headers: { 'Authorization': `Bearer ${token}` }
    })
    .then(res => res.blob())
    .then(blob => {
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `cosecha-${cosechaId}.pdf`;
      link.click();
      URL.revokeObjectURL(link.href);
    });
  }
}