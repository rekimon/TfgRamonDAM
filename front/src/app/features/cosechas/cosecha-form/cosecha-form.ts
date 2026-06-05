import { Component, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CosechaService } from '../../../core/services/cosecha';

@Component({
  selector: 'app-cosecha-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  template: `
    <h2 mat-dialog-title>
      <mat-icon>agriculture</mat-icon>
      Registrar Cosecha
    </h2>

    <mat-dialog-content>
      <form [formGroup]="form" class="form-container">

        <label>Fecha de cosecha *</label>
        <input type="date" formControlName="fechaCosecha"
               class="input-field" [min]="hoy">
        @if (form.get('fechaCosecha')?.hasError('fechaAnterior') &&
             form.get('fechaCosecha')?.touched) {
          <small class="error-msg">
            La fecha no puede ser anterior a hoy
          </small>
        }

        <label>Kg obtenidos *</label>
        <input type="number" formControlName="kgObtenidos"
               min="0.01" step="0.01" class="input-field"
               placeholder="Ej: 5000"
               (input)="form.get('kgObtenidos')?.setValue(
                 +$any($event.target).value)">

        <label>Precio por kg (€) *</label>
        <input type="number" formControlName="precioPorKg"
               min="0" step="0.01" class="input-field"
               placeholder="Ej: 0.25"
               (input)="form.get('precioPorKg')?.setValue(
                 +$any($event.target).value)">

        <label>Calidad</label>
        <select formControlName="calidad" class="input-field">
          <option value="BAJA">Baja</option>
          <option value="ESTANDAR">Estandar</option>
          <option value="PREMIUM">Premium</option>
        </select>

        @if (ingresoEstimado() > 0) {
          <div class="ingreso-preview">
            <strong>Ingreso estimado:
              {{ ingresoEstimado() | currency:'EUR' }}
            </strong>
          </div>
        }

        <label>Observaciones</label>
        <textarea formControlName="observaciones" rows="3"
                  class="input-field"
                  placeholder="Notas adicionales..."></textarea>

      </form>
    </mat-dialog-content>

    <mat-dialog-actions align="end">
      <button mat-button [mat-dialog-close]="false">Cancelar</button>
      <button mat-raised-button color="primary"
              [disabled]="form.invalid || cargando()"
              (click)="guardar()">
        @if (cargando()) {
          <mat-spinner diameter="20"></mat-spinner>
        } @else {
          <mat-icon>save</mat-icon>
          Guardar
        }
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    h2 { display: flex; align-items: center; gap: 8px; color: #1b5e20; }

    mat-dialog-content {
      width: 420px;
      overflow-y: auto;
    }

    .form-container {
      display: flex;
      flex-direction: column;
      gap: 8px;
      padding: 8px 0;
    }

    label {
      font-size: 13px;
      font-weight: 600;
      color: #555;
      margin-top: 4px;
    }

    .error-msg {
      color: #c62828;
      font-size: 12px;
      margin-top: -4px;
    }

    .input-field {
      width: 100%;
      padding: 10px 12px;
      border: 1px solid #ccc;
      border-radius: 8px;
      font-size: 14px;
      box-sizing: border-box;
      outline: none;
      font-family: inherit;

      &:focus {
        border-color: #2e7d32;
        box-shadow: 0 0 0 2px rgba(46,125,50,0.15);
      }
    }

    .ingreso-preview {
      padding: 12px;
      background: #e8f5e9;
      border-radius: 8px;
      color: #1b5e20;
      text-align: center;
    }
  `]
})
export class CosechaForm {

  form: FormGroup;
  cargando = signal(false);
  hoy = new Date().toISOString().split('T')[0];

  static fechaNoAnterior(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    const fecha = new Date(control.value);
    return fecha < hoy ? { fechaAnterior: true } : null;
  }

  constructor(
    private fb: FormBuilder,
    private cosechaService: CosechaService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<CosechaForm>,
    @Inject(MAT_DIALOG_DATA) public data: { cultivoId: number }
  ) {
    this.form = this.fb.group({
      fechaCosecha: ['', [
        Validators.required,
        CosechaForm.fechaNoAnterior
      ]],
      kgObtenidos: [null, Validators.required],
      precioPorKg: [null, Validators.required],
      calidad: ['ESTANDAR'],
      observaciones: ['']
    });
  }

  ingresoEstimado(): number {
    const kg = this.form.get('kgObtenidos')?.value ?? 0;
    const precio = this.form.get('precioPorKg')?.value ?? 0;
    return kg * precio;
  }

  guardar(): void {
    if (this.form.invalid) return;
    this.cargando.set(true);

    this.cosechaService.crear({
      cultivoId: this.data.cultivoId,
      ...this.form.value
    }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open('Cosecha registrada', 'Cerrar',
          { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: err => {
        this.cargando.set(false);
        this.snackBar.open(
          err.error?.mensaje ?? 'Error al guardar',
          'Cerrar', { duration: 3000 });
      }
    });
  }
}