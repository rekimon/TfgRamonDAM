import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { ParcelaService } from '../../../core/services/parcela';
import { AuthService } from '../../../core/services/auth';
import { Parcela } from '../../../core/models/parcela';
import { ParcelaForm } from '../parcela-form/parcela-form';

@Component({
  selector: 'app-parcela-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatChipsModule
  ],
  template: `
    <div class="page-container">

      <div class="page-header">
        <div>
          <h1>Parcelas</h1>
          <p>Gestion de tus explotaciones agricolas</p>
        </div>
        @if (authService.isOwner() || authService.isAdmin()) {
          <button mat-raised-button color="primary" (click)="abrirFormulario()">
            <mat-icon>add</mat-icon>
            Nueva parcela
          </button>
        }
      </div>

      <mat-form-field appearance="outline" class="busqueda">
        <mat-label>Buscar parcelas</mat-label>
        <input matInput [formControl]="busqueda"
               placeholder="Nombre, municipio, provincia...">
        <mat-icon matPrefix>search</mat-icon>
      </mat-form-field>

      @if (cargando()) {
        <div class="loading">
          <mat-spinner diameter="48"></mat-spinner>
        </div>
      } @else if (parcelas().length === 0) {
        <div class="empty-state">
          <mat-icon>terrain</mat-icon>
          <h3>No hay parcelas registradas</h3>
          <p>Crea tu primera parcela para comenzar</p>
          @if (authService.isOwner() || authService.isAdmin()) {
            <button mat-raised-button color="primary"
                    (click)="abrirFormulario()">
              Crear parcela
            </button>
          }
        </div>
      } @else {
        <div class="parcelas-grid">
          @for (parcela of parcelas(); track parcela.id) {
            <mat-card class="parcela-card" (click)="verDetalle(parcela.id)">
              <mat-card-header>
                <mat-icon mat-card-avatar class="parcela-avatar">terrain</mat-icon>
                <mat-card-title>{{ parcela.nombre }}</mat-card-title>
                <mat-card-subtitle>
                  {{ parcela.municipio }}, {{ parcela.provincia }}
                </mat-card-subtitle>
              </mat-card-header>

              <mat-card-content>
                <div class="parcela-stats">
                  <div class="stat">
                    <mat-icon>straighten</mat-icon>
                    <span>{{ parcela.superficieHa }} ha</span>
                  </div>
                  <div class="stat">
                    <mat-icon>location_on</mat-icon>
                    <span>{{ parcela.latitud | number:'1.4-4' }},
                          {{ parcela.longitud | number:'1.4-4' }}</span>
                  </div>
                  @if (parcela.referenciaCatastral) {
                    <div class="stat">
                      <mat-icon>article</mat-icon>
                      <span>{{ parcela.referenciaCatastral }}</span>
                    </div>
                  }
                </div>
              </mat-card-content>

              <mat-card-actions>
                <button mat-button color="primary"
                        (click)="verDetalle(parcela.id); $event.stopPropagation()">
                  <mat-icon>visibility</mat-icon>
                  Ver detalle
                </button>
                @if (authService.isOwner() || authService.isAdmin()) {
                  <button mat-button color="accent"
                          (click)="abrirFormulario(parcela); $event.stopPropagation()">
                    <mat-icon>edit</mat-icon>
                    Editar
                  </button>
                  <button mat-button color="warn"
                          (click)="eliminar(parcela); $event.stopPropagation()">
                    <mat-icon>delete</mat-icon>
                  </button>
                }
              </mat-card-actions>
            </mat-card>
          }
        </div>
      }

    </div>
  `,
  styles: [`
    .page-container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 24px;

      h1 {
        margin: 0;
        font-size: 28px;
        font-weight: 700;
        color: #1b5e20;
      }

      p { margin: 4px 0 0; color: #666; }
    }

    .busqueda {
      width: 100%;
      max-width: 400px;
      margin-bottom: 24px;
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

    .parcelas-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
      gap: 16px;
    }

    .parcela-card {
      border-radius: 12px;
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 24px rgba(0,0,0,0.12);
      }

      .parcela-avatar {
        background: #e8f5e9;
        color: #2e7d32;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        width: 40px;
        height: 40px;
      }
    }

    .parcela-stats {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-top: 8px;

      .stat {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 13px;
        color: #555;

        mat-icon {
          font-size: 16px;
          width: 16px;
          height: 16px;
          color: #2e7d32;
        }
      }
    }
  `]
})
export class ParcelaList implements OnInit {

  parcelas = signal<Parcela[]>([]);
  cargando = signal(true);
  busqueda = new FormControl('');

  constructor(
    public authService: AuthService,
    private parcelaService: ParcelaService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.cargar();

    this.busqueda.valueChanges.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(valor => {
      this.cargar(valor ?? '');
    });
  }

  cargar(busqueda = ''): void {
    this.cargando.set(true);
    this.parcelaService.listar(busqueda).subscribe({
      next: res => {
        if (res.data) this.parcelas.set(res.data.content);
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });
  }

  verDetalle(id: number): void {
    this.router.navigate(['/parcelas', id]);
  }

  abrirFormulario(parcela?: Parcela): void {
    const ref = this.dialog.open(ParcelaForm, {
      width: '560px',
      data: parcela ?? null
    });

    ref.afterClosed().subscribe(resultado => {
      if (resultado) this.cargar();
    });
  }

  eliminar(parcela: Parcela): void {
    if (!confirm(`Eliminar la parcela "${parcela.nombre}"?`)) return;

    this.parcelaService.eliminar(parcela.id).subscribe({
      next: () => {
        this.snackBar.open('Parcela eliminada', 'Cerrar',
          { duration: 3000 });
        this.cargar();
      },
      error: () => this.snackBar.open('Error al eliminar', 'Cerrar',
        { duration: 3000 })
    });
  }
}