import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../../../core/services/auth';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  roles?: string[];
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    MatListModule,
    MatIconModule,
    MatDividerModule
  ],
  template: `
    <div class="sidebar" [class.collapsed]="!abierto()">
      <div class="sidebar-content">
        <mat-nav-list>
          @for (item of itemsFiltrados(); track item.route) {
            <a mat-list-item
               [routerLink]="item.route"
               routerLinkActive="active-link"
               [routerLinkActiveOptions]="{ exact: item.route === '/dashboard' }">
              <mat-icon matListItemIcon>{{ item.icon }}</mat-icon>
              @if (abierto()) {
                <span matListItemTitle>{{ item.label }}</span>
              }
            </a>
          }
        </mat-nav-list>
      </div>
    </div>
  `,
  styles: [`
    .sidebar {
      position: fixed;
      top: 64px;
      left: 0;
      bottom: 0;
      width: 240px;
      background: #fff;
      border-right: 1px solid #e0e0e0;
      transition: width 0.3s ease;
      z-index: 900;
      overflow-x: hidden;
      box-shadow: 2px 0 8px rgba(0,0,0,0.08);

      &.collapsed {
        width: 64px;
      }
    }

    .sidebar-content {
      padding: 8px 0;
    }

    a[mat-list-item] {
      border-radius: 0 24px 24px 0;
      margin-right: 8px;
      color: #333;
      transition: background 0.2s;

      &:hover {
        background: #f1f8e9;
        color: #1b5e20;
      }

      &.active-link {
        background: #e8f5e9;
        color: #1b5e20;
        font-weight: 600;

        mat-icon { color: #2e7d32; }
      }
    }
  `]
})
export class Sidebar {
  abierto = input<boolean>(true);

private navItems: NavItem[] = [
  { label: 'Dashboard', icon: 'dashboard', route: '/dashboard' },
  { label: 'Parcelas', icon: 'terrain', route: '/parcelas' },
  { label: 'Historico Sensores', icon: 'show_chart',
    route: '/sensores/historico' },
  { label: 'Alertas', icon: 'notifications_active', route: '/alertas' },
  { label: 'Tareas', icon: 'task', route: '/tareas' },
  { label: 'Administracion', icon: 'admin_panel_settings',
    route: '/admin', roles: ['ROLE_ADMIN'] }
];
  constructor(private authService: AuthService) {}

  itemsFiltrados() {
    const rol = this.authService.getRol();
    return this.navItems.filter(item =>
      !item.roles || item.roles.includes(rol ?? '')
    );
  }
}