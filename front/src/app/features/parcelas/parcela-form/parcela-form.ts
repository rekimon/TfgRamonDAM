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
import { ParcelaService } from '../../../core/services/parcela';
import { Parcela } from '../../../core/models/parcela';

@Component({
  selector: 'app-parcela-form',
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
    <mat-icon>terrain</mat-icon>
    {{ parcela ? 'Editar parcela' : 'Nueva parcela' }}
  </h2>

  <mat-dialog-content>
    <form [formGroup]="form" class="form-grid">

      <mat-form-field appearance="outline" class="full">
        <mat-label>Nombre *</mat-label>
        <input matInput formControlName="nombre"
               placeholder="Ej: Parcela Norte">
        <mat-icon matPrefix>terrain</mat-icon>
        @if (form.get('nombre')?.hasError('required') &&
             form.get('nombre')?.touched) {
          <mat-error>El nombre es obligatorio</mat-error>
        }
      </mat-form-field>

      <mat-form-field appearance="outline" class="full">
        <mat-label>Descripcion</mat-label>
        <textarea matInput formControlName="descripcion"
                  rows="2"
                  placeholder="Descripcion opcional de la parcela">
        </textarea>
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Superficie (ha) *</mat-label>
        <input matInput type="number" formControlName="superficieHa"
               placeholder="Ej: 5.5" min="0.01" step="0.01">
        <span matSuffix>ha</span>
        @if (form.get('superficieHa')?.hasError('required') &&
             form.get('superficieHa')?.touched) {
          <mat-error>Obligatorio</mat-error>
        }
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Referencia catastral</mat-label>
        <input matInput formControlName="referenciaCatastral"
               placeholder="Ej: 06015A001000010000FX">
        <mat-icon matPrefix>article</mat-icon>
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Latitud *</mat-label>
        <input matInput type="number" formControlName="latitud"
               placeholder="Ej: 38.7223" step="0.0001">
        <mat-icon matPrefix>location_on</mat-icon>
        @if (form.get('latitud')?.hasError('required') &&
             form.get('latitud')?.touched) {
          <mat-error>Obligatorio</mat-error>
        }
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Longitud *</mat-label>
        <input matInput type="number" formControlName="longitud"
               placeholder="Ej: -6.3422" step="0.0001">
        <mat-icon matPrefix>location_on</mat-icon>
        @if (form.get('longitud')?.hasError('required') &&
             form.get('longitud')?.touched) {
          <mat-error>Obligatorio</mat-error>
        }
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Municipio</mat-label>
        <input matInput formControlName="municipio"
               placeholder="Ej: Badajoz">
        <mat-icon matPrefix>location_city</mat-icon>
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Provincia</mat-label>
        <input matInput formControlName="provincia"
               placeholder="Ej: Badajoz">
        <mat-icon matPrefix>map</mat-icon>
      </mat-form-field>

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
  h2 {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #1b5e20;
    margin-bottom: 4px;
  }

  mat-dialog-content {
    min-width: 520px;
    padding-top: 16px !important;
  }

  .form-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 0 16px;

    .full { grid-column: 1 / -1; }
  }
`]
})
export class ParcelaForm {

  form: FormGroup;
  cargando = signal(false);

  constructor(
    private fb: FormBuilder,
    private parcelaService: ParcelaService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<ParcelaForm>,
    @Inject(MAT_DIALOG_DATA) public parcela: Parcela | null
  ) {
    this.form = this.fb.group({
      nombre: [parcela?.nombre ?? '', Validators.required],
      descripcion: [parcela?.descripcion ?? ''],
      superficieHa: [parcela?.superficieHa ?? '', Validators.required],
      latitud: [parcela?.latitud ?? '', Validators.required],
      longitud: [parcela?.longitud ?? '', Validators.required],
      municipio: [parcela?.municipio ?? ''],
      provincia: [parcela?.provincia ?? ''],
      referenciaCatastral: [parcela?.referenciaCatastral ?? '']
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    this.cargando.set(true);

    const request$ = this.parcela
      ? this.parcelaService.actualizar(this.parcela.id, this.form.value)
      : this.parcelaService.crear(this.form.value);

    request$.subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open(
          this.parcela ? 'Parcela actualizada' : 'Parcela creada',
          'Cerrar', { duration: 3000 });
        this.dialogRef.close(true);
      },
      error: (err) => {
        this.cargando.set(false);
        this.snackBar.open(
          err.error?.mensaje ?? 'Error al guardar',
          'Cerrar', { duration: 3000 });
      }
    });
  }
}