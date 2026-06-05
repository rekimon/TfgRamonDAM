import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login')
      .then(m => m.Login)
  },
  {
    path: 'registro',
    loadComponent: () => import('./features/auth/registro/registro')
      .then(m => m.Registro)
  },
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./shared/components/layout/layout')
      .then(m => m.Layout),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard/dashboard')
          .then(m => m.Dashboard)
      },
      {
        path: 'parcelas',
        loadComponent: () => import('./features/parcelas/parcela-list/parcela-list')
          .then(m => m.ParcelaList)
      },
      {
        path: 'parcelas/:id',
        loadComponent: () => import('./features/parcelas/parcela-detail/parcela-detail')
          .then(m => m.ParcelaDetail)
      },
      {
        path: 'alertas',
        loadComponent: () => import('./features/alertas/alerta-list/alerta-list')
          .then(m => m.AlertaList)
      },
      {
        path: 'tareas',
        loadComponent: () => import('./features/tareas/tarea-list/tarea-list')
          .then(m => m.TareaList)
      },
      {
        path: 'admin',
        canActivate: [roleGuard],
        data: { roles: ['ROLE_ADMIN'] },
        loadComponent: () => import('./features/admin/usuario-list/usuario-list')
          .then(m => m.UsuarioList)
      },
      {
  path: 'sensores/historico',
  loadComponent: () => import('./features/sensores/sensor-historico/sensor-historico')
    .then(m => m.SensorHistorico)
},
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];