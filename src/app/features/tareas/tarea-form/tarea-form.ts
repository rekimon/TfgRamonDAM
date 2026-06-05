import { Component, Inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TareaService } from '../../../core/services/tarea';
import { UsuarioService } from '../../../core/services/usuario';
import { Usuario } from '../../../core/models/usuario';
import { AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-tarea-form',
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
    MatSnackBarModule
  ],
  template: `
    <h2 mat-dialog-title>
      <mat-icon>task</mat-icon>
      {{ data.tarea ? 'Editar Tarea' : 'Nueva Tarea' }}
    </h2>

    <mat-dialog-content>
      <form [formGroup]="form" class="form-grid">

        <mat-form-field appearance="outline" class="full">
          <mat-label>Titulo</mat-label>
          <input matInput formControlName="titulo">
          @if (form.get('titulo')?.hasError('required') &&
               form.get('titulo')?.touched) {
            <mat-error>Obligatorio</mat-error>
          }
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Tipo</mat-label>
          <mat-select formControlName="tipo">
            <mat-option value="RIEGO">Riego</mat-option>
            <mat-option value="FERTILIZACION">Fertilizacion</mat-option>
            <mat-option value="PODA">Poda</mat-option>
            <mat-option value="RECOLECCION">Recoleccion</mat-option>
            <mat-option value="MANTENIMIENTO">Mantenimiento</mat-option>
            <mat-option value="OTRO">Otro</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Prioridad</mat-label>
          <mat-select formControlName="prioridad">
            <mat-option value="BAJA">Baja</mat-option>
            <mat-option value="MEDIA">Media</mat-option>
            <mat-option value="ALTA">Alta</mat-option>
            <mat-option value="URGENTE">Urgente</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline" class="full">
          <mat-label>Asignar a trabajador (opcional)</mat-label>
          <mat-select formControlName="asignadoAId">
            <mat-option [value]="null">Sin asignar</mat-option>
            @for (worker of workers(); track worker.id) {
              <mat-option [value]="worker.id">
                {{ worker.nombre }} {{ worker.apellidos }}
              </mat-option>
            }
          </mat-select>
          <mat-icon matPrefix>person</mat-icon>
        </mat-form-field>

       <mat-form-field appearance="outline" class="full">
  <mat-label>Fecha prevista</mat-label>
  <input matInput type="date" formControlName="fechaPrevista"
         [min]="hoy">
  @if (form.get('fechaPrevista')?.hasError('required') &&
       form.get('fechaPrevista')?.touched) {
    <mat-error>Obligatorio</mat-error>
  }
  @if (form.get('fechaPrevista')?.hasError('fechaAnterior')) {
    <mat-error>La fecha no puede ser anterior a hoy</mat-error>
  }
</mat-form-field>

        <mat-form-field appearance="outline" class="full">
          <mat-label>Descripcion</mat-label>
          <textarea matInput formControlName="descripcion" rows="3">
          </textarea>
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
`]
})
export class TareaForm implements OnInit {

  form: FormGroup;
  cargando = signal(false);
  workers = signal<Usuario[]>([]);
  hoy = new Date().toISOString().split('T')[0];

  constructor(
    private fb: FormBuilder,
    private tareaService: TareaService,
    private usuarioService: UsuarioService,
    private snackBar: MatSnackBar,
    public dialogRef: MatDialogRef<TareaForm>,
    @Inject(MAT_DIALOG_DATA) public data: {
      parcelaId: number;
      tarea?: any;
    }
  ) {
    this.form = this.fb.group({
      titulo: [data.tarea?.titulo ?? '', Validators.required],
      tipo: [data.tarea?.tipo ?? 'RIEGO', Validators.required],
      prioridad: [data.tarea?.prioridad ?? 'MEDIA'],
      asignadoAId: [data.tarea?.asignadoAId ?? null],
     fechaPrevista: [data.tarea?.fechaPrevista ?? '', [
     Validators.required,
      TareaForm.fechaNoAnterior
]],
      descripcion: [data.tarea?.descripcion ?? '']
    });
  }

  ngOnInit(): void {
    this.usuarioService.listarWorkers().subscribe({
      next: res => {
        if (res.data) this.workers.set(res.data.content);
      }
    });
  }

  guardar(): void {
    if (this.form.invalid) return;
    this.cargando.set(true);

    const esEdicion = !!this.data.tarea;

    const request$ = esEdicion
      ? this.tareaService.actualizar(this.data.tarea.id, this.form.value)
      : this.tareaService.crear({
          parcelaId: this.data.parcelaId,
          ...this.form.value
        });

    request$.subscribe({
      next: () => {
        this.cargando.set(false);
        this.snackBar.open(
          esEdicion ? 'Tarea actualizada' : 'Tarea creada',
          'Cerrar', { duration: 3000 });
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
  static fechaNoAnterior(control: AbstractControl): ValidationErrors | null {
  if (!control.value) return null;
  const hoy = new Date();
  hoy.setHours(0, 0, 0, 0);
  const fecha = new Date(control.value);
  return fecha < hoy ? { fechaAnterior: true } : null;
}
}