import { Component, OnInit, signal, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Chart, registerables } from 'chart.js';
import { SensorService } from '../../../core/services/sensor';
import { ParcelaService } from '../../../core/services/parcela';
import { Parcela } from '../../../core/models/parcela';
import { SensorDatos } from '../../../core/models/sensor-datos';

Chart.register(...registerables);

@Component({
  selector: 'app-sensor-historico',
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
    MatProgressSpinnerModule
  ],
  template: `
    <div class="page-container">

      <div class="page-header">
        <div>
          <h1>Historico de Sensores</h1>
          <p>Evolucion de variables ambientales por parcela</p>
        </div>
      </div>

      <mat-card class="filtros-card">
        <mat-card-content>
          <div class="filtros">

            <mat-form-field appearance="outline">
              <mat-label>Parcela</mat-label>
              <mat-select [formControl]="parcelaCtrl"
                          (selectionChange)="cargar()">
                @for (p of parcelas(); track p.id) {
                  <mat-option [value]="p.id">{{ p.nombre }}</mat-option>
                }
              </mat-select>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Variable</mat-label>
              <mat-select [formControl]="variableCtrl"
                          (selectionChange)="actualizarGrafico()">
                <mat-option value="temperatura">
                  Temperatura (°C)
                </mat-option>
                <mat-option value="humedadSuelo">
                  Humedad suelo (%)
                </mat-option>
                <mat-option value="humedadAmbiental">
                  Humedad ambiental (%)
                </mat-option>
                <mat-option value="luminosidad">
                  Luminosidad (lux)
                </mat-option>
              </mat-select>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha inicio</mat-label>
              <input matInput type="date" [formControl]="desdeCtrl">
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Fecha fin</mat-label>
              <input matInput type="date" [formControl]="hastaCtrl">
            </mat-form-field>

            <button mat-raised-button color="primary"
                    (click)="cargar()"
                    [disabled]="!parcelaCtrl.value || cargando()">
              <mat-icon>search</mat-icon>
              Consultar
            </button>

          </div>
        </mat-card-content>
      </mat-card>

      @if (cargando()) {
        <div class="loading">
          <mat-spinner diameter="48"></mat-spinner>
        </div>
      } @else if (datos().length === 0 && consultado()) {
        <div class="empty-state">
          <mat-icon>show_chart</mat-icon>
          <h3>Sin datos</h3>
          <p>No hay lecturas en el rango de fechas seleccionado</p>
        </div>
      } @else if (datos().length > 0) {

        <!-- Tarjetas resumen -->
        <div class="stats-grid">
          <mat-card class="stat-card">
            <mat-card-content>
              <p class="stat-label">Minimo</p>
              <h2 class="stat-value">{{ minimo() | number:'1.1-1' }}
                {{ unidad() }}</h2>
            </mat-card-content>
          </mat-card>
          <mat-card class="stat-card">
            <mat-card-content>
              <p class="stat-label">Maximo</p>
              <h2 class="stat-value">{{ maximo() | number:'1.1-1' }}
                {{ unidad() }}</h2>
            </mat-card-content>
          </mat-card>
          <mat-card class="stat-card">
            <mat-card-content>
              <p class="stat-label">Promedio</p>
              <h2 class="stat-value">{{ promedio() | number:'1.1-1' }}
                {{ unidad() }}</h2>
            </mat-card-content>
          </mat-card>
          <mat-card class="stat-card">
            <mat-card-content>
              <p class="stat-label">Lecturas</p>
              <h2 class="stat-value">{{ datos().length }}</h2>
            </mat-card-content>
          </mat-card>
        </div>

        <!-- Gráfico -->
        <mat-card class="chart-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>show_chart</mat-icon>
              {{ etiquetaVariable() }}
            </mat-card-title>
          </mat-card-header>
          <mat-card-content>
            <div class="chart-container">
              <canvas #chartCanvas></canvas>
            </div>
          </mat-card-content>
        </mat-card>

      }

    </div>
  `,
  styles: [`
    .page-container { max-width: 1200px; margin: 0 auto; }

    .page-header {
      margin-bottom: 24px;

      h1 { margin: 0; font-size: 28px; font-weight: 700; color: #1b5e20; }
      p { margin: 4px 0 0; color: #666; }
    }

    .filtros-card {
      border-radius: 12px;
      margin-bottom: 24px;
    }

    .filtros {
      display: flex;
      gap: 16px;
      flex-wrap: wrap;
      align-items: center;

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

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      margin-bottom: 24px;

      @media (max-width: 768px) {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    .stat-card {
      border-radius: 12px;
      text-align: center;

      .stat-label {
        margin: 0;
        font-size: 13px;
        color: #666;
      }

      .stat-value {
        margin: 4px 0 0;
        font-size: 28px;
        font-weight: 700;
        color: #1b5e20;
      }
    }

    .chart-card {
      border-radius: 12px;

      mat-card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        color: #1b5e20;
      }
    }

    .chart-container {
      position: relative;
      height: 400px;
      width: 100%;
    }
  `]
})
export class SensorHistorico implements OnInit {

  @ViewChild('chartCanvas') chartCanvas!: ElementRef<HTMLCanvasElement>;

  parcelas = signal<Parcela[]>([]);
  datos = signal<SensorDatos[]>([]);
  cargando = signal(false);
  consultado = signal(false);

  parcelaCtrl = new FormControl<number | null>(null);
  variableCtrl = new FormControl<string>('temperatura');
  desdeCtrl = new FormControl<string>(this.fechaHaceNDias(7));
  hastaCtrl = new FormControl<string>(this.hoy());

  private chart: Chart | null = null;

  constructor(
    private sensorService: SensorService,
    private parcelaService: ParcelaService
  ) {}

  ngOnInit(): void {
    this.parcelaService.listar().subscribe({
      next: res => {
        if (res.data && res.data.content.length > 0) {
          this.parcelas.set(res.data.content);
          this.parcelaCtrl.setValue(res.data.content[0].id);
          this.cargar();
        }
      }
    });
  }

  cargar(): void {
    const parcelaId = this.parcelaCtrl.value;
    if (!parcelaId) return;

    this.cargando.set(true);
    this.consultado.set(false);

    const desde = this.desdeCtrl.value
      ? `${this.desdeCtrl.value}T00:00:00` : undefined;
    const hasta = this.hastaCtrl.value
      ? `${this.hastaCtrl.value}T23:59:59` : undefined;

    this.sensorService.listarPorParcela(parcelaId, 0, 500).subscribe({
      next: res => {
        if (res.data) {
          let items = res.data.content;

          if (desde) {
            items = items.filter(d =>
              new Date(d.timestamp) >= new Date(desde));
          }
          if (hasta) {
            items = items.filter(d =>
              new Date(d.timestamp) <= new Date(hasta));
          }

          items.sort((a, b) =>
            new Date(a.timestamp).getTime() -
            new Date(b.timestamp).getTime());

          this.datos.set(items);
          this.consultado.set(true);
          this.cargando.set(false);

          setTimeout(() => this.actualizarGrafico(), 100);
        }
      },
      error: () => this.cargando.set(false)
    });
  }

  actualizarGrafico(): void {
    if (!this.chartCanvas || this.datos().length === 0) return;

    const variable = this.variableCtrl.value ?? 'temperatura';
    const datos = this.datos();

    const labels = datos.map(d =>
      new Date(d.timestamp).toLocaleDateString('es-ES', {
        day: '2-digit', month: '2-digit',
        hour: '2-digit', minute: '2-digit'
      })
    );

    const valores = datos.map(d => {
      const v = (d as any)[variable];
      return v != null ? Number(v) : null;
    });

    if (this.chart) {
      this.chart.destroy();
      this.chart = null;
    }

    const color = this.colorVariable(variable);

    this.chart = new Chart(this.chartCanvas.nativeElement, {
      type: 'line',
      data: {
        labels,
        datasets: [{
          label: this.etiquetaVariable(),
          data: valores,
          borderColor: color,
          backgroundColor: color + '20',
          borderWidth: 2,
          pointRadius: datos.length > 50 ? 0 : 3,
          pointHoverRadius: 5,
          fill: true,
          tension: 0.3,
          spanGaps: true
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: ctx =>
                `${ctx.parsed.y?.toFixed(1)} ${this.unidad()}`
            }
          }
        },
        scales: {
          x: {
            ticks: {
              maxTicksLimit: 12,
              maxRotation: 45
            },
            grid: { color: '#f0f0f0' }
          },
          y: {
            grid: { color: '#f0f0f0' },
            ticks: {
              callback: val => `${val} ${this.unidad()}`
            }
          }
        }
      }
    });
  }

  minimo(): number {
    const v = this.variableCtrl.value ?? 'temperatura';
    const vals = this.datos()
      .map(d => Number((d as any)[v]))
      .filter(n => !isNaN(n));
    return vals.length ? Math.min(...vals) : 0;
  }

  maximo(): number {
    const v = this.variableCtrl.value ?? 'temperatura';
    const vals = this.datos()
      .map(d => Number((d as any)[v]))
      .filter(n => !isNaN(n));
    return vals.length ? Math.max(...vals) : 0;
  }

  promedio(): number {
    const v = this.variableCtrl.value ?? 'temperatura';
    const vals = this.datos()
      .map(d => Number((d as any)[v]))
      .filter(n => !isNaN(n));
    return vals.length
      ? vals.reduce((a, b) => a + b, 0) / vals.length : 0;
  }

  etiquetaVariable(): string {
    const map: Record<string, string> = {
      temperatura: 'Temperatura (°C)',
      humedadSuelo: 'Humedad suelo (%)',
      humedadAmbiental: 'Humedad ambiental (%)',
      luminosidad: 'Luminosidad (lux)'
    };
    return map[this.variableCtrl.value ?? 'temperatura'];
  }

  unidad(): string {
    const map: Record<string, string> = {
      temperatura: '°C',
      humedadSuelo: '%',
      humedadAmbiental: '%',
      luminosidad: 'lux'
    };
    return map[this.variableCtrl.value ?? 'temperatura'];
  }

  colorVariable(variable: string): string {
    const map: Record<string, string> = {
      temperatura: '#e53935',
      humedadSuelo: '#1565c0',
      humedadAmbiental: '#0288d1',
      luminosidad: '#f9a825'
    };
    return map[variable] ?? '#2e7d32';
  }

  private hoy(): string {
    return new Date().toISOString().split('T')[0];
  }

  private fechaHaceNDias(n: number): string {
    const d = new Date();
    d.setDate(d.getDate() - n);
    return d.toISOString().split('T')[0];
  }
}