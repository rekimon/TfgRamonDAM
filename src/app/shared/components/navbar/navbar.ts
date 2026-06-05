import { Component, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../../core/services/auth';
import { RolPipe } from '../../../core/pipes/rol-pipe';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatBadgeModule,
    MatDividerModule,
    RolPipe
  ],
  template: `
    <mat-toolbar class="navbar">
      <button mat-icon-button (click)="toggleSidebar.emit()">
        <mat-icon>menu</mat-icon>
      </button>

      <div class="navbar-brand">
        <mat-icon class="brand-icon">eco</mat-icon>
        <span class="brand-name">AgroGestion</span>
      </div>

      <span class="spacer"></span>

      <div class="navbar-actions">
        <span class="user-info">
          <mat-icon>person</mat-icon>
          {{ authService.currentUser()?.nombre }}
        </span>

        <button mat-icon-button [matMenuTriggerFor]="userMenu">
          <mat-icon>account_circle</mat-icon>
        </button>

        <mat-menu #userMenu="matMenu">
          <div class="menu-header">
            <strong>{{ authService.currentUser()?.nombre }}</strong>
            <small>{{ authService.currentUser()?.email }}</small>
            <span class="rol-badge">
              {{ authService.currentUser()?.rol | rol }}
            </span>
          </div>
          <mat-divider></mat-divider>
          <button mat-menu-item (click)="authService.logout()">
            <mat-icon>logout</mat-icon>
            Cerrar sesion
          </button>
        </mat-menu>
      </div>
    </mat-toolbar>
  `,
  styles: [`
    .navbar {
      background: #1b5e20;
      color: white;
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      z-index: 1000;
      box-shadow: 0 2px 8px rgba(0,0,0,0.3);
    }

    .navbar-brand {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-left: 8px;

      .brand-icon {
        color: #a5d6a7;
        font-size: 28px;
        width: 28px;
        height: 28px;
      }

      .brand-name {
        font-size: 20px;
        font-weight: 700;
        letter-spacing: 0.5px;
      }
    }

    .spacer { flex: 1; }

    .navbar-actions {
      display: flex;
      align-items: center;
      gap: 8px;

      .user-info {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 14px;
        opacity: 0.9;

        @media (max-width: 600px) {
          display: none;
        }
      }
    }

    button[mat-icon-button] {
      color: #a5d6a7 !important;

      &:hover {
        color: white !important;
        background: rgba(255,255,255,0.1);
      }
    }

    .menu-header {
      padding: 12px 16px;
      display: flex;
      flex-direction: column;
      gap: 2px;

      strong { font-size: 14px; }
      small { font-size: 12px; color: #666; }

      .rol-badge {
        margin-top: 4px;
        font-size: 11px;
        padding: 2px 8px;
        background: #e8f5e9;
        color: #1b5e20;
        border-radius: 12px;
        width: fit-content;
      }
    }
  `]
})
export class Navbar {
  toggleSidebar = output<void>();

  constructor(public authService: AuthService, private router: Router) {}
}