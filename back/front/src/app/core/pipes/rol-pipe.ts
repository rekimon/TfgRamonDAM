import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'rol', standalone: true })
export class RolPipe implements PipeTransform {
  transform(rol: string | null | undefined): string {
    switch (rol) {
      case 'ROLE_ADMIN':   return 'Administrador';
      case 'ROLE_OWNER':   return 'Jefe';
      case 'ROLE_WORKER':  return 'Trabajador';
      default:             return rol ?? '';
    }
  }
}