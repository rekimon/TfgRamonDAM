import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const rolesPermitidos: string[] = route.data['roles'] ?? [];
  const rolUsuario = authService.getRol();

  if (!rolUsuario) {
    router.navigate(['/login']);
    return false;
  }

  if (rolesPermitidos.length === 0 || rolesPermitidos.includes(rolUsuario)) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};