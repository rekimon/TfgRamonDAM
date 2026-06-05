import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Sidebar } from '../sidebar/sidebar';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, Navbar, Sidebar],
  template: `
    <app-navbar (toggleSidebar)="sidebarAbierto.set(!sidebarAbierto())" />

    <app-sidebar [abierto]="sidebarAbierto()" />

    <div class="main-content" [class.expanded]="!sidebarAbierto()">
      <router-outlet />
    </div>
  `,
  styles: [`
    .main-content {
      margin-top: 64px;
      margin-left: 240px;
      padding: 24px;
      min-height: calc(100vh - 64px);
      background: #f5f5f5;
      transition: margin-left 0.3s ease;

      &.expanded {
        margin-left: 64px;
      }
    }
  `]
})
export class Layout {
  sidebarAbierto = signal(true);
}