import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SensorService } from '../../../core/services/sensor';

@Component({
  selector: 'app-sensor-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  template: `
    <h2 mat-dialog-title>
      <mat-icon>sensors</mat-icon>
      Enviar Datos de Sensores
    </h2>

    <mat-dialog-content>
      <p class="subtitle">
        Introduce los valores actuales de los sensores de la parcela.
        Los campos son opcionales — solo rellena los disponibles.
      </p>

      <form [formGroup]="form" class="form-grid">

        <mat-form-field appearance="outline">
          <mat-label>Temperatura (°C)</mat-label>
          <input matInput type="number" formControlName="temperatura"
                 step="0.1">
          <mat-icon matPrefix class="temp">thermostat</mat-icon>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Humedad suelo (%)</mat-label>
          <input matInput type="number" formControlName="humedadSuelo"
                 min="0" max="100" step="0.1">
          <mat-icon matPrefix class="hum">water_drop</mat-icon>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Humedad ambiental (%)</mat-label>
          <input matInput type="number" formControlName="humedadAmbiental"
                 min="0" max="100" step="0.1">
          <mat-icon matPrefix class="amb">cloud</mat-icon>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Luminosidad (lux)</mat-label>
          <input matInput type="number" formControlName="luminosidad"
                 min="0" step="1">
          <mat-icon matPrefix class="lux">light_mode</mat-icon>
        </mat-form-field>

      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">Cancelar</button>
      <button mat-raised-button color="primary"
              [disabled]="cargando()"
              (click)="enviar()">
        @if (cargando()) {
          <mat-spinner diameter="20"></mat-spinner>
        } @else {
          <mat-icon>send</mat-icon>
          Enviar datos
        }
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    h2 { display: flex; align-items: center; gap: 8px; color: #1b5e20; }

    .subtitle {
      font-size: 13px;
      color: #666;
      margin: 0 0 16px;
    }

    .form-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0 16px;
    }

    mat-dialog-content { min-width: 420px; }

    .temp { color: #e53935; }
    .hum { color: #1565c0; }
    .amb { color: #0288d1; }
    .lux { color: #f9a825; }
  `]
})
export class SensorForm {

  form: FormGroup;
  cargando = signal(false);

  constructor(
    private fb: FormBuilder,
    private sensorService: SensorService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<SensorForm>,
    @Inject(MAT_DIALOG_DATA) public data: { parcelaId: number }
  ) {
    this.form = this.fb.group({
      temperatura: [null],
      humedadSuelo: [null],
      humedadAmbiental: [null],
      luminosidad: [null]
    });
  }

  enviar(): void {
    this.cargando.set(true);

    this.sensorService.registrar({
      parcelaId: this.data.parcelaId,
      temperatura: this.form.value.temperatura ?? undefined,
      humedadSuelo: this.form.value.humedadSuelo ?? undefined,
      humedadAmbiental: this.form.value.humedadAmbiental ?? undefined,
      luminosidad: this.form.value.luminosidad ?? undefined,
      timestamp: new Date().toISOString().slice(0, 19)
    }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open('Datos enviados correctamente', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: err => {
        this.cargando.set(false);
        this.snackBar.open(
          err.error?.mensaje ?? 'Error al enviar',
          'Cerrar', { duration: 3000 });
      }
    });
  }
}