import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { ParcelaService } from '../../../core/services/parcela';
import { AlertaService } from '../../../core/services/alerta';
import { TareaService } from '../../../core/services/tarea';
import { SensorService } from '../../../core/services/sensor';
import { AuthService } from '../../../core/services/auth';
import { WeatherService, WeatherForecast } from '../../../core/services/wheather';
import { Parcela } from '../../../core/models/parcela';
import { Alerta } from '../../../core/models/alerta';
import { Tarea } from '../../../core/models/tarea';
import { SensorDatos } from '../../../core/models/sensor-datos';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    MatDividerModule
  ],
  template: `
    <div class="dashboard">

      <div class="dashboard-header">
        <div>
          <h1>Dashboard</h1>
          <p>Bienvenido, {{ authService.currentUser()?.nombre }}</p>
        </div>
      </div>

      <!-- Tarjetas resumen -->
      <div class="stats-grid">
        <mat-card class="stat-card green">
          <mat-card-content>
            <div class="stat-content">
              <div>
                <p class="stat-label">Parcelas activas</p>
                <h2 class="stat-value">{{ totalParcelas() }}</h2>
              </div>
              <mat-icon class="stat-icon">terrain</mat-icon>
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card red">
          <mat-card-content>
            <div class="stat-content">
              <div>
                <p class="stat-label">Alertas activas</p>
                <h2 class="stat-value">{{ totalAlertas() }}</h2>
              </div>
              <mat-icon class="stat-icon">notifications_active</mat-icon>
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card orange">
          <mat-card-content>
            <div class="stat-content">
              <div>
                <p class="stat-label">Tareas pendientes</p>
                <h2 class="stat-value">{{ totalTareas() }}</h2>
              </div>
              <mat-icon class="stat-icon">task</mat-icon>
            </div>
          </mat-card-content>
        </mat-card>

        <mat-card class="stat-card blue">
          <mat-card-content>
            <div class="stat-content">
              <div>
                <p class="stat-label">Parcela activa</p>
                <h2 class="stat-value" style="font-size:16px">
                  {{ parcelaSeleccionada()?.nombre ?? 'Ninguna' }}
                </h2>
              </div>
              <mat-icon class="stat-icon">location_on</mat-icon>
            </div>
          </mat-card-content>
        </mat-card>
      </div>

      <div class="dashboard-grid">

        <!-- Parcelas -->
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>terrain</mat-icon>
              Mis Parcelas
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            @if (cargando()) {
              <div class="loading">
                <mat-spinner diameter="32"></mat-spinner>
              </div>
            } @else if (parcelas().length === 0) {
              <div class="empty-state">
                <mat-icon>terrain</mat-icon>
                <p>No tienes parcelas registradas</p>
                <button mat-raised-button color="primary"
                        (click)="router.navigate(['/parcelas'])">
                  Crear parcela
                </button>
              </div>
            } @else {
              <div class="parcela-list">
                @for (parcela of parcelas(); track parcela.id) {
                  <div class="parcela-item"
                       [class.selected]="parcelaSeleccionada()?.id === parcela.id"
                       (click)="seleccionarParcela(parcela)">
                    <div class="parcela-info">
                      <strong>{{ parcela.nombre }}</strong>
                      <small>{{ parcela.municipio }}, {{ parcela.provincia }}</small>
                      <small>{{ parcela.superficieHa }} ha</small>
                    </div>
                    <mat-icon>chevron_right</mat-icon>
                  </div>
                }
              </div>
              <button mat-button color="primary"
                      (click)="router.navigate(['/parcelas'])">
                Ver todas
              </button>
            }
          </mat-card-content>
        </mat-card>

        <!-- Sensor datos ultima lectura -->
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>sensors</mat-icon>
              Ultima Lectura de Sensores
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            @if (!parcelaSeleccionada()) {
              <div class="empty-state">
                <mat-icon>sensors</mat-icon>
                <p>Selecciona una parcela para ver los datos</p>
              </div>
            } @else if (!ultimaLectura()) {
              <div class="empty-state">
                <mat-icon>sensors_off</mat-icon>
                <p>Sin datos de sensores</p>
              </div>
            } @else {
              <div class="sensor-grid">
                @if (ultimaLectura()?.temperatura !== null) {
                  <div class="sensor-item">
                    <mat-icon class="temp">thermostat</mat-icon>
                    <div>
                      <p class="sensor-label">Temperatura</p>
                      <p class="sensor-value">
                        {{ ultimaLectura()?.temperatura }}°C
                      </p>
                    </div>
                  </div>
                }
                @if (ultimaLectura()?.humedadSuelo !== null) {
                  <div class="sensor-item">
                    <mat-icon class="hum">water_drop</mat-icon>
                    <div>
                      <p class="sensor-label">Humedad suelo</p>
                      <p class="sensor-value">
                        {{ ultimaLectura()?.humedadSuelo }}%
                      </p>
                    </div>
                  </div>
                }
                @if (ultimaLectura()?.humedadAmbiental !== null) {
                  <div class="sensor-item">
                    <mat-icon class="amb">cloud</mat-icon>
                    <div>
                      <p class="sensor-label">Humedad ambiental</p>
                      <p class="sensor-value">
                        {{ ultimaLectura()?.humedadAmbiental }}%
                      </p>
                    </div>
                  </div>
                }
                @if (ultimaLectura()?.luminosidad !== null) {
                  <div class="sensor-item">
                    <mat-icon class="lux">light_mode</mat-icon>
                    <div>
                      <p class="sensor-label">Luminosidad</p>
                      <p class="sensor-value">
                        {{ ultimaLectura()?.luminosidad }} lux
                      </p>
                    </div>
                  </div>
                }
                <small class="timestamp">
                  Ultima actualizacion:
                  {{ ultimaLectura()?.timestamp | date:'dd/MM/yyyy HH:mm' }}
                </small>
              </div>
            }
          </mat-card-content>
        </mat-card>

        <!-- Alertas activas -->
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>notifications_active</mat-icon>
              Alertas Activas
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            @if (alertas().length === 0) {
              <div class="empty-state">
                <mat-icon class="ok">check_circle</mat-icon>
                <p>Sin alertas activas</p>
              </div>
            } @else {
              <div class="alerta-list">
                @for (alerta of alertas().slice(0, 5); track alerta.id) {
                  <div class="alerta-item"
                       [class]="'alerta-' + alerta.severidad.toLowerCase()">
                    <div class="alerta-info">
                      <strong>{{ alerta.tipoAlerta }}</strong>
                      <small>{{ alerta.mensaje }}</small>
                      <small>{{ alerta.fechaDisparo | date:'dd/MM HH:mm' }}</small>
                    </div>
                    <span class="severidad-badge"
                          [class]="'badge-' + alerta.severidad.toLowerCase()">
                      {{ alerta.severidad }}
                    </span>
                  </div>
                }
              </div>
              <button mat-button color="warn"
                      (click)="router.navigate(['/alertas'])">
                Ver todas las alertas
              </button>
            }
          </mat-card-content>
        </mat-card>

        <!-- Tareas proximas -->
        <mat-card class="dashboard-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>task</mat-icon>
              Tareas Proximas
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            @if (tareas().length === 0) {
              <div class="empty-state">
                <mat-icon>task_alt</mat-icon>
                <p>Sin tareas pendientes</p>
              </div>
            } @else {
              <div class="tarea-list">
                @for (tarea of tareas().slice(0, 5); track tarea.id) {
                  <div class="tarea-item">
                    <div class="tarea-info">
                      <strong>{{ tarea.titulo }}</strong>
                      <small>{{ tarea.parcelaNombre }}</small>
                      <small>{{ tarea.fechaPrevista | date:'dd/MM/yyyy' }}</small>
                    </div>
                    <span class="prioridad-badge"
                          [class]="'badge-' + tarea.prioridad.toLowerCase()">
                      {{ tarea.prioridad }}
                    </span>
                  </div>
                }
              </div>
              <button mat-button color="primary"
                      (click)="router.navigate(['/tareas'])">
                Ver todas las tareas
              </button>
            }
          </mat-card-content>
        </mat-card>

      </div>

      <!-- Previsión meteorológica -->
      <mat-card class="dashboard-card weather-card">
        <mat-card-header>
          <mat-card-title>
            <mat-icon>cloud</mat-icon>
            Prevision Meteorologica
            @if (prevision()?.ciudad) {
              <span class="ciudad">— {{ prevision()!.ciudad }}</span>
            }
          </mat-card-title>
        </mat-card-header>
        <mat-card-content>
          @if (previsionCargando()) {
            <div class="loading">
              <mat-spinner diameter="32"></mat-spinner>
            </div>
          } @else if (!parcelaSeleccionada()) {
            <div class="empty-state">
              <mat-icon>cloud_off</mat-icon>
              <p>Selecciona una parcela</p>
            </div>
          } @else if (!prevision()) {
            <div class="empty-state">
              <mat-icon>cloud_off</mat-icon>
              <p>No se pudo obtener la prevision</p>
            </div>
          } @else {
            <div class="prevision-grid">
              @for (dia of prevision()!.dias; track dia.fecha) {
                <div class="dia-card">
                  <p class="dia-nombre">{{ dia.diaSemana }}</p>
                  <p class="dia-fecha">{{ dia.fecha | date:'dd/MM' }}</p>
                  <img [src]="'https://openweathermap.org/img/wn/'
                              + dia.icono + '@2x.png'"
                       [alt]="dia.descripcion"
                       class="dia-icono">
                  <p class="dia-desc">{{ dia.descripcion }}</p>
                  <div class="dia-temps">
                    <span class="temp-max">{{ dia.tempMax }}°</span>
                    <span class="temp-min">{{ dia.tempMin }}°</span>
                  </div>
                  <div class="dia-extras">
                    <span title="Probabilidad de lluvia">
                      <mat-icon>water_drop</mat-icon>
                      {{ dia.probabilidadLluvia | number:'1.0-0' }}%
                    </span>
                    <span title="Viento">
                      <mat-icon>air</mat-icon>
                      {{ dia.viento }} m/s
                    </span>
                  </div>
                </div>
              }
            </div>
          }
        </mat-card-content>
      </mat-card>

    </div>
  `,
  styles: [`
    .dashboard {
      max-width: 1400px;
      margin: 0 auto;
    }

    .dashboard-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      h1 {
        margin: 0;
        font-size: 28px;
        font-weight: 700;
        color: #1b5e20;
      }

      p { margin: 4px 0 0; color: #666; }
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      margin-bottom: 24px;

      @media (max-width: 900px) {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .stat-card {
      border-radius: 12px;
      border-left: 4px solid;

      &.green { border-color: #2e7d32; }
      &.red { border-color: #c62828; }
      &.orange { border-color: #e65100; }
      &.blue { border-color: #1565c0; }

      .stat-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .stat-label { margin: 0; font-size: 13px; color: #666; }

      .stat-value {
        margin: 4px 0 0;
        font-size: 32px;
        font-weight: 700;
        color: #1a1a1a;
      }

      .stat-icon {
        font-size: 40px;
        width: 40px;
        height: 40px;
        opacity: 0.2;
      }
    }

    .dashboard-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      margin-bottom: 16px;

      @media (max-width: 900px) {
        grid-template-columns: 1fr;
      }
    }

    .dashboard-card {
      border-radius: 12px;

      mat-card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        color: #1b5e20;
      }
    }

    .weather-card {
      margin-top: 0;
      border-radius: 12px;
    }

    .ciudad {
      font-size: 14px;
      font-weight: 400;
      color: #666;
      margin-left: 4px;
    }

    .loading {
      display: flex;
      justify-content: center;
      padding: 32px;
    }

    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 24px;
      color: #999;
      gap: 8px;

      mat-icon {
        font-size: 48px;
        width: 48px;
        height: 48px;
        opacity: 0.4;
        &.ok { color: #2e7d32; opacity: 0.7; }
      }

      p { margin: 0; }
    }

    .parcela-list {
      display: flex;
      flex-direction: column;
      gap: 4px;
      max-height: 250px;
      overflow-y: auto;
    }

    .parcela-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 12px;
      border-radius: 8px;
      cursor: pointer;
      transition: background 0.2s;

      &:hover { background: #f1f8e9; }

      &.selected {
        background: #e8f5e9;
        border-left: 3px solid #2e7d32;
      }

      .parcela-info {
        display: flex;
        flex-direction: column;
        gap: 2px;

        strong { font-size: 14px; }
        small { font-size: 12px; color: #666; }
      }
    }

    .sensor-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
    }

    .sensor-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px;
      background: #f5f5f5;
      border-radius: 8px;

      mat-icon {
        font-size: 32px;
        width: 32px;
        height: 32px;

        &.temp { color: #e53935; }
        &.hum { color: #1565c0; }
        &.amb { color: #0288d1; }
        &.lux { color: #f9a825; }
      }

      .sensor-label { margin: 0; font-size: 12px; color: #666; }

      .sensor-value {
        margin: 2px 0 0;
        font-size: 18px;
        font-weight: 700;
        color: #1a1a1a;
      }
    }

    .timestamp {
      grid-column: 1 / -1;
      color: #999;
      font-size: 11px;
    }

    .alerta-list, .tarea-list {
      display: flex;
      flex-direction: column;
      gap: 4px;
      max-height: 250px;
      overflow-y: auto;
    }

    .alerta-item, .tarea-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 12px;
      border-radius: 8px;
      border-left: 3px solid;

      .alerta-info, .tarea-info {
        display: flex;
        flex-direction: column;
        gap: 2px;

        strong { font-size: 13px; }
        small { font-size: 11px; color: #666; }
      }
    }

    .alerta-critica { border-color: #c62828; background: #fff8f8; }
    .alerta-alta { border-color: #e65100; background: #fff3e0; }
    .alerta-media { border-color: #f9a825; background: #fffde7; }
    .alerta-baja { border-color: #558b2f; background: #f9fbe7; }

    .severidad-badge, .prioridad-badge {
      font-size: 11px;
      padding: 2px 8px;
      border-radius: 12px;
      font-weight: 600;
      white-space: nowrap;
    }

    .badge-critica { background: #ffcdd2; color: #c62828; }
    .badge-alta { background: #ffe0b2; color: #e65100; }
    .badge-media { background: #fff9c4; color: #f57f17; }
    .badge-baja { background: #dcedc8; color: #558b2f; }
    .badge-urgente { background: #ffcdd2; color: #c62828; }

    .prevision-grid {
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      gap: 12px;

      @media (max-width: 768px) {
        grid-template-columns: repeat(3, 1fr);
      }
    }

    .dia-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 12px 8px;
      background: #f8f9fa;
      border-radius: 12px;
      text-align: center;
      gap: 4px;

      .dia-nombre {
        margin: 0;
        font-weight: 700;
        font-size: 13px;
        color: #1b5e20;
      }

      .dia-fecha { margin: 0; font-size: 12px; color: #999; }

      .dia-icono { width: 50px; height: 50px; }

      .dia-desc {
        margin: 0;
        font-size: 11px;
        color: #555;
        text-transform: capitalize;
      }

      .dia-temps {
        display: flex;
        gap: 8px;

        .temp-max {
          font-weight: 700;
          font-size: 16px;
          color: #e53935;
        }

        .temp-min { font-size: 16px; color: #1565c0; }
      }

      .dia-extras {
        display: flex;
        gap: 8px;
        font-size: 11px;
        color: #666;

        span {
          display: flex;
          align-items: center;
          gap: 2px;

          mat-icon {
            font-size: 12px;
            width: 12px;
            height: 12px;
          }
        }
      }
    }
  `]
})
export class Dashboard implements OnInit {

  prevision = signal<WeatherForecast | null>(null);
  previsionCargando = signal(false);
  parcelas = signal<Parcela[]>([]);
  alertas = signal<Alerta[]>([]);
  tareas = signal<Tarea[]>([]);
  ultimaLectura = signal<SensorDatos | null>(null);
  parcelaSeleccionada = signal<Parcela | null>(null);
  cargando = signal(true);
  totalParcelas = signal(0);
  totalAlertas = signal(0);
  totalTareas = signal(0);

  constructor(
    public authService: AuthService,
    public router: Router,
    private parcelaService: ParcelaService,
    private alertaService: AlertaService,
    private tareaService: TareaService,
    private sensorService: SensorService,
    private weatherService: WeatherService
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.parcelaService.listar().subscribe({
      next: res => {
        if (res.data) {
          this.parcelas.set(res.data.content);
          this.totalParcelas.set(res.data.totalElements);
          if (res.data.content.length > 0) {
            this.seleccionarParcela(res.data.content[0]);
          }
        }
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });

    this.tareaService.listar(undefined, 'PENDIENTE').subscribe({
      next: res => {
        if (res.data) {
          this.tareas.set(res.data.content);
          this.totalTareas.set(res.data.totalElements);
        }
      }
    });
  }

  seleccionarParcela(parcela: Parcela): void {
    this.parcelaSeleccionada.set(parcela);

    this.alertaService.listarPorParcela(parcela.id, 'ACTIVA').subscribe({
      next: res => {
        if (res.data) {
          this.alertas.set(res.data.content);
          this.totalAlertas.set(res.data.totalElements);
        }
      }
    });

    this.sensorService.obtenerUltimo(parcela.id).subscribe({
      next: res => {
        if (res.data) this.ultimaLectura.set(res.data);
      },
      error: () => this.ultimaLectura.set(null)
    });

    if (parcela.latitud && parcela.longitud) {
      this.cargarPrevision(parcela.latitud, parcela.longitud);
    }
  }

  cargarPrevision(lat: number, lon: number): void {
    this.previsionCargando.set(true);
    this.weatherService.obtenerPrevisión(lat, lon).subscribe({
      next: res => {
        if (res.data) this.prevision.set(res.data);
        this.previsionCargando.set(false);
      },
      error: () => this.previsionCargando.set(false)
    });
  }
}