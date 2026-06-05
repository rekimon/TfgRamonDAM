import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CultivoService } from '../../../core/services/cultivo';
import { TipoCultivoResumen } from '../../../core/models/tipo-cultivo';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { ApiResponse, PagedResponse } from '../../../core/models/api-response';

@Component({
  selector: 'app-cultivo-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  template: `
    <h2 mat-dialog-title>
      <mat-icon>grass</mat-icon>
      Nuevo Cultivo
    </h2>

    <mat-dialog-content>
      <form [formGroup]="form" class="form-grid">

        <mat-form-field appearance="outline" class="full">
          <mat-label>Tipo de cultivo</mat-label>
          <mat-select formControlName="tipoCultivoId">
            @for (tipo of tiposCultivo(); track tipo.id) {
              <mat-option [value]="tipo.id">
                {{ tipo.nombre }}
                @if (tipo.nombreCientifico) {
                  <small> ({{ tipo.nombreCientifico }})</small>
                }
              </mat-option>
            }
          </mat-select>
          @if (form.get('tipoCultivoId')?.hasError('required') &&
               form.get('tipoCultivoId')?.touched) {
            <mat-error>Obligatorio</mat-error>
          }
        </mat-form-field>

        <mat-form-field appearance="outline" class="full">
          <mat-label>Nombre personalizado (opcional)</mat-label>
          <input matInput formControlName="nombrePersonalizado"
                 placeholder="Ej: Trigo primavera 2026">
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Fecha de siembra *</mat-label>
          <input matInput [matDatepicker]="pickerSiembra"
                 formControlName="fechaSiembra"
                 [min]="minFecha"
                 placeholder="dd/mm/yyyy">
          <mat-datepicker-toggle matIconSuffix
                                 [for]="pickerSiembra">
          </mat-datepicker-toggle>
          <mat-datepicker #pickerSiembra></mat-datepicker>
          @if (form.get('fechaSiembra')?.hasError('required') &&
               form.get('fechaSiembra')?.touched) {
            <mat-error>Obligatorio</mat-error>
          }
          @if (form.get('fechaSiembra')?.hasError('matDatepickerMin')) {
            <mat-error>La fecha no puede ser anterior a hoy</mat-error>
          }
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Fecha cosecha estimada</mat-label>
          <input matInput [matDatepicker]="pickerCosecha"
                 formControlName="fechaCosechaEstimada"
                 [min]="form.get('fechaSiembra')?.value || minFecha"
                 placeholder="dd/mm/yyyy">
          <mat-datepicker-toggle matIconSuffix
                                 [for]="pickerCosecha">
          </mat-datepicker-toggle>
          <mat-datepicker #pickerCosecha></mat-datepicker>
          @if (form.get('fechaCosechaEstimada')
               ?.hasError('matDatepickerMin')) {
            <mat-error>Debe ser posterior a la fecha de siembra</mat-error>
          }
        </mat-form-field>

        @if (form.hasError('cosechaAntesSiembra') &&
             form.get('fechaCosechaEstimada')?.value) {
          <p class="error-group full">
            <mat-icon>error</mat-icon>
            La fecha de cosecha debe ser posterior a la de siembra
          </p>
        }

        <mat-form-field appearance="outline" class="full">
          <mat-label>Notas</mat-label>
          <textarea matInput formControlName="notas" rows="3"></textarea>
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
    h2 { display: flex; align-items: center; gap: 8px; color: #1b5e20; }

    .form-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0 16px;
      .full { grid-column: 1 / -1; }
    }

    mat-dialog-content {
      min-width: 460px;
      padding-top: 16px !important;
    }

    .error-group {
      display: flex;
      align-items: center;
      gap: 6px;
      color: #c62828;
      font-size: 12px;
      margin: -8px 0 8px;

      mat-icon {
        font-size: 16px;
        width: 16px;
        height: 16px;
      }
    }
  `]
})
export class CultivoForm implements OnInit {

  form: FormGroup;
  cargando = signal(false);
  tiposCultivo = signal<TipoCultivoResumen[]>([]);
  minFecha = new Date();

  static fechaCosechaMayorSiembra(
      form: AbstractControl): ValidationErrors | null {
    const siembra = form.get('fechaSiembra')?.value;
    const cosecha = form.get('fechaCosechaEstimada')?.value;
    if (!siembra || !cosecha) return null;
    return new Date(cosecha) <= new Date(siembra)
      ? { cosechaAntesSiembra: true } : null;
  }

  constructor(
    private fb: FormBuilder,
    private cultivoService: CultivoService,
    private http: HttpClient,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<CultivoForm>,
    @Inject(MAT_DIALOG_DATA) public data: { parcelaId: number }
  ) {
    this.form = this.fb.group({
      tipoCultivoId: ['', Validators.required],
      nombrePersonalizado: [''],
      fechaSiembra: ['', Validators.required],
      fechaCosechaEstimada: [''],
      notas: ['']
    }, { validators: CultivoForm.fechaCosechaMayorSiembra });
  }

  ngOnInit(): void {
    this.http.get<ApiResponse<PagedResponse<TipoCultivoResumen>>>(
      `${environment.apiUrl}/tipos-cultivo?size=50`
    ).subscribe({
      next: res => {
        if (res.data) this.tiposCultivo.set(res.data.content);
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    this.cargando.set(true);

    const fechaSiembra = this.form.value.fechaSiembra instanceof Date
      ? this.form.value.fechaSiembra.toISOString().split('T')[0]
      : this.form.value.fechaSiembra;

    const fechaCosechaEstimada = this.form.value.fechaCosechaEstimada
      ? (this.form.value.fechaCosechaEstimada instanceof Date
          ? this.form.value.fechaCosechaEstimada.toISOString().split('T')[0]
          : this.form.value.fechaCosechaEstimada)
      : null;

    this.cultivoService.crear({
      parcelaId: this.data.parcelaId,
      ...this.form.value,
      fechaSiembra,
      fechaCosechaEstimada
    }).subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open('Cultivo creado', 'Cerrar',
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