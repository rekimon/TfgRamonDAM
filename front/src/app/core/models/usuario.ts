import { RolNombre } from './auth';

export type EstadoUsuario = 'PENDIENTE' | 'ACTIVO' | 'INACTIVO' | 'RECHAZADO';

export interface Usuario {
  id: number;
  nombre: string;
  apellidos: string;
  email: string;
  telefono?: string;
  estado: EstadoUsuario;
  rol: RolNombre;
  ultimoAcceso?: string;
  createdAt: string;
}

export interface AprobarUsuarioRequest {
  rol: RolNombre;
}